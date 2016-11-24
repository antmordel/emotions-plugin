package es.uma.lcc.e_motions.transformations.model2model;

import java.io.IOException;
import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.IReferenceModel;

import es.uma.lcc.e_motions.common.EmotionsPaths;
import es.uma.lcc.e_motions.metamodels.Metamodels;

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

	public Behavior2MaudeM2M(IFile modelBehavior, IFile modelMetamodel, IFile modelAtl) {
		super();
		this.modelBehavior = modelBehavior;
		this.modelMetamodel = modelMetamodel;
		this.modelAtl = modelAtl;
	}

	@Override
	public void execute() throws ATLCoreException, IOException {
		/* Loading Maude Metamodel */
		IReferenceModel maudeMM = mf.newReferenceModel();
		injector.inject(maudeMM, EmotionsPaths.MAUDE, new HashMap<String, Object>());

	}

}
