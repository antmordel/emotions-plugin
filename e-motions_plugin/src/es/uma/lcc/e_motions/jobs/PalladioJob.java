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
import es.uma.lcc.e_motions.codegeneration.EcoreMM2Maude;
import es.uma.lcc.e_motions.codegeneration.Model2Maude;
import es.uma.lcc.e_motions.codegeneration.PalladioFlattenInputModel;
import es.uma.lcc.e_motions.common.PalladioFileManager;
import es.uma.lcc.e_motions.common.PalladioRunningInformation;
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
	private PalladioRunningInformation info;
	
	
	public PalladioJob(String name) {
		super(name);
		console = EmotionsConsole.getDefault();
		info = PalladioRunningInformation.getDefault();
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		new PalladioFlattenInputModel().run();
		
		/* Beh2Maude */
		try {
			Behavior2Maude.behOcl2Maude("file:///" + info.getBehaviorModel().getLocation().toOSString(),
					"file:///" + info.getMetamodel().getLocation().toOSString());
			console.println("Behavior to Maude trasnformation finished.");
		} catch (MaudelingException | URISyntaxException | CoreException
				| ATLCoreException | IOException e) {
			console.println("[ERROR] Error in Behavior2Maude transformation process.\n"
					+ e.getMessage());
		}
		/* end BehOcl2Maude */
		
		/* EcoreMM2Maude */
		try {
			EcoreMM2Maude.ecore2Maude("file:///" + info.getMetamodel().getLocation().toOSString());
			console.println("Ecore to Maude trasnformation finished.");
		} catch (ATLCoreException | IOException e1) {
			console.println("[ERROR] Error in EcoreMM2Maude transformation process.\n" + e1.getMessage());
		}
		/* end EcoreMM2Maude */
		
		/* InitialModel2Maude */
		try {
			// "file:///" + fileManager.getFolderTmp().getLocation().toOSString() + "/outFlatten.xmi"
			String nameInitialModule = Model2Maude.model2Maude(
						PalladioFileManager.getDefault().getFolderTmp().getFile("outFlatten.xmi"),
						info.getMetamodel());
		} catch (Exception e) {
			console.println("[ERROR] Error in initial Model2Maude transformation process.\n"
						+ e.getMessage());
		}
		/* end InitialModel2Maude */
		
		return Status.OK_STATUS;
	}
	
}
