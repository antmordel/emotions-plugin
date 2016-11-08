package es.uma.lcc.e_motions.codegeneration;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

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
import es.uma.lcc.e_motions.metamodels.Metamodels;
import es.uma.lcc.e_motions.running_information.PalladioRunningInformation;
import es.uma.lcc.e_motions.transformations.Transformations;

/**
 * @copyright Antonio Moreno-Delgado <i>amoreno@lcc.uma.es</i>
 * @date July 31th 2014
 * 
 * 
 *       This file is part of e-Motions. It has been generated with Xtend.
 *
 *       e-Motions is free software: you can redistribute it and/or modify it
 *       under the terms of the GNU General Public License as published by the
 *       Free Software Foundation, either version 3 of the License, or (at your
 *       option) any later version.
 * 
 *       e-Motions is distributed in the hope that it will be useful, but
 *       WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *       General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License along
 *       with e-Motions. If not, see <http://www.gnu.org/licenses/>.
 */
public class EcoreMM2Maude {

	public static void ecore2Maude(String metamodelString) throws ATLCoreException, IOException {
		long startTime = System.currentTimeMillis();
		FileManager _fm = FileManager.getDefault();
		Printer _p = Printer.getDefault();

		/**
		 * ATL transformation header:
		 * 
		 * create OUT : Maude from IN : Ecore;
		 */
		_p.debug("1 - Starting to transform metamodel.");
		ModelFactory mF = new EMFModelFactory();
		IInjector injector = new EMFInjector();
		IExtractor extractor = new EMFExtractor();

		/* Loading Ecore MM */
		IReferenceModel ecoreMM = mF.newReferenceModel();
		injector.inject(ecoreMM, "http://www.eclipse.org/emf/2002/Ecore");
		_p.debug("2 - EcoreMM loaded.");

		/* Loading Maude Metamodel */
		IReferenceModel maudeMM = mF.newReferenceModel();
		injector.inject(maudeMM, Metamodels.class.getResource("Maude.ecore").openStream(),
				new HashMap<String, Object>());
		_p.debug("3 - Maude MM loaded.");

		/* Loading Ecore model, i.e., the Metamodel of the system */
		IModel mmModel = mF.newModel(ecoreMM);
		_p.debug("* - Metamodel: <" + metamodelString + ">.");
		injector.inject(mmModel, metamodelString);
		_p.debug("4 - Metamodel loaded.");

		/* Loading Maude Model */
		IModel maudeModel = mF.newModel(maudeMM);

		/* Run transformation */
		ILauncher transformationLauncher = new EMFVMLauncher();

		transformationLauncher.initialize(new HashMap<String, Object>());
		transformationLauncher.addInModel(mmModel, "IN", "Ecore");
		transformationLauncher.addOutModel(maudeModel, "OUT", "Maude");

		Map<String, Object> options = new HashMap<String, Object>();

		transformationLauncher.launch(ILauncher.RUN_MODE, null, options,
				Transformations.class.getResourceAsStream("EcoreMM2Maude.asm"));
		_p.debug("5 - Transformation finished.");

		/* Extract resulting model */
		String project = null;
		if (_fm.getBehavior() != null) {
			_fm.getBehavior().getProject().getName();
		} else {
			PalladioRunningInformation.getDefault().getBehaviorModel().getProject().getName();
		}
		String metamodelName = _fm.getMetamodel() != null ? _fm.getMetamodel().getName()
				: PalladioRunningInformation.getDefault().getMetamodel().getName();
		String outFile = metamodelName.substring(0, metamodelName.indexOf(".")) + ".xmi";
		_fm.setMetamodelMaude(new Path("platform:/resource/" + project + "/" + FileManager.TMP_FOLDER + "/" + outFile));
		_p.debug("7 - Output model: " + _fm.getMetamodelMaude().toOSString());
		extractor.extract(maudeModel, _fm.getMetamodelMaude().toOSString());
		_p.debug("8 - Output model saved.");

		long endTime = System.currentTimeMillis();
		DecimalFormat df = new DecimalFormat("#0.000");
		_p.println("EcoreMM2Maude M2M transformation in " + df.format((endTime - startTime) / 1000.0) + " seconds.");
	}
	
	public static void ecore2MaudePalladio(String metamodelString) throws ATLCoreException, IOException {
		long startTime = System.currentTimeMillis();

		/**
		 * ATL transformation header:
		 * 
		 * create OUT : Maude from IN : Ecore;
		 */
		ModelFactory mF = new EMFModelFactory();
		IInjector injector = new EMFInjector();
		IExtractor extractor = new EMFExtractor();

		/* Loading Ecore MM */
		IReferenceModel ecoreMM = mF.newReferenceModel();
		injector.inject(ecoreMM, "http://www.eclipse.org/emf/2002/Ecore");

		/* Loading Maude Metamodel */
		IReferenceModel maudeMM = mF.newReferenceModel();
		injector.inject(maudeMM, Metamodels.class.getResource("Maude.ecore").openStream(),
				new HashMap<String, Object>());

		/* Loading Ecore model, i.e., the Metamodel of the system */
		IModel mmModel = mF.newModel(ecoreMM);
		injector.inject(mmModel, metamodelString);

		/* Loading Maude Model */
		IModel maudeModel = mF.newModel(maudeMM);

		/* Run transformation */
		ILauncher transformationLauncher = new EMFVMLauncher();

		transformationLauncher.initialize(new HashMap<String, Object>());
		transformationLauncher.addInModel(mmModel, "IN", "Ecore");
		transformationLauncher.addOutModel(maudeModel, "OUT", "Maude");

		Map<String, Object> options = new HashMap<String, Object>();

		transformationLauncher.launch(ILauncher.RUN_MODE, null, options,
				Transformations.class.getResourceAsStream("EcoreMM2Maude.asm"));

		/* Extract resulting model */
		PalladioRunningInformation info = PalladioRunningInformation.getDefault();
		
		String project = PalladioRunningInformation.getDefault().getBehaviorModel().getProject().getName();
		String metamodelName = PalladioRunningInformation.getDefault().getMetamodel().getName();
		String outFile = metamodelName.substring(0, metamodelName.indexOf(".")) + ".xmi";
		info.setMetamodelMaude(new Path("platform:/resource/" + project + "/" + FileManager.TMP_FOLDER + "/" + outFile));
		extractor.extract(maudeModel, info.getMetamodelMaude().toOSString());
	}
}
