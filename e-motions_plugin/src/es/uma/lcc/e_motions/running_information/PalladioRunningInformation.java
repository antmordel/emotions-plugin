package es.uma.lcc.e_motions.running_information;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import es.uma.lcc.e_motions.common.FileManager;
import es.uma.lcc.e_motions.exceptions.FileNotSet;
import es.uma.lcc.e_motions.resources.EmotionsResources;

/**
 * Stores running information of the actual context.
 * 
 * @author Antonio Moreno-Delgado <code>amoreno@lcc.uma.es</code>
 *
 */
public class PalladioRunningInformation {

	public static final String BEHAVIOR_TMP_FILE = "outMaudeBehavior.xmi";
	public static final String FLATTEN_MODEL = "outFlattenModel.xmi";
	public static final String INITIAL_MODEL_MAUDE_FILENAME = "initModelInMaude.xmi";
	public static final String INITIAL_MODEL_NAME = "flattenPalladio.xmi";
	public static final String PARAMS_MODEL_FILENAME = "params.xmi";

	private static PalladioRunningInformation self;

	private IFile behaviorModel;
	private IFile metamodel;

	private IFile usageModel;
	private IFile repositoryModel;
	private IFile systemModel;
	private IFile allocationModel;
	private IFile resenvModel;

	/* New files: since they have to be modified */
	private IFile newUsageModel;
	private IFile newRepositoryModel;
	private IFile newSystemModel;
	private IFile newAllocationModel;
	private IFile newResenvModel;

	private IFile newPalladioRepository;
	private IFile newPalladioResourceType;
	/* end new files */

	private int limitTime;

	private boolean appliedRules;
	private boolean showAdvisories;

	private String folderOutputString;

	private IFolder folderTmp;

	private IFolder folderOutput;

	/*
	 * Output files
	 */

	/* Stores the Maude model resulting of the Behavior2Maude transformation. */
	private IPath behaviorMaude;
	private String behaviorCodeName;
	/* Stores the Maude model resulting of the EcoreMM2Maude transformation. */
	private IPath metamodelMaude;

	private IFile metamodelMaudeCode;
	private IFile initialModelCode;

	private IPath initialModelMaude;
	private IFile behaviorCode;

	private PalladioRunningInformation() {
		folderOutputString = "maude";
		limitTime = -1;
	}

	public static PalladioRunningInformation getDefault() {
		return self == null ? self = new PalladioRunningInformation() : self;
	}

	public IFolder getFolderTmp() {
		IProject currentProject = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(getBehaviorModel().getProject().getName());
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

	private void copyIFileRemovePathmaps(IFile source, IFile target) throws CoreException, IOException {
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
		newUsageModel = getFolderTmp().getFile(getUsageModel().getName());
		copyIFileRemovePathmaps(getUsageModel(), newUsageModel);
		// Repository model
		newRepositoryModel = getFolderTmp().getFile(getRepositoryModel().getName());
		copyIFileRemovePathmaps(getRepositoryModel(), newRepositoryModel);
		// System model
		newSystemModel = getFolderTmp().getFile(getSystemModel().getName());
		copyIFileRemovePathmaps(getSystemModel(), newSystemModel);
		// Allocation model
		newAllocationModel = getFolderTmp().getFile(getAllocationModel().getName());
		copyIFileRemovePathmaps(getAllocationModel(), newAllocationModel);
		// ResourceEnvironment model
		newResenvModel = getFolderTmp().getFile(getResenvModel().getName());
		copyIFileRemovePathmaps(getResenvModel(), newResenvModel);
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
			newPalladioRepository.setContents(new ByteArrayInputStream(fileContentsRepository.getBytes()), true, true,
					null);
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
			newPalladioResourceType.setContents(new ByteArrayInputStream(fileContentsResourceType.getBytes()), true,
					true, null);
		} else {
			newPalladioResourceType.create(new ByteArrayInputStream(fileContentsResourceType.getBytes()), true, null);
		}
	}

	public IFile getBehaviorModel() {
		return behaviorModel;
	}

	public void setBehaviorModel(IFile behaviorModel) {
		this.behaviorModel = behaviorModel;
		behaviorCodeName = behaviorModel.getName() + ".maude";
	}

	public IFile getMetamodel() {
		return metamodel;
	}

	public void setMetamodel(IFile metamodel) {
		this.metamodel = metamodel;
	}

	public IFile getUsageModel() {
		return usageModel;
	}

	public void setUsageModel(IFile usageModel) {
		this.usageModel = usageModel;
	}

	public IFile getRepositoryModel() {
		return repositoryModel;
	}

	public void setRepositoryModel(IFile repositoryModel) {
		this.repositoryModel = repositoryModel;
	}

	public IFile getSystemModel() {
		return systemModel;
	}

	public void setSystemModel(IFile systemModel) {
		this.systemModel = systemModel;
	}

	public IFile getAllocationModel() {
		return allocationModel;
	}

	public void setAllocationModel(IFile allocationModel) {
		this.allocationModel = allocationModel;
	}

	public IFile getResenvModel() {
		return resenvModel;
	}

	public void setResenvModel(IFile resenvModel) {
		this.resenvModel = resenvModel;
	}

	public int getLimitTime() {
		return limitTime;
	}

	public void setLimitTime(int limitTime) {
		this.limitTime = limitTime;
	}

	public boolean isAppliedRules() {
		return appliedRules;
	}

	public void setAppliedRules(boolean appliedRules) {
		this.appliedRules = appliedRules;
	}

	public boolean isShowAdvisories() {
		return showAdvisories;
	}

	public void setShowAdvisories(boolean showAdvisories) {
		this.showAdvisories = showAdvisories;
	}

	public String getFolderOutputString() {
		return folderOutputString;
	}

	public void setFolderOutputString(String folderOutputString) {
		this.folderOutputString = folderOutputString;
	}

	public IFile getNewUsageModel() {
		return newUsageModel;
	}

	public IFile getNewRepositoryModel() {
		return newRepositoryModel;
	}

	public IFile getNewSystemModel() {
		return newSystemModel;
	}

	public IFile getNewAllocationModel() {
		return newAllocationModel;
	}

	public IFile getNewResenvModel() {
		return newResenvModel;
	}

	public IFile getNewPalladioRepository() {
		return newPalladioRepository;
	}

	public IFile getNewPalladioResourceType() {
		return newPalladioResourceType;
	}

	public void setBehaviorMaude(IPath behaviorMaude) {
		this.behaviorMaude = behaviorMaude;
	}

	public IPath getBehaviorMaude() {
		return behaviorMaude;
	}

	public IFolder getFolderOutput() throws CoreException {
		if (folderOutput == null) {
			if (folderOutputString == null) {
				throw new FileNotSet("Output folder has not been set.");
			}
			if (getBehaviorModel() == null) {
				throw new FileNotSet("Behavior model has not been set.");
			}
			IProject currentProject = ResourcesPlugin.getWorkspace().getRoot()
					.getProject(this.getBehaviorModel().getProject().getName());
			folderOutput = currentProject.getFolder(folderOutputString);
			if (!folderOutput.exists()) {
				folderOutput.create(true, true, null);
			}
		}
		return folderOutput;
	}

	public IFile createMetamodelCodeInMaudeFolder() throws CoreException {
		return metamodelMaudeCode = this.getFolderOutput().getFile(metamodel.getName() + ".maude");
	}

	public IFile createBehaviorCodeInFolder() throws CoreException {
		if (behaviorCodeName == null) {
			throw new FileNotSet("Behavior Model has not been set yet. Behavior code cannot be set.");
		}
		return behaviorCode = getFolderOutput().getFile(behaviorCodeName);
	}

	public IFile createInitialModelCodeInMaudeFolder() throws CoreException {
		return initialModelCode = this.getFolderOutput().getFile(INITIAL_MODEL_NAME + ".maude");
	}

	public IPath getMetamodelMaude() {
		return metamodelMaude;
	}

	public void setMetamodelMaude(IPath metamodelMaude) {
		this.metamodelMaude = metamodelMaude;
	}

	public IPath getInitialModelMaude() {
		return initialModelMaude;
	}

	public void setInitialModelMaude(IPath initialModelMaude) {
		this.initialModelMaude = initialModelMaude;
	}

	public IFile createParamsModelFile(String content) throws CoreException {
		IFile modelFile = getFolderTmp().getFile(PARAMS_MODEL_FILENAME);
		if (!modelFile.exists()) {
			modelFile.create(new ByteArrayInputStream(content.getBytes()), true, null);
		} else {
			modelFile.setContents(new ByteArrayInputStream(content.getBytes()), true, true, null);
		}
		return modelFile;
	}

	public IFile createFileInMaudeFolder(String name) throws CoreException {
		return this.getFolderOutput().getFile(name);
	}

	public void copyFile(IFile aux, URL resource) throws CoreException, IOException {
		if (!aux.exists()) {
			aux.create(new ByteArrayInputStream(FileManager.readFile(resource).getBytes()), true, null);
		} else {
			aux.setContents(new ByteArrayInputStream(FileManager.readFile(resource).getBytes()), true, true, null);
		}
	}
	
	public String extractBehaviorModuleName() throws IOException {
		BufferedReader br = null;
		try {
			behaviorCode.refreshLocal(IResource.DEPTH_INFINITE, null);
			br = new BufferedReader(new InputStreamReader(behaviorCode.getContents()));
		} catch (CoreException e) {
			e.printStackTrace();
		}
		String line = br.readLine();
		while (!line.startsWith("mod ")) {
			line = br.readLine();
		}

		String acum = "";
		if (line.startsWith("mod ")) {
			line = line.replaceFirst("mod ", "");
			int i = 0;
			while (line.charAt(i) != ' ' && line.charAt(i) != '\t') {
				acum = acum + line.charAt(i);
				i++;
			}
		}
		return acum;
	}
}
