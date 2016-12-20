package es.uma.lcc.e_motions.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;

import es.uma.lcc.e_motions.console.EmotionsConsole;
import es.uma.lcc.e_motions.launchconfiguration.EmotionsLaunchConfiguration;
import es.uma.lcc.e_motions.runningdata.EmotionsFileManager;

public abstract class EmotionsHandler extends AbstractHandler {
	
	protected EmotionsFileManager fm;
	protected EmotionsLaunchConfiguration launch;
	
	
	protected void getConsole() {
		EmotionsConsole.getDefault().setVisible().clean();
	}
	
	public static IProject getSelectedProject(){    
        ISelectionService selectionService = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService();    

        ISelection selection = selectionService.getSelection();    

        IProject project = null;    
        if(selection instanceof IStructuredSelection) {    
            Object element = ((IStructuredSelection) selection).getFirstElement();    

            if (element instanceof IResource) {    
                project = ((IResource) element).getProject();    
            }  
        }
        return project;    
    }
}
