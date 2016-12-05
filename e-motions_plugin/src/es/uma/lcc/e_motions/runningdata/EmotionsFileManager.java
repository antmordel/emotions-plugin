package es.uma.lcc.e_motions.runningdata;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.NoSuchFileException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import es.uma.lcc.e_motions.Activator;

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
	
	/* Stores the Behavior Maude in XMI (conforming to Maude) */
	private IFile behaviorMaudeXMI;
	
	/* Stores the Behavior Maude code resulting of the Behavior2Maude transformation. */
	private IFile behaviorMaudeCode;
	
	/* Stores the TickRule module */
	private IFile runMaudeCode;
	
	/* Stores the input models */
	protected IFile initModel;
	protected IFile initModelXmi;
	protected IFile initModelCode;
	
	/* Stores the aux parameters file used in for the model2maude (init model) transformation */
	protected IFile paramsAuxFile;
	
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
			if (!folderTmp.exists()) {
				try {
					folderTmp.create(true, true, null);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
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
	
	public IFile getBehaviorMaudeXMI() throws NoSuchFileException {
		if (behaviorMaudeXMI == null) {
			if (getBehaviorModel() == null) {
				throw new NoSuchFileException("Behavior cannot be found");
			}
			behaviorMaudeXMI = getFolderTmp().getFile(getBehaviorModel().getName() + ".xmi");
		}
		return behaviorMaudeXMI;
	}
	
	public IFile getBehaviorMaudeCode() throws CoreException {
		if (behaviorMaudeCode == null) {
			behaviorMaudeCode = getFolderResult().getFile(getBehaviorModel().getName() + ".maude");
			if (!behaviorMaudeCode.exists()) {
				byte[] bytes = "".getBytes();
			    InputStream source = new ByteArrayInputStream(bytes);
			    behaviorMaudeCode.create(source, IResource.NONE, null);
			}
		}
		return behaviorMaudeCode;
	}
	
	public IProject getCurrentProject() {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(this.getBehaviorModel().getProject().getName());
	}
	
	public void copyFile(String file) throws IOException, CoreException {
		InputStream inp = Activator.getDefault().getBundle().getEntry("lib/resources/maude/" + file).openStream();
		String fileContents = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(inp));
		String line = br.readLine();
		while (line != null) {
			fileContents = fileContents + line + "\n";
			line = br.readLine();
		}
		br.close();
		
		IFile result = getFolderResult().getFile(file);
		if(!result.exists()){
			result.create(new ByteArrayInputStream(fileContents.getBytes()), true, null);
		} else {
			result.setContents(new ByteArrayInputStream(fileContents.getBytes()), true, true, null);
		}
	}
	
	public IFile getRunMaudeCode() throws CoreException {
		if (runMaudeCode == null) {
			runMaudeCode = getFolderResult().getFile("run.maude");
		}
		return runMaudeCode;
	}
	
	public IFile getInitModel() {
		return initModel;
	}

	public void setInitModel(IFile initModel) {
		this.initModel = initModel;
	}
	
	public IFile getInitModelXmi() {
		if (initModelXmi == null) {
			String name = getInitModel().getFileExtension().equals("xmi")?getInitModel().getName():getInitModel().getName() + ".xmi";
			initModelXmi = getFolderTmp().getFile(name);
		}
		return initModelXmi;
	}

	public IFile getInitModelCode() throws NoSuchFileException, CoreException {
		if (initModelCode == null) {
			if (initModel == null) {
				initModelCode = getFolderResult().getFile("EmptyModel.maude");
			} else {
				initModelCode = getFolderResult().getFile(getInitModel().getName() + ".maude");
				if (!initModelCode.exists()) {
					byte[] bytes = "".getBytes();
				    InputStream source = new ByteArrayInputStream(bytes);
				    initModelCode.create(source, IResource.NONE, null);
				}
			}
		}
		return initModelCode;
	}
	
	public IFile getParamsAuxFile() throws CoreException {
		if (paramsAuxFile == null) {
			paramsAuxFile = getFolderTmp().getFile("paramsATL.xmi");
		}
		return paramsAuxFile;
	}
	
	@Override
	public String toString() {
		return "FileManager [behaviorModel=" + behaviorModel + ", metamodel=" + metamodel + ", limitTime=" + limitTime
				+ ", infiniteLimitTime=" + infiniteLimitTime + ", appliedRules=" + appliedRules + ", showAdvisories="
				+ showAdvisories + ", folderResult=" + folderResult + "]";
	}
	
}
