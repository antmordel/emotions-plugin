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
import org.eclipse.core.runtime.Path;
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
import org.eclipse.m2m.atl.engine.emfvm.launch.EMFVMLauncher;

import es.uma.lcc.e_motions.common.FileManager;
import es.uma.lcc.e_motions.common.Printer;
import es.uma.lcc.e_motions.maudeprocess.exceptions.MaudelingException;
import es.uma.lcc.e_motions.metamodels.Metamodels;
import es.uma.lcc.e_motions.transformations.Transformations;

public class Model2Maude {

	public static String model2Maude(IFile initialModel, IFile metamodel) throws IOException,
			URISyntaxException, CoreException, MaudelingException, ATLCoreException {
		
		long startTime = System.currentTimeMillis();
		Printer _p = Printer.getDefault();
		
		/*
		 * Falta checkear que ya se ha realizado con anterioridad
		 */

		/**
		 * ATL transformation header:
		 * 
		 * create OUT : Maude from IN : MM, INMM : Ecore, INPAR : Parameter;
		 */

		_p.debug("1 - Starting to transform initial model.");
		ModelFactory mF = new EMFModelFactory();
		IInjector injector = new EMFInjector();
		IExtractor extractor = new EMFExtractor();
		
		/* Loading Maude Metamodel */
		IReferenceModel maudeMM = mF.newReferenceModel();
		injector.inject(maudeMM, Metamodels.class.getResource("Maude.ecore").openStream(),
				new HashMap<String, Object>());
		_p.debug("2 - Maude MM loaded.");

		/* Loading User Metamodel */
		IReferenceModel userMM = mF.newReferenceModel();
		injector.inject(userMM, metamodel.getContents(), new HashMap<String, Object>());
		_p.debug("3 - User MM loaded.");

		/* Loading Ecore Metamodel */
		IReferenceModel ecoreMM = mF.newReferenceModel();
		injector.inject(ecoreMM, "http://www.eclipse.org/emf/2002/Ecore");
		_p.debug("4 - Ecore MM loaded.");
		
		/* Loading Parameter Metamodel */
		IReferenceModel parameterMM = mF.newReferenceModel();
		injector.inject(parameterMM, Metamodels.class.getResource("ATLParameters.ecore").openStream(),
				new HashMap<String, Object>());
		_p.debug("5 - Parameter MM loaded.");
		
		/* Loading User Model */
		IModel userInitialModel = mF.newModel(userMM);
		injector.inject(userInitialModel, initialModel.getFullPath().toOSString());
		_p.debug("5 - User Initial Model loaded.");
		
		/* Loading User Metamodel */
		IModel userMetamodel = mF.newModel(ecoreMM);
		injector.inject(userMetamodel, metamodel.getFullPath().toOSString());
		_p.debug("5 - User Metamodel loaded.");
		
		/* Loading Params model */
		// -> We create the params model file
		String modelName = FileManager.getDefault().getInitialModel().getName();
		modelName = modelName.substring(0, modelName.indexOf('.')).toUpperCase();
		
		IFile paramsFile = FileManager.getDefault().createParamsModelFile(
				ModuleGenerators.createParametersModel(modelName));
		
		IModel paramsModel = mF.newModel(parameterMM);
		injector.inject(paramsModel, paramsFile.getFullPath().toOSString());
		_p.debug("6 - Params model loaded.");
		
		/* Create output model */
		IModel maudeModel = mF.newModel(maudeMM);
		_p.debug("7 - Maude model created.");

		/* Run transformation */
		ILauncher transformationLauncher = new EMFVMLauncher();

		transformationLauncher.initialize(new HashMap<String, Object>());
		transformationLauncher.addInModel(userInitialModel, "IN", "MM");
		transformationLauncher.addInModel(userMetamodel, "INMM", "Ecore");
		transformationLauncher.addInModel(paramsModel, "INPAR", "Parameter");
		transformationLauncher.addOutModel(maudeModel, "OUT", "Maude");

		Map<String, Object> options = new HashMap<String, Object>();
		options.put("allowInterModelReferences", Boolean.TRUE);
		
		transformationLauncher.launch(ILauncher.RUN_MODE, null, options,
		    Transformations.class.getResourceAsStream("Model2Maude.asm"));
		_p.debug("8 - Transformation launched.");

		/* Extract resulting model */
		FileManager _fm = FileManager.getDefault();
		String project = _fm.getBehavior().getProject().getName();
		
		_fm.setInitialModelMaude(
				new Path("platform:/resource/" + project + "/" + FileManager.TMP_FOLDER + "/" + FileManager.INITIAL_MODEL_MAUDE_FILENAME));
		_p.debug("9 - Output model: " + _fm.getInitialModelMaude().toOSString());
		extractor.extract(maudeModel, _fm.getInitialModelMaude().toOSString());
		_p.debug("10 - Output model saved in "+_fm.getInitialModelMaude().toOSString());

		long endTime = System.currentTimeMillis();
		DecimalFormat df = new DecimalFormat("#0.000");
		_p.println("Model2Maude M2M transformation in " + df.format((endTime - startTime) / 1000.0) + " seconds.");
		
		return modelName;
	}
}
