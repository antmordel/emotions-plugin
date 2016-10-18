package es.uma.lcc.e_motions.common;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * @author Antonio Moreno-Delgado <amoreno@lcc.uma.es>
 *
 */
public class LaunchConfiguration {
	
	private PalladioRunningInformation info;
	private IProject project;
	private Properties props;
	
	private final String FILE_NAME = ".launchemotions";
	
	private final static String BEH_MODEL = "behaviorModel";
	private final static String METAMODEL = "metamodel";
	
	private final static String USAGE_MODEL = "usageModel";
	private final static String REPOSITORY_MODEL = "repositoryModel";
	private final static String SYSTEM_MODEL = "systemModel";
	private final static String ALLOCATION_MODEL = "allocationModel";
	private final static String RESENV_MODEL = "resenvModel";

	
	public LaunchConfiguration() {
		info = PalladioRunningInformation.getDefault();
		props = new Properties();
	}
	
	private boolean createProps() {
		
		props.setProperty(BEH_MODEL, info.getBehaviorModel().getFullPath().toOSString());
		props.setProperty(METAMODEL, info.getMetamodel().getFullPath().toOSString());
		props.setProperty(USAGE_MODEL, info.getUsageModel().getFullPath().toOSString());
		props.setProperty(REPOSITORY_MODEL, info.getRepositoryModel().getFullPath().toOSString());
		props.setProperty(SYSTEM_MODEL, info.getSystemModel().getFullPath().toOSString());
		props.setProperty(ALLOCATION_MODEL, info.getAllocationModel().getFullPath().toOSString());
		props.setProperty(RESENV_MODEL, info.getResenvModel().getFullPath().toOSString());
		
		return true;
	}
	
	public boolean save() {
		boolean res;
		
		res = createProps();
		
		project = info.getBehaviorModel().getProject();
		IFile file = project.getFile(FILE_NAME);
		try {
			FileOutputStream fos = new FileOutputStream(file.getRawLocation().makeAbsolute().toFile());
			props.store(fos, "Launch configuration for e-Motions");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	private IProject getCurrentProject(){    
        ISelectionService selectionService = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService();    

        ISelection selection = selectionService.getSelection();    

        IProject project = null;    
        if(selection instanceof IStructuredSelection) {    
            Object element = ((IStructuredSelection) selection).getFirstElement();    

            if (element instanceof IResource) {    
                project = ((IResource)element).getProject();    
            }  
        }     
        return project;    
    }

	public void read() {
		project = getCurrentProject();
		if (project != null) {
			IFile fileProperties = project.getFile(FILE_NAME);
			if (fileProperties.exists()) {
				try {
					props.load(new FileInputStream(fileProperties.getRawLocation().makeAbsolute().toFile()));
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (info.getBehaviorModel() == null) {
					IPath path = new Path(props.getProperty(BEH_MODEL));
					IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
					info.setBehaviorModel(file);
				}
				if (info.getMetamodel() == null) {
					IPath path = new Path(props.getProperty(METAMODEL));
					IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
					info.setMetamodel(file);
				}
				if (info.getUsageModel() == null) {
					IPath path = new Path(props.getProperty(USAGE_MODEL));
					IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
					info.setUsageModel(file);
				}
				if (info.getRepositoryModel() == null) {
					IPath path = new Path(props.getProperty(REPOSITORY_MODEL));
					IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
					info.setRepositoryModel(file);
				}
				if (info.getSystemModel() == null) {
					IPath path = new Path(props.getProperty(SYSTEM_MODEL));
					IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
					info.setSystemModel(file);
				}
				if (info.getAllocationModel() == null) {
					IPath path = new Path(props.getProperty(ALLOCATION_MODEL));
					IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
					info.setAllocationModel(file);
				}
				if (info.getResenvModel() == null) {
					IPath path = new Path(props.getProperty(RESENV_MODEL));
					IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
					info.setResenvModel(file);
				}
			}
		}
	}
}
