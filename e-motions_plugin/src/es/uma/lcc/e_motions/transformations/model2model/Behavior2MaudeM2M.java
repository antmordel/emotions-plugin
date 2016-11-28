package es.uma.lcc.e_motions.transformations.model2model;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.IModel;
import org.eclipse.m2m.atl.core.IReferenceModel;
import org.eclipse.m2m.atl.core.launch.ILauncher;
import org.eclipse.m2m.atl.engine.emfvm.launch.EMFVMLauncher;

import behavior.BehaviorPackage;
import es.uma.lcc.e_motions.Activator;
import es.uma.lcc.e_motions.common.EmotionsPaths;

public class Behavior2MaudeM2M extends ATLTransformation {

	/**
	 * Some notes
	 * 
	 * This transformation should been modified and improved. It needs a
	 * *pre*-processing transformation for the OCL expressions.
	 * 
	 * This *pre* transformation is done using the {@link OclBehaviorParser} class.
	 * 
	 */

	/**
	 * ATL transformation header:
	 * 
	 * create OUT : Maude from IN : Behavior, INATL : ATL;
	 */

	private IFile modelBehavior;
	private IFile modelMetamodel;
	private IFile modelAtl;
	private IFile modelOutput;

	public Behavior2MaudeM2M(IFile modelBehavior, IFile modelMetamodel, IFile modelAtl, IFile modelOutput) {
		super();
		this.modelBehavior = modelBehavior;
		this.modelMetamodel = modelMetamodel;
		this.modelAtl = modelAtl;
		this.modelOutput = modelOutput;
	}

	@Override
	public void execute() throws ATLCoreException, IOException {
		
		/* Loading Maude Metamodel */
		IReferenceModel maudeMM = mf.newReferenceModel();
		injector.inject(maudeMM, EmotionsPaths.MAUDE, new HashMap<String, Object>());
		
		/* Loading Behavior Metamodel */
		IReferenceModel behMM = mf.newReferenceModel();
		injector.inject(behMM, URI.createURI(BehaviorPackage.eNS_URI).toString());
		
		/* Loading ATL Metamodel */
		IReferenceModel atlMM = mf.newReferenceModel();
		injector.inject(atlMM, EmotionsPaths.ATL, new HashMap<String, Object>());
		
		/* Loading Behavior Model */
		IModel behModel = mf.newModel(behMM);
		injector.inject(behModel, "file:///" + modelBehavior.getLocation().toPortableString());
		
		/* Loading OCL Parser */
		IModel oclModel = mf.newModel(atlMM);
		injector.inject(oclModel, path(modelAtl));
		
		/* Loading Maude Model */
		IModel maudeModel = mf.newModel(maudeMM);
		
		/* Run transformation */
		ILauncher transformationLauncher = new EMFVMLauncher();

		transformationLauncher.initialize(new HashMap<String, Object>());
		transformationLauncher.addInModel(behModel, "IN", "Behavior");
		transformationLauncher.addInModel(oclModel, "INATL", "ATL");
		transformationLauncher.addOutModel(maudeModel, "OUT", "Maude");
		
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("allowInterModelReferences", Boolean.TRUE);
		
		URL trafoURL = Activator.getDefault().getBundle().getEntry(EmotionsPaths.BEHAVIOR2MAUDE);
		transformationLauncher.launch(ILauncher.RUN_MODE, null, options, new Object[] { trafoURL.openStream() });
		
		/* serialize model */
		extractor.extract(maudeModel, path(modelOutput));
	}

}
