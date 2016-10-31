package es.uma.lcc.e_motions.jobs;

import java.io.IOException;
import java.net.URISyntaxException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.m2m.atl.core.ATLCoreException;

import es.uma.lcc.e_motions.codegeneration.Behavior2Maude;
import es.uma.lcc.e_motions.codegeneration.PalladioFlattenInputModel;
import es.uma.lcc.e_motions.common.PalladioFileManager;
import es.uma.lcc.e_motions.console.EmotionsConsole;
import es.uma.lcc.e_motions.maudeprocess.exceptions.MaudelingException;

/**
 * This class implements the flattening transformation for Palladio.
 * 
 * @author Antonio Moreno-Delgado <amoreno@lcc.uma.es>
 *
 */
public class PalladioJob extends Job {
	
	private EmotionsConsole console;
	private PalladioFileManager palladioFileManager;
	
	
	public PalladioJob(String name) {
		super(name);
		console = EmotionsConsole.getDefault();
		palladioFileManager = PalladioFileManager.getDefault();
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		new PalladioFlattenInputModel().run();
		
		/* Beh2Maude */
		try {
			Behavior2Maude.behOcl2Maude(_fm.getBehaviorString(),
					_fm.getMetamodelString());
		} catch (MaudelingException | URISyntaxException | CoreException
				| ATLCoreException | IOException e) {
			_p.println("[ERROR] Error in Behavior2Maude transformation process.\n"
					+ e.getMessage());
		}
		/* end BehOcl2Maude */
		
		return Status.OK_STATUS;
	}
	
}
