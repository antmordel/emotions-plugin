package es.uma.lcc.e_motions.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import es.uma.lcc.e_motions.console.EmotionsConsole;
import es.uma.lcc.e_motions.dialogs.PalladioDialog;
import es.uma.lcc.e_motions.jobs.PalladioJob;
import es.uma.lcc.e_motions.launchconfiguration.PalladioLaunchConfiguration;
import es.uma.lcc.e_motions.runningdata.PalladioFileManager;

public class PalladioHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		PalladioDialog dialog;
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		PalladioFileManager fm = new PalladioFileManager();
//		PalladioLaunchConfiguration launch = new PalladioLaunchConfiguration();
//		launch.read();
//		
		
		
		dialog = new PalladioDialog(window.getShell(), fm);
        int exitCode = dialog.open();
//        
//        if (exitCode == Window.OK) {
//        	EmotionsConsole.getDefault().setVisible().clean();
//        	
//        	/* save the new configuration */
//        	launch = new PalladioLaunchConfiguration();
//        	launch.save();
//        	
//        	Job palladioJob = new PalladioJob("Creating Palladio infrastructure");
//        	palladioJob.setPriority(Job.SHORT);
//        	palladioJob.schedule(); // start as soon as possible
//        }
        
		return null;
	}

}
