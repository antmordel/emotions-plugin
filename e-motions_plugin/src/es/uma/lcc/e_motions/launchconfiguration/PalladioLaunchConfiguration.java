package es.uma.lcc.e_motions.launchconfiguration;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import es.uma.lcc.e_motions.runningdata.PalladioFileManager;

/**
 * 
 * @author Antonio Moreno-Delgado <amoreno@lcc.uma.es>
 *
 */
public class PalladioLaunchConfiguration extends EmotionsLaunchConfiguration {
	
	private final static String USAGE_MODEL = "usageModel";
	private final static String REPOSITORY_MODEL = "repositoryModel";
	private final static String SYSTEM_MODEL = "systemModel";
	private final static String ALLOCATION_MODEL = "allocationModel";
	private final static String RESENV_MODEL = "resenvModel";
	
	public PalladioLaunchConfiguration(PalladioFileManager fm) {
		super(fm);
		props = new Properties();
	}
	
	protected boolean createProps() {
		super.createProps();
		
		PalladioFileManager fm = (PalladioFileManager) this.fm;
		
		props.setProperty(USAGE_MODEL, fm.getUsageModel().getFullPath().toOSString());
		props.setProperty(REPOSITORY_MODEL, fm.getRepositoryModel().getFullPath().toOSString());
		props.setProperty(SYSTEM_MODEL, fm.getSystemModel().getFullPath().toOSString());
		props.setProperty(ALLOCATION_MODEL, fm.getAllocationModel().getFullPath().toOSString());
		props.setProperty(RESENV_MODEL, fm.getResenvModel().getFullPath().toOSString());
		
		return true;
	}
	
	public boolean save() {
		boolean res;
		
		res = createProps();
		
		IProject project = fm.getBehaviorModel().getProject();
		IFile file = project.getFile(FILE_NAME);
		try {
			FileOutputStream fos = new FileOutputStream(file.getRawLocation().makeAbsolute().toFile());
			props.store(fos, "Launch configuration for e-Motions");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	public void read() {
		super.read();
		PalladioFileManager fm = (PalladioFileManager) this.fm;
		IProject project = getSelectedProject();
		if (project != null) {
			IFile fileProperties = project.getFile(FILE_NAME);
			if (fileProperties.exists()) {
				try {
					props.load(new FileInputStream(fileProperties.getRawLocation().makeAbsolute().toFile()));
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (fm.getUsageModel() == null && props.getProperty(USAGE_MODEL) != null) {
					IPath path = new Path(props.getProperty(USAGE_MODEL));
					IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
					fm.setUsageModel(file);
				}
				if (fm.getRepositoryModel() == null && props.getProperty(REPOSITORY_MODEL) != null) {
					IPath path = new Path(props.getProperty(REPOSITORY_MODEL));
					IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
					fm.setRepositoryModel(file);
				}
				if (fm.getSystemModel() == null && props.getProperty(SYSTEM_MODEL) != null) {
					IPath path = new Path(props.getProperty(SYSTEM_MODEL));
					IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
					fm.setSystemModel(file);
				}
				if (fm.getAllocationModel() == null && props.getProperty(ALLOCATION_MODEL) != null) {
					IPath path = new Path(props.getProperty(ALLOCATION_MODEL));
					IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
					fm.setAllocationModel(file);
				}
				if (fm.getResenvModel() == null && props.getProperty(RESENV_MODEL) != null) {
					IPath path = new Path(props.getProperty(RESENV_MODEL));
					IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
					fm.setResenvModel(file);
				}
			}
		}
	}
	
	
}
