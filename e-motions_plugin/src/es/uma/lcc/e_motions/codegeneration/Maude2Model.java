/**
 * @copyright Antonio Moreno-Delgado <i>amoreno@lcc.uma.es</i>
 * @date July 31th 2014
 * 
 * 
 *  This file is part of e-Motions. It has been generated with Xtend.
 *
 *  e-Motions is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  e-Motions is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with e-Motions.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.uma.lcc.e_motions.codegeneration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.IExtractor;
import org.eclipse.m2m.atl.core.IInjector;
import org.eclipse.m2m.atl.core.IModel;
import org.eclipse.m2m.atl.core.IReferenceModel;
import org.eclipse.m2m.atl.core.ModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFExtractor;
import org.eclipse.m2m.atl.core.emf.EMFInjector;
import org.eclipse.m2m.atl.core.emf.EMFModelFactory;
import org.eclipse.m2m.atl.core.launch.ILauncher;
import org.eclipse.m2m.atl.engine.compiler.AtlCompiler;
import org.eclipse.m2m.atl.engine.compiler.AtlStandaloneCompiler;
import org.eclipse.m2m.atl.engine.compiler.CompileTimeError;
import org.eclipse.m2m.atl.engine.emfvm.launch.EMFVMLauncher;
import org.eclipse.m2m.atl.engine.parser.AtlParser;

import es.uma.lcc.e_motions.common.FileManager;
import es.uma.lcc.e_motions.common.Printer;
import es.uma.lcc.e_motions.maudeprocess.exceptions.MaudelingException;
import es.uma.lcc.e_motions.metamodels.Metamodels;
import es.uma.lcc.e_motions.transformations.Transformations;


public class Maude2Model {

	public static boolean maude2Model(IFile metamodel) throws IOException,
			URISyntaxException, CoreException, MaudelingException, ATLCoreException {
		boolean res = true;
		long startTime = System.currentTimeMillis();
		Printer _p = Printer.getDefault();
		
		/**
		 * ATL transformation header:
		 * 
		 * create OUT : ATL from IN : Ecore;
		 */

		_p.debug("1 - Starting to transform output model.");
		ModelFactory mF = new EMFModelFactory();
		IInjector injector = new EMFInjector();
		IExtractor extractor = new EMFExtractor();
		
		/* Loading ATL Metamodel */
		IReferenceModel atlMM = mF.newReferenceModel();
		injector.inject(atlMM, Metamodels.class.getResource("ATL.ecore").openStream(), new HashMap<String, Object>());
		_p.debug("2 - ATL MM loaded.");

		/* Loading Ecore Metamodel */
		IReferenceModel ecoreMM = mF.newReferenceModel();
		injector.inject(ecoreMM, "http://www.eclipse.org/emf/2002/Ecore");
		_p.debug("3 - Ecore MM loaded.");
		
		/* Loading user Metamodel Model */
		IModel userMM = mF.newModel(ecoreMM);
		injector.inject(userMM, metamodel.getFullPath().toOSString());
		_p.debug("4 - User Metamodel loaded.");
		
		/* Create output model */
		IModel atlModel = mF.newModel(atlMM);
		_p.debug("5 - Maude model created.");

		/* Run transformation */
		ILauncher transformationLauncher = new EMFVMLauncher();

		transformationLauncher.initialize(new HashMap<String, Object>());
		transformationLauncher.addInModel(userMM, "IN", "Ecore");
		transformationLauncher.addOutModel(atlModel, "OUT", "ATL");

		Map<String, Object> options = new HashMap<String, Object>();
		options.put("allowInterModelReferences", Boolean.TRUE);
		
		transformationLauncher.launch(ILauncher.RUN_MODE, null, options,
		    Transformations.class.getResourceAsStream("Maude2Model.asm"));
		_p.debug("6 - Transformation launched.");

		/* Extract resulting model */
		FileManager _fm = FileManager.getDefault();
		
		AtlParser ap = AtlParser.getDefault();
		ap.extract(atlModel, _fm.getAuxATLInverseTransformation().getLocation().toOSString(), new HashMap<String, Object>());
		_fm.getAuxATLInverseTransformation().refreshLocal(0, null);
		
		_p.debug("7 - Output model: " + _fm.getAuxATLInverseTransformation().getLocation().toOSString());
		
		/**
		 * The created atl transformation has the following header:
		 *  
		 *  module Maude2ModelInverseTransf;
		 *	create OUT : MM from IN : Maude;
		 *  
		 *  uses Maude2ModelLib;
		 */
		
		/* Loading Maude metamodel */
		IReferenceModel maudeMM = mF.newReferenceModel();
		injector.inject(maudeMM, "http://MaudeMM/1.0");
		_p.debug("8 - Maude MM loaded.");
		
		/* Loading User Metamodel */
		IReferenceModel userMetamodel = mF.newReferenceModel();
		injector.inject(userMetamodel, _fm.getMetamodel().getContents(), new HashMap<String, Object>());
		_p.debug("9 - User MM loaded.");
		
		/* Loading output Maude model */
		IModel outputModel = mF.newModel(maudeMM);
		injector.inject(outputModel, _fm.getOutputMaudeModel().getFullPath().toOSString());
		
		/* Out model */
		IModel outUserModel = mF.newModel(userMetamodel);
		
		/* Compiling the ATL transformation */
		AtlStandaloneCompiler compiler = AtlCompiler.getCompiler(AtlCompiler.DEFAULT_COMPILER_NAME);
		CompileTimeError errors[] = compiler.compile(_fm.getAuxATLInverseTransformation().getContents(),
				_fm.getAuxATLInverseTransformationASM().getLocation().toOSString());
		if(errors.length > 0) {
			res = false;
			_p.println("Maude2Model transformation compile errors: "+errors[0].toString());
		}
		_fm.getAuxATLInverseTransformationASM().refreshLocal(0, null);
		_p.debug("10 - Transformation compiled.");
		
		/* Run transformation */
		transformationLauncher.initialize(new HashMap<String, Object>());
		transformationLauncher.addInModel(outputModel, "IN", "Maude");
		transformationLauncher.addOutModel(outUserModel, "OUT", "MM");
		
		transformationLauncher.addLibrary("Maude2ModelLib", Transformations.class.getResource("Maude2ModelLib.asm").openStream());		
		transformationLauncher.launch(ILauncher.RUN_MODE, null, options,
				_fm.getAuxATLInverseTransformationASM().getContents());
		_p.debug("11 - Transformation launched.");
		
		/* Extract output model */
		extractor.extract(outUserModel, _fm.getResult().toOSString());
		
		long endTime = System.currentTimeMillis();
		DecimalFormat df = new DecimalFormat("#0.000");
		_p.println("Model2Maude M2M transformation in " + df.format((endTime - startTime) / 1000.0) + " seconds.");
		
		return res;
	}
}
