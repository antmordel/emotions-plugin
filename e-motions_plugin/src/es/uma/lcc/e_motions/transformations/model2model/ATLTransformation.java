package es.uma.lcc.e_motions.transformations.model2model;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.IExtractor;
import org.eclipse.m2m.atl.core.IInjector;
import org.eclipse.m2m.atl.core.ModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFExtractor;
import org.eclipse.m2m.atl.core.emf.EMFInjector;
import org.eclipse.m2m.atl.core.emf.EMFModelFactory;

public abstract class ATLTransformation {
	
	
	protected ModelFactory mf;
	protected IInjector injector;
	protected IExtractor extractor;
	
	public ATLTransformation() {
		mf = new EMFModelFactory();
		injector = new EMFInjector();
		extractor = new EMFExtractor();
	}
	
	public abstract void execute() throws ATLCoreException, IOException, CoreException;
	
	protected String path(IFile file) {
		return file.getFullPath().toPortableString();
	}
}
