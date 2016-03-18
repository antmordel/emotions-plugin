package es.uma.lcc.e_motions.handlers;


import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import es.uma.lcc.e_motions.MaudeInfJob;
import es.uma.lcc.e_motions.common.Printer;
import es.uma.lcc.e_motions.dialogs.MaudeCodeDialog;

public class CodGenerationHandler extends AbstractHandler {
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		Dialog maudeCodeDialog;
		
		// open the dialog
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
        maudeCodeDialog = new MaudeCodeDialog(window.getShell());
        int exitCodeEMotionsDialog = maudeCodeDialog.open();
        
        // cleaning the output
     	Printer.clean();
     	if(exitCodeEMotionsDialog == Window.OK){
     			Job job = new MaudeInfJob("Creating Maude Infrastructure");
     			job.setPriority(Job.SHORT);
     			job.schedule(); // start as soon as possible
     		}
        
		return null;
	}
}