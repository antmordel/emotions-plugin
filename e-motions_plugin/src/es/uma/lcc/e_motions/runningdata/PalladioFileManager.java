package es.uma.lcc.e_motions.runningdata;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.NoSuchFileException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

public class PalladioFileManager extends EmotionsFileManager {
	
	private IFile usageModel;
	private IFile repositoryModel;
	private IFile systemModel;
	private IFile allocationModel;
	private IFile resenvModel;
	
	private IFile newUsageModel;
	private IFile newRepositoryModel;
	private IFile newSystemModel;
	private IFile newAllocationModel;
	private IFile newResenvModel;
	
	private IFile palladioResourceTypes;
	private IFile palladioPrimitiveTypes;
	
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
	public IFile getNewUsageModel() throws NoSuchFileException {
		if (newUsageModel == null) {
			if (getUsageModel() == null) {
				throw new NoSuchFileException("UsageModel cannot be found");
			}
			newUsageModel = getFolderTmp().getFile(getUsageModel().getName());
		}
		return newUsageModel;
	}
	public void setNewUsageModel(IFile newUsageModel) {
		this.newUsageModel = newUsageModel;
	}
	public IFile getNewRepositoryModel() throws NoSuchFileException {
		if (newRepositoryModel == null) {
			if (getRepositoryModel() == null) {
				throw new NoSuchFileException("Repository cannot be found");
			}
			newRepositoryModel = getFolderTmp().getFile(getRepositoryModel().getName());
		}
		return newRepositoryModel;
	}
	public void setNewRepositoryModel(IFile newRepositoryModel) {
		this.newRepositoryModel = newRepositoryModel;
	}
	public IFile getNewSystemModel() throws NoSuchFileException {
		if (newSystemModel == null) {
			if (getSystemModel() == null) {
				throw new NoSuchFileException("SystemModel cannot be found");
			}
			newSystemModel = getFolderTmp().getFile(getSystemModel().getName());
		}
		return newSystemModel;
	}
	public void setNewSystemModel(IFile newSystemModel) {
		this.newSystemModel = newSystemModel;
	}
	public IFile getNewAllocationModel() throws NoSuchFileException {
		if (newAllocationModel == null) {
			if (getAllocationModel() == null) {
				throw new NoSuchFileException("AllocationModel cannot be found");
			}
			newAllocationModel = getFolderTmp().getFile(getAllocationModel().getName());
		}
		return newAllocationModel;
	}
	public void setNewAllocationModel(IFile newAllocationModel) {
		this.newAllocationModel = newAllocationModel;
	}
	public IFile getNewResenvModel() throws NoSuchFileException {
		if (newResenvModel == null) {
			if (getResenvModel() == null) {
				throw new NoSuchFileException("ResourceEnvironmentModel cannot be found");
			}
			newResenvModel = getFolderTmp().getFile(getResenvModel().getName());
		}
		return newResenvModel;
	}
	public void setNewResenvModel(IFile newResenvModel) {
		this.newResenvModel = newResenvModel;
	}
	public IFile getPalladioResourceTypes() throws NoSuchFileException {
		if (palladioResourceTypes == null) {
			palladioResourceTypes = getFolderTmp().getFile("Palladio.resourcetype");
		}
		return palladioResourceTypes;
	}
	public IFile getPalladioPrimitiveTypes() throws NoSuchFileException {
		if (palladioPrimitiveTypes == null) {
			palladioPrimitiveTypes = getFolderTmp().getFile("PrimitiveTypes.repository");
		}
		return palladioPrimitiveTypes;
	}
	public void createInitModel() throws CoreException {
		if (initModel == null) {
			initModel = getFolderTmp().getFile("palladioFlattenInitModel.xmi");
			if (!initModel.exists()) {
				byte[] bytes = "".getBytes();
			    InputStream source = new ByteArrayInputStream(bytes);
			    initModel.create(source, IResource.NONE, null);
			}
		}
	}
}
