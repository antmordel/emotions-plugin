package es.uma.lcc.e_motions.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.m2m.atl.emftvm.EmftvmFactory;
import org.eclipse.m2m.atl.emftvm.ExecEnv;
import org.eclipse.m2m.atl.emftvm.Metamodel;

import es.uma.lcc.e_motions.console.EmotionsConsole;
import es.uma.lcc.e_motions.metamodels.Metamodels;

public class PalladioJob extends Job {
	
	private EmotionsConsole console;
	
	public PalladioJob(String name) {
		super(name);
		console = EmotionsConsole.getDefault();
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		flattenPalladioFiles();
		
		return Status.OK_STATUS;
	}
	
	private void flattenPalladioFiles() {
		/* 
		 * Creates the environment
		 */
		ExecEnv env = EmftvmFactory.eINSTANCE.createExecEnv();
		ResourceSet rs = new ResourceSetImpl();
		console.println("Flattening Palladio files\n-------------------------");
		
		/*
		 * Load metamodels PCM
		 */
		console.println((Metamodels.class.getResource("Maude.ecore").toString()));
		Metamodel MMunits = EmftvmFactory.eINSTANCE.createMetamodel();
		MMunits.setResource(rs.getResource(URI.createURI(Metamodels.class.getResource("Maude.ecore").toString()), true));
		console.println(MMunits.toString());
		env.registerMetaModel("Units", MMunits);
		
		Metamodel MMprobFunction = EmftvmFactory.eINSTANCE.createMetamodel();
		MMprobFunction.setResource(rs.getResource(URI.createURI(Metamodels.class.getResource("probabilityfunction.ecore").toString()), true));
		env.registerMetaModel("ProbabilityFunction", MMprobFunction);
		
		// TODO
		
		console.println(env.toString());
	}

}
