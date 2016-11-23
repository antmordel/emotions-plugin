package es.uma.lcc.e_motions.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;

import es.uma.lcc.e_motions.runningdata.EmotionsFileManager;

public class CodGenerationJob extends EmotionsJob {

	public CodGenerationJob(String name) {
		super(name);
	}
	
	public CodGenerationJob(String name, EmotionsFileManager fm) {
		super(name, fm);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("e-Motions code generation", 1);
		
		runEcore2MaudeM2M(monitor);
		
		
		monitor.done();
		return null;
	}

}
