package es.uma.lcc.e_motions.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import es.uma.lcc.e_motions.runningdata.EmotionsFileManager;

public class CodGenerationJob extends EmotionsJob {

	public CodGenerationJob(String name, EmotionsFileManager fm) {
		super(name, fm);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("e-Motions code generation", 1);

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
		init = System.currentTimeMillis();
		copyInfrastuctureFiles();
		console.successln("Maude infraestructure copied in "
				+ df.format((System.currentTimeMillis() - init) / 1000.0) + " seconds.");
		
		monitor.done();
		return Status.OK_STATUS;
	}

}
