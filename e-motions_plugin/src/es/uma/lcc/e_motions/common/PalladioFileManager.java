package es.uma.lcc.e_motions.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import es.uma.lcc.e_motions.resources.EmotionsResources;

/**
 * This class handles files information as the temporal folder and the output
 * files.
 * 
 * @author Antonio Moreno-Delgado <amoreno@lcc.uma.es>
 *
 */
public class PalladioFileManager {

	private static PalladioFileManager self;

	private PalladioRunningInformation info;
	private IFolder folderTmp;

	private IFile newUsageModel;
	private IFile newRepositoryModel;
	private IFile newSystemModel;
	private IFile newAllocationModel;
	private IFile newResenvModel;
	
	private IFile newPalladioRepository;
	private IFile newPalladioResourceType;

	private PalladioFileManager() {
		info = PalladioRunningInformation.getDefault();
	}

	public static PalladioFileManager getDefault() {
		return self == null ? self = new PalladioFileManager() : self;
	}

	public IFolder getFolderTmp() {
		IProject currentProject = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(info.getBehaviorModel().getProject().getName());
		folderTmp = currentProject.getFolder(".tmp");
		if (!folderTmp.exists()) {
			try {
				folderTmp.create(true, true, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return folderTmp;
	}

	public void copyIFileRemovePathmaps(IFile source, IFile target) throws CoreException, IOException {
		// Reading file
		InputStream inp = source.getContents();
		String fileContents = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(inp));
		String line = br.readLine();
		while (line != null) {
			fileContents = fileContents + line + "\n";
			line = br.readLine();
		}
		br.close();

		// Changing pathmaps
		fileContents = fileContents.replaceAll("pathmap://PCM_MODELS/", "");

		// Saving output
		if (target.exists()) {
			target.setContents(new ByteArrayInputStream(fileContents.getBytes()), true, true, null);
		} else {
			target.create(new ByteArrayInputStream(fileContents.getBytes()), true, null);
		}
	}

	public void copyIFilesRemovePathmaps() throws CoreException, IOException {
		// UsageModel model
		newUsageModel = getFolderTmp().getFile(info.getUsageModel().getName());
		copyIFileRemovePathmaps(info.getUsageModel(), newUsageModel);
		// Repository model
		newRepositoryModel = getFolderTmp().getFile(info.getRepositoryModel().getName());
		copyIFileRemovePathmaps(info.getRepositoryModel(), newRepositoryModel);
		// System model
		newSystemModel = getFolderTmp().getFile(info.getSystemModel().getName());
		copyIFileRemovePathmaps(info.getSystemModel(), newSystemModel);
		// Allocation model
		newAllocationModel = getFolderTmp().getFile(info.getAllocationModel().getName());
		copyIFileRemovePathmaps(info.getAllocationModel(), newAllocationModel);
		// ResourceEnvironment model
		newResenvModel = getFolderTmp().getFile(info.getResenvModel().getName());
		copyIFileRemovePathmaps(info.getResenvModel(), newResenvModel);
	}
	
	public void copyFixedFiles() throws IOException, CoreException {
		newPalladioRepository = getFolderTmp().getFile("PrimitiveTypes.repository");
		InputStream inp = EmotionsResources.class.getResource("PrimitiveTypes.repository").openStream();
		String fileContentsRepository = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(inp));
		String line = br.readLine();
		while (line != null) {
			fileContentsRepository = fileContentsRepository + line + "\n";
			line = br.readLine();
		}
		br.close();
		if (newPalladioRepository.exists()) {
			newPalladioRepository.setContents(new ByteArrayInputStream(fileContentsRepository.getBytes()), true, true, null);
		} else {
			newPalladioRepository.create(new ByteArrayInputStream(fileContentsRepository.getBytes()), true, null);
		}
		
		newPalladioResourceType = getFolderTmp().getFile("Palladio.resourcetype");
		inp = EmotionsResources.class.getResource("Palladio.resourcetype").openStream();
		String fileContentsResourceType = "";
		br = new BufferedReader(new InputStreamReader(inp));
		line = br.readLine();
		while (line != null) {
			fileContentsResourceType = fileContentsResourceType + line + "\n";
			line = br.readLine();
		}
		br.close();
		if (newPalladioResourceType.exists()) {
			newPalladioResourceType.setContents(new ByteArrayInputStream(fileContentsResourceType.getBytes()), true, true, null);
		} else {
			newPalladioResourceType.create(new ByteArrayInputStream(fileContentsResourceType.getBytes()), true, null);
		}
	}

	public IFile getNewRepositoryModel() {
		return newRepositoryModel;
	}

	public IFile getNewResenvModel() {
		return newResenvModel;
	}

	public IFile getNewUsageModel() {
		return newUsageModel;
	}

	public IFile getNewSystemModel() {
		return newSystemModel;
	}

	public IFile getNewAllocationModel() {
		return newAllocationModel;
	}

	public IFile getNewPalladioRepository() {
		return newPalladioRepository;
	}

	public IFile getNewPalladioResourceType() {
		return newPalladioResourceType;
	}
}
