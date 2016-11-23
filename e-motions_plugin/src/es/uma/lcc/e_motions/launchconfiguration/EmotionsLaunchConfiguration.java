package es.uma.lcc.e_motions.launchconfiguration;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;

import es.uma.lcc.e_motions.runningdata.EmotionsFileManager;

/**
 * 
 * @author Antonio Moreno-Delgado <amoreno@lcc.uma.es>
 *
 */
public class EmotionsLaunchConfiguration {
	
	protected final String FILE_NAME = ".launchemotions";
	protected final static String BEH_MODEL = "behaviorModel";
	protected final static String METAMODEL = "metamodel";
	
	protected final static String PRINT_ADV = "printAdvisories";
	protected final static String PRINT_RULES = "printRules";
	protected final static String IS_INFINITE = "isInfinite";
	protected final static String TIME_LIMIT = "timeLimit";
	
	protected final static String OUTPUT_FOLDER = "ouputFolder";
	
	protected EmotionsFileManager fm;
	protected Properties props;
	
	public EmotionsLaunchConfiguration(EmotionsFileManager fm) {
		this.fm = fm;
		props = new Properties();
	}
	
	private boolean createProps() {
		props.setProperty(BEH_MODEL, fm.getBehaviorModel().getFullPath().toOSString());
		props.setProperty(METAMODEL, fm.getMetamodel().getFullPath().toOSString());
		
		props.setProperty(PRINT_ADV, Boolean.toString(fm.isShowAdvisories()));
		props.setProperty(PRINT_RULES, Boolean.toString(fm.isAppliedRules()));
		props.setProperty(IS_INFINITE, Boolean.toString(fm.isInfiniteLimitTime()));
		props.setProperty(TIME_LIMIT, Integer.toString(fm.getLimitTime()));
		
		props.setProperty(OUTPUT_FOLDER, fm.getFolderOutputPathString());
		return true;
	}
	
	private IProject getProject() {
		return fm.getBehaviorModel().getProject();
	}
	
	public boolean save() {
		boolean res;
		
		res = createProps();
		
		IFile file = getProject().getFile(FILE_NAME);
		
		try {
			FileOutputStream fos = new FileOutputStream(file.getRawLocation().makeAbsolute().toFile());
			props.store(fos, "Launch configuration for e-Motions");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	protected IProject getSelectedProject(){    
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
	
	public void read() {
		IProject project = getSelectedProject();
		IFile fileProperties = project!=null?project.getFile(FILE_NAME):null;
		if (project != null && fileProperties != null) {
			try {
				props.load(new FileInputStream(fileProperties.getRawLocation().makeAbsolute().toFile()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IPath path;
			IFile file;
			
			/* behavior model */
			path = new Path(props.getProperty(BEH_MODEL));
			file = root.getFile(path);
			fm.setBehaviorModel(file);
			
			/* metamodel */
			path = new Path(props.getProperty(METAMODEL));
			file = root.getFile(path);
			fm.setMetamodel(file);
			
			/* more options */
			fm.setAppliedRules(Boolean.parseBoolean(props.getProperty(PRINT_RULES)));
			fm.setShowAdvisories(Boolean.parseBoolean(props.getProperty(PRINT_ADV)));
			fm.setInfiniteLimitTime(Boolean.parseBoolean(props.getProperty(IS_INFINITE)));
			fm.setLimitTime(Integer.parseInt(props.getProperty(TIME_LIMIT)));
			
			/* output folder */
			fm.setFolderOutputPathString(props.getProperty(OUTPUT_FOLDER));
		}
	}
}
