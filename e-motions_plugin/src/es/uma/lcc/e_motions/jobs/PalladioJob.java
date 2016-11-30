package es.uma.lcc.e_motions.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import es.uma.lcc.e_motions.runningdata.CodGenerationFileManager;
import es.uma.lcc.e_motions.runningdata.PalladioFileManager;

public class PalladioJob extends EmotionsJob {

	public PalladioJob(String name, PalladioFileManager fm) {
		super(name, fm);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		CodGenerationFileManager fm = (CodGenerationFileManager) super.fm;
		monitor.beginTask("e-Motions code generation", 1);

		monitor.done();
		return Status.OK_STATUS;
	}

}
