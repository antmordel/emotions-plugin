package es.uma.lcc.e_motions.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import es.uma.lcc.e_motions.common.PalladioLaunchConfiguration;
import es.uma.lcc.e_motions.console.EmotionsConsole;
import es.uma.lcc.e_motions.dialogs.PalladioDialog;
import es.uma.lcc.e_motions.jobs.PalladioJob;

public class PalladioHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Dialog palladioDialog;
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		PalladioLaunchConfiguration launch = new PalladioLaunchConfiguration();
		launch.read();
		
		palladioDialog = new PalladioDialog(window.getShell());
        int exitCode = palladioDialog.open();
        
        if (exitCode == Window.OK) {
        	EmotionsConsole.getDefault().setVisible().clean();
        	
        	/* save the new configuration */
        	launch = new PalladioLaunchConfiguration();
        	launch.save();
        	
        	Job palladioJob = new PalladioJob("Creating Palladio infrastructure");
        	palladioJob.setPriority(Job.SHORT);
        	palladioJob.schedule(); // start as soon as possible
        }
        
		return null;
	}

}
