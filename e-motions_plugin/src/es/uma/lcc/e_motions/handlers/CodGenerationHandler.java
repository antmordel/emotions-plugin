package es.uma.lcc.e_motions.handlers;


import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import es.uma.lcc.e_motions.MaudeInfJob;
import es.uma.lcc.e_motions.common.Printer;
import es.uma.lcc.e_motions.dialogs.CodGenerationDialog;
import es.uma.lcc.e_motions.runningdata.CodGenerationFileManager;

public class CodGenerationHandler extends AbstractHandler {
	
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		CodGenerationDialog dialog;
		CodGenerationFileManager fm = new CodGenerationFileManager();
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		dialog = new CodGenerationDialog(window.getShell(), fm);
        
		int exitCode = dialog.open();
                     
		return null;
	}
}