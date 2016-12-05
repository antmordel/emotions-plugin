package es.uma.lcc.e_motions.transformations.model2model;

import java.io.IOException;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.IExtractor;
import org.eclipse.m2m.atl.core.IInjector;
import org.eclipse.m2m.atl.core.ModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFExtractor;
import org.eclipse.m2m.atl.core.emf.EMFInjector;
import org.eclipse.m2m.atl.core.emf.EMFModelFactory;

public abstract class EmotionsTransformation {
	
	
	protected ModelFactory mf;
	protected IInjector injector;
	protected IExtractor extractor;
	
	public EmotionsTransformation() {
		mf = new EMFModelFactory();
		injector = new EMFInjector();
		extractor = new EMFExtractor();
	}
	
	public abstract void execute() throws ATLCoreException, IOException, CoreException;
	
	protected String path(IFile file) {
		return file.getFullPath().toPortableString();
	}
	
	protected void registerMetamodel(ResourceSet rs, String path) {
		Resource Rpcm = rs.getResource(URI.createURI(path), true);
		Iterator<EObject> eo = Rpcm.getAllContents();
		while (eo.hasNext()) {
			EObject next = eo.next();
			if (next instanceof EPackage) {
				rs.getPackageRegistry().put(((EPackage) next).getNsURI(), ((EPackage) next));
			}
		}
	}
}
