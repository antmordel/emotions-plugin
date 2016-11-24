package es.uma.lcc.e_motions.runningdata;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.NoSuchFileException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

/**
 * 
 * @author Antonio Moreno-Delgado <amoreno@lcc.uma.es>
 *
 */
public abstract class EmotionsFileManager {
	
	/**
	 * Input data 
	 */
	protected IFile behaviorModel;
	protected IFile metamodel;
	
	private int limitTime;
	private boolean infiniteLimitTime;

	private boolean appliedRules;
	private boolean showAdvisories;
	
	private IFolder folderResult;
	
	/**
	 *  Generated data 
	 */
	private IFolder folderTmp;
	
	/* Stores the Maude model resulting of the EcoreMM2Maude transformation. */
	private IFile metamodelMaudeXMI;
	
	/* Stores the Maude code resulting of the EcoreMM2Maude transformation. */
	private IFile metamodelMaudeCode;
	
	/* Stores the OCL parser in ATL code */
	private IFile oclParserCode;
	
	/* Stores the OCL parser in XMI (conforming to ATL) */
	private IFile oclParserXMI;
	
	public IFile getBehaviorModel() {
		return behaviorModel;
	}
	public void setBehaviorModel(IFile behaviorModel) {
		this.behaviorModel = behaviorModel;
	}
	public IFile getMetamodel() {
		return metamodel;
	}
	public void setMetamodel(IFile metamodel) {
		this.metamodel = metamodel;
	}
	
	public int getLimitTime() {
		return limitTime;
	}

	public void setLimitTime(int limitTime) {
		this.limitTime = limitTime;
	}

	public boolean isInfiniteLimitTime() {
		return infiniteLimitTime;
	}
	public void setInfiniteLimitTime(boolean infiniteLimitTime) {
		this.infiniteLimitTime = infiniteLimitTime;
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
	public IFolder getFolderResult() {
		return folderResult;
	}
	public void setFolderResult(IFolder folderResult) {
		this.folderResult = folderResult;
	}
	
	public IFolder getFolderTmp(){
		if (folderTmp == null) {
			IProject currentProject = ResourcesPlugin.getWorkspace().getRoot().getProject(this.getBehaviorModel().getProject().getName());
			folderTmp = currentProject.getFolder(".tmp");
		}
		return folderTmp;
	}
	
	public IFile getMetamodelMaudeXMI() throws NoSuchFileException {
		if (metamodelMaudeXMI == null) {
			if (getMetamodel() == null) {
				throw new NoSuchFileException("Metamodel cannot be found");
			}
			metamodelMaudeXMI = getFolderTmp().getFile(getMetamodel().getName() + ".xmi");
		}
		return metamodelMaudeXMI;
	}
	
	public IFile getMetamodelMaudeCode() throws CoreException {
		if (metamodelMaudeCode == null) {
			metamodelMaudeCode = getFolderResult().getFile(getMetamodel().getName() + ".maude");
			if (!metamodelMaudeCode.exists()) {
				byte[] bytes = "".getBytes();
			    InputStream source = new ByteArrayInputStream(bytes);
			    metamodelMaudeCode.create(source, IResource.NONE, null);
			}
		}
		return metamodelMaudeCode;
	}
	
	public IFile getOclParserCode() throws CoreException {
		if (oclParserCode == null) {
			oclParserCode = getFolderTmp().getFile("OCLParser.atl");
			if (!oclParserCode.exists()) {
				byte[] bytes = "".getBytes();
			    InputStream source = new ByteArrayInputStream(bytes);
			    oclParserCode.create(source, IResource.NONE, null);
			}
		}
		return oclParserCode;
	}
	
	public IFile getOclParserXMI() throws CoreException {
		if (oclParserXMI == null) {
			oclParserXMI = getFolderTmp().getFile("OCLParser.xmi");
			if (!oclParserXMI.exists()) {
				byte[] bytes = "".getBytes();
			    InputStream source = new ByteArrayInputStream(bytes);
			    oclParserXMI.create(source, IResource.NONE, null);
			}
		}
		return oclParserXMI;
	}
	
	@Override
	public String toString() {
		return "FileManager [behaviorModel=" + behaviorModel + ", metamodel=" + metamodel + ", limitTime=" + limitTime
				+ ", infiniteLimitTime=" + infiniteLimitTime + ", appliedRules=" + appliedRules + ", showAdvisories="
				+ showAdvisories + ", folderResult=" + folderResult + "]";
	}
	
	
	
}
