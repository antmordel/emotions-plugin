package es.uma.lcc.e_motions.transformations.model2model;

import org.eclipse.m2m.atl.core.IExtractor;
import org.eclipse.m2m.atl.core.IInjector;
import org.eclipse.m2m.atl.core.ModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFExtractor;
import org.eclipse.m2m.atl.core.emf.EMFInjector;
import org.eclipse.m2m.atl.core.emf.EMFModelFactory;

public class ATLTransformation {
	
	protected ModelFactory mf;
	protected IInjector injector;
	protected IExtractor extractor;
	
	public ATLTransformation() {
		mf = new EMFModelFactory();
		injector = new EMFInjector();
		extractor = new EMFExtractor();
	}
}
