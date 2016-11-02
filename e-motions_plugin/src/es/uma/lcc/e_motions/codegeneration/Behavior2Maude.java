package es.uma.lcc.e_motions.codegeneration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
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

import behavior.BehaviorPackage;
import es.uma.lcc.e_motions.common.ExecutionData;
import es.uma.lcc.e_motions.common.FileManager;
import es.uma.lcc.e_motions.common.Printer;
import es.uma.lcc.e_motions.maudeprocess.exceptions.MaudelingException;
import es.uma.lcc.e_motions.metamodels.Metamodels;
import es.uma.lcc.e_motions.transformations.Transformations;

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

public class Behavior2Maude {

	/**
	 * 
	 * @param behaviorModel
	 *            Full path of the behavior model (.behavior)
	 * @param metamodel
	 *            Full path of the metamodel
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws CoreException
	 * @throws MaudelingException
	 * @throws ATLCoreException
	 */
	public static void behOcl2Maude(String behaviorModel, String metamodel) throws IOException,
			URISyntaxException, CoreException, MaudelingException, ATLCoreException {
		
		long startTime = System.currentTimeMillis();
		Printer _p = Printer.getDefault();
		
		OclBehaviorParser.oclParser(behaviorModel, metamodel);

		/**
		 * ATL transformation header:
		 * 
		 * create OUT : Maude from IN : Behavior, INATL : ATL;
		 */

		_p.debug("1 - Starting to transform behavior.");
		ModelFactory mF = new EMFModelFactory();
		IInjector injector = new EMFInjector();
		IExtractor extractor = new EMFExtractor();
		
		/* Loading Maude Metamodel */
		IReferenceModel maudeMM = mF.newReferenceModel();
		injector.inject(maudeMM, Metamodels.class.getResource("Maude.ecore").openStream(),
				new HashMap<String, Object>());
		_p.debug("2 - Maude MM loaded.");

		/* Loading Behavior Metamodel */
		IReferenceModel behMM = mF.newReferenceModel();
		injector.inject(behMM, URI.createURI(BehaviorPackage.eNS_URI).toString());
		_p.debug("3 - Behavior MM loaded.");

		/* Loading ATL Metamodel */
		IReferenceModel atlMM = mF.newReferenceModel();
		injector.inject(atlMM, Metamodels.class.getResource("ATL.ecore").openStream(), new HashMap<String, Object>());
		_p.debug("4 - ATL MM loaded.");
		
		/* Loading Behavior Model */
		IModel behModel = mF.newModel(behMM);
		_p.debug("* - Behavior Model: <"+behaviorModel+">.");
		injector.inject(behModel, behaviorModel);
		_p.debug("5 - Behavior Model loaded.");
		
		/* Loading OCL Parser */
		IModel oclModel = mF.newModel(atlMM);
		injector.inject(oclModel, "file:///" + FileManager.getDefault().getOCLParserModel().getLocation().toOSString());

		/* Loading Maude Model */
		IModel maudeModel = mF.newModel(maudeMM);

		/* Run transformation */
		ILauncher transformationLauncher = new EMFVMLauncher();

		transformationLauncher.initialize(new HashMap<String, Object>());
		transformationLauncher.addInModel(behModel, "IN", "Behavior");
		transformationLauncher.addInModel(oclModel, "INATL", "ATL");
		transformationLauncher.addOutModel(maudeModel, "OUT", "Maude");

		Map<String, Object> options = new HashMap<String, Object>();
		options.put("allowInterModelReferences", Boolean.TRUE);
		
		if (ExecutionData.getDefault().isTrackingActions()) {
			transformationLauncher.launch(ILauncher.RUN_MODE, null, options,
					Transformations.class.getResourceAsStream("BehOcl2MaudeTrack.asm"));
		} else {
			transformationLauncher.launch(ILauncher.RUN_MODE, null, options,
					Transformations.class.getResourceAsStream("BehOcl2Maude.asm"));
		}
		_p.debug("6 - Transformation launched.");

		/* Extract resulting model */
		String project = FileManager.getDefault().getBehavior().getProject().getName();
		
		FileManager _fm = FileManager.getDefault();
		_fm.setBehaviorMaude(
				new Path("platform:/resource/" + project + "/" + FileManager.TMP_FOLDER + "/" + FileManager.BEHAVIOR_TMP_FILE));
		_p.debug("7 - Output model: " + _fm.getBehaviorMaude().toOSString());
		extractor.extract(maudeModel, _fm.getBehaviorMaude().toOSString());
		_p.debug("8 - Output model saved.");

		long endTime = System.currentTimeMillis();
		DecimalFormat df = new DecimalFormat("#0.000");
		String _tracking = ExecutionData.getDefault().isTrackingActions()?"(with tracking of actions)":"(without tracking of actions)";
		_p.println("Behavior2Maude M2M transformation " +_tracking+" in " + df.format((endTime - startTime) / 1000.0) + " seconds.");
	}
}
