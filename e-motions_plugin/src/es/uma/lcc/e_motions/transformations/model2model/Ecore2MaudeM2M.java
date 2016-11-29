package es.uma.lcc.e_motions.transformations.model2model;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.IModel;
import org.eclipse.m2m.atl.core.IReferenceModel;
import org.eclipse.m2m.atl.core.launch.ILauncher;
import org.eclipse.m2m.atl.engine.emfvm.launch.EMFVMLauncher;

import es.uma.lcc.e_motions.Activator;
import es.uma.lcc.e_motions.common.EmotionsPaths;

public class Ecore2MaudeM2M extends EmotionsTransformation {
	/**
	 * ATL transformation header:
	 * 
	 * module ec2ma; -- Module Template
	 * create OUT : Maude from IN : Ecore; --, INPAR : Parameter;
	 */
	
	private IFile input;
	private IFile output;
	
	public Ecore2MaudeM2M(IFile input, IFile output) {
		super();
		this.input = input;
		this.output = output;
	}

	public void execute() throws ATLCoreException, IOException {
		IReferenceModel ecoreMM = mf.newReferenceModel();
		injector.inject(ecoreMM, "http://www.eclipse.org/emf/2002/Ecore");
		
		IReferenceModel maudeMM = mf.newReferenceModel();
		injector.inject(maudeMM, EmotionsPaths.MAUDE, new HashMap<String, Object>());
		
		IModel mmModel = mf.newModel(ecoreMM);
		System.out.println(input);
		injector.inject(mmModel, path(input));
		
		IModel maudeModel = mf.newModel(maudeMM);
		
		/* Run transformation */
		ILauncher transformationLauncher = new EMFVMLauncher();

		transformationLauncher.initialize(new HashMap<String, Object>());
		transformationLauncher.addInModel(mmModel, "IN", "Ecore");
		transformationLauncher.addOutModel(maudeModel, "OUT", "Maude");

		Map<String, Object> options = new HashMap<String, Object>();
		
		URL trafoURL = Activator.getDefault().getBundle().getEntry(EmotionsPaths.ECORE2MAUDE);
		transformationLauncher.launch(ILauncher.RUN_MODE, null, options, new Object[] { trafoURL.openStream() });
		
		/* serialize model */
		extractor.extract(maudeModel, path(output));
	}
}
