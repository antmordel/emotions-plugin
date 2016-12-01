package es.uma.lcc.e_motions.jobs;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import es.uma.lcc.e_motions.runningdata.PalladioFileManager;
import es.uma.lcc.e_motions.transformations.model2model.PalladioFlattenInputModel;

public class PalladioJob extends EmotionsJob {

	public PalladioJob(String name, PalladioFileManager fm) {
		super(name, fm);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		PalladioFileManager fm = (PalladioFileManager) super.fm;
		monitor.beginTask("Palladio code generation", 1);

		/* Flattening Palladio models */
		monitor.subTask("Flattening input models");
		monitor.worked(1);
		init = System.currentTimeMillis();
		PalladioFlattenInputModel palladioFlatten;
		try {
			/* Firstly, we create a init model */
			fm.createInitModel();
			palladioFlatten = new PalladioFlattenInputModel(fm.getUsageModel(),
					fm.getRepositoryModel(), fm.getSystemModel(), fm.getAllocationModel(), fm.getResenvModel(),
					fm.getNewUsageModel(), fm.getNewRepositoryModel(), fm.getNewSystemModel(), fm.getNewAllocationModel(), fm.getNewResenvModel(),
					fm.getPalladioResourceTypes(), fm.getPalladioPrimitiveTypes(), fm.getInitModel());
			palladioFlatten.execute();
		} catch (CoreException | IOException e) {
			console.errorln("Flatten input model failed. " + e.getMessage());
		}
		console.println("Flatten Palladio input model in " + df.format((System.currentTimeMillis() - init) / 1000.0)
				+ " seconds.");
		/* end Flattening Palladio models */
		
		/* Ecore 2 Maude */
		init = System.currentTimeMillis();
		runEcore2MaudeM2M(monitor);
		runEcoreMaude2Code(monitor);
		console.successln("Ecore 2 Maude transformation generated in "
				+ df.format((System.currentTimeMillis() - init) / 1000.0) + " seconds.");
		
		/* Behavior 2 Maude */
		init = System.currentTimeMillis();
		runBehavior2MaudeM2M(monitor);
		runBehavior2MaudeCode(monitor);
		console.successln("Behavior 2 Maude transformation generated in "
				+ df.format((System.currentTimeMillis() - init) / 1000.0) + " seconds.");
		
		/* Infraestructure */
		monitor.subTask("Copying Maude infrastructure");
		monitor.worked(1);
		init = System.currentTimeMillis();
		copyInfrastuctureFiles();
		console.successln("Maude infraestructure copied in "
				+ df.format((System.currentTimeMillis() - init) / 1000.0) + " seconds.");

		monitor.done();
		return Status.OK_STATUS;
	}

}
