package es.uma.lcc.e_motions.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
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

/**
 * @author  Antonio Moreno.
 * 
 * @date July 2nd
 * 
 */
public class FileManager {
	
	public static String BEHAVIOR_TMP_FILE = "outMaudeBehavior.xmi",
			                 TMP_FOLDER = ".tmp",
			                 OCLBEHAVIOR_ATL = "oclBehavior.atl",
			                 OCLBEHAVIOR_XMI = "oclBehavior.xmi",
			                 BEHAVIOR_CODE,
			                 TICKMODULE_FILENAME = "TickModule.maude",
			                 PARAMS_MODEL_FILENAME = "params.xmi",
			                 INITIAL_MODEL_MAUDE_FILENAME = "initModelInMaude.xmi",
			                 EMPTY_MODEL = "EmptyModel.maude",
			                 AUX_ATL_INVERSE_TRANSF = "inverseATLTransformation.atl",
			                 AUX_ATL_INVERSE_TRANSF_ASM = "inverseATLTransformation.asm";
	
	private static FileManager self;
	
	private IFile oclParserModel;
	private IFile behavior;
	private IFile metamodel;
	private IFile initialModel;
	private IFile behaviorCode;
	private IFile initialModelCode;
	private IFile metamodelCode;
	private IFile tickModule;
	private IFile outputMaudeModel;
	private IFile auxATLInverseTransformation;
	private IFile auxATLInverseTransformationASM;


	private IPath result; // model resulting of e-Motions execution
	private String folderResultName = "maude";
	private IFolder folderResult; // folder containing the e-Motions infrastructure and the code generated
	private IFolder tmp;
	
	/* Stores the Maude model resulting of the Behavior2Maude transformation. */
	private IPath behaviorMaude;
	/* Stores the Maude model resulting of the EcoreMM2Maude transformation. */
	private IPath metamodelMaude;
	/*  Stores the initial Maude model resulting of the Model2Maude transformation. */
	private IPath initialModelMaude;
	
	
	private FileManager(){}
	
	public static FileManager getDefault(){
		if(self == null){
			self = new FileManager();
		}
		return self;
	}

	public void setOCLParserModel(IFile oclBehaviorATLModel) {
		this.oclParserModel = oclBehaviorATLModel;
	}
	
	public IFile getOCLParserModel() {
		return oclParserModel;
	}

	public IFile getBehavior() {
		return behavior;
	}

	public IFile createBehaviorCode() throws CoreException {
		String project = this.getBehavior().getProject().getName();
		tmp = this.createFolderTmp(project);
		behaviorCode = tmp.getFile(BEHAVIOR_CODE);
		return behaviorCode;
	}
	
	public IFile createInitialModelCode() throws CoreException {
		String project = this.getBehavior().getProject().getName();
		tmp = this.createFolderTmp(project);
		initialModelCode = tmp.getFile(initialModel.getName()+".maude");
		return initialModelCode;
	}
	
	public IFile createBehaviorCodeInMaudeFolder() throws CoreException {
		if (this.getFolderResult() != null) {
			behaviorCode = this.getFolderResult().getFile(BEHAVIOR_CODE);
		}
		return behaviorCode;
	}
	
	public IFile createFileInMaudeFolder(String name) throws CoreException {
		IFile res = null;
		if (this.getFolderResult() != null) {
			res = this.getFolderResult().getFile(name);
		}
		return res;
	}
	
	public IFile createFileInTmpFolder(String name) throws CoreException {
		IFile res = null;
		if (this.getFolderTmp() != null) {
			res = this.getFolderTmp().getFile(name);
		}
		return res;
	}
	
	public IFile createMetamodelCode() throws CoreException {
		String project = this.getBehavior().getProject().getName();
		tmp = this.createFolderTmp(project);
		metamodelCode = tmp.getFile(metamodel.getName()+".maude");
		return metamodelCode;
	}
	
	public IFile createOutputModelMaude() throws CoreException {
		String project = this.getBehavior().getProject().getName();
		tmp = this.createFolderTmp(project);
		outputMaudeModel = tmp.getFile("outputModelMaude.xmi");
		return outputMaudeModel;
	}
	
	public IFile createMetamodelCodeInMaudeFolder() throws CoreException {
		if (this.getFolderResult() != null) {
			metamodelCode = this.getFolderResult().getFile(metamodel.getName()+".maude");
		}
		return metamodelCode;
	}
	
	public IFile createInitialModelCodeInMaudeFolder() throws CoreException {
		if (this.getFolderResult() != null) {
			initialModelCode = this.getFolderResult().getFile(initialModel.getName()+".maude");
		}
		return initialModelCode;
	}
	
	public void setBehavior(IFile behaviorModel) {
		this.behavior = behaviorModel;
		BEHAVIOR_CODE = this.behavior.getName() + ".maude";
	}

	public void setMetamodel(IFile result) {
		this.metamodel = result;
	}
	
	public IFile getMetamodel() {
		return this.metamodel;
	}
		
	public void setResult(IPath result) {
		this.result = result;
	}

	public IPath getResult() {
		return this.result;
	}
	
	public void setFolderResultName(String folderResult) {
		this.folderResultName = folderResult;
	}
	
	public String getFolderResultName() {
		return folderResultName;
	}

	public IFile getMetamodelCode() {
		return metamodelCode;
	}

	public IFile getBehaviorCode() {
		return behaviorCode;
	}
	
	public IFile getInitialModelCode() {
		return initialModelCode;
	}

	public String getBehaviorString() {
		//return "platform:/resource"+this.behavior.getFullPath().toString();
		System.out.println("file://"+this.behavior.getRawLocation().toString());
		return "file:///"+this.behavior.getRawLocation().toString();
	}
	
	public String getMetamodelString() {
		//return "platform:/resource"+this.metamodel.getFullPath().toString();
		System.out.println(this.metamodel.getRawLocationURI().toString());
		return this.metamodel.getRawLocationURI().toString();
	}

	public IPath getBehaviorMaude() {
		return behaviorMaude;
	}

	public void setBehaviorMaude(IPath behaviorMaude) {
		this.behaviorMaude = behaviorMaude;
	}

	public IFolder getFolderTmp(String project){
		IProject currentProject = ResourcesPlugin.getWorkspace().getRoot().getProject(project);
		tmp = currentProject.getFolder(".tmp");
		return tmp.exists()?tmp:null;
	}
	
	public IFolder getFolderTmp(){
		IProject currentProject = ResourcesPlugin.getWorkspace().getRoot().getProject(this.getBehavior().getProject().getName());
		tmp = currentProject.getFolder(".tmp");
		return tmp.exists()?tmp:null;
	}

	public IFolder createFolderTmp(String project) throws CoreException {
		IProject currentProject = ResourcesPlugin.getWorkspace().getRoot().getProject(project);
		tmp = currentProject.getFolder(".tmp");
		if (!tmp.exists()) {
			tmp.create(true, true, null);
		}
		return tmp;
	}
	
	public IFolder createFolderResult() throws CoreException {
		if(folderResultName != null && this.getBehavior() != null){
			IProject currentProject = ResourcesPlugin.getWorkspace().getRoot().getProject(this.getBehavior().getProject().getName());
			folderResult = currentProject.getFolder(this.folderResultName);
			if (!folderResult.exists()) {
				folderResult.create(true, true, null);
			}
		}
		return folderResult;
	}

	public IPath getMetamodelMaude() {
		return metamodelMaude;
	}

	public void setMetamodelMaude(IPath metamodelMaude) {
		this.metamodelMaude = metamodelMaude;
	}
	
	public static String readFile(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath), 20000);
		String fileContents = "";
		String line = br.readLine();
		while (line != null) {
			fileContents = fileContents + line + "\n";
			line = br.readLine();
		}
		br.close();
		return fileContents;
	}

	public static String readFile(URL file) throws IOException {
		InputStream inp = file.openStream();
		String fileContents = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(inp));
		String line = br.readLine();
		while (line != null) {
			fileContents = fileContents + line + "\n";
			line = br.readLine();
		}
		br.close();
		return fileContents;
	}
	
	public String extractMetamodelMaudeName() throws IOException {
		BufferedReader br = null;
    try {
    	metamodelCode.refreshLocal(IResource.DEPTH_INFINITE, null);
	    br = new BufferedReader(new InputStreamReader(metamodelCode.getContents()));
    } catch (CoreException e) {
	    e.printStackTrace();
    }
		String line = br.readLine();
		while (!line.startsWith("  op ")) {
			line = br.readLine();
		}

		String acum = "";
		if (line.startsWith("  op ")) {
			line = line.replaceFirst("  op ", "");
			int i = 0;
			while (line.charAt(i) != ' ' && line.charAt(i) != '\t') {
				acum = acum + line.charAt(i);
				i++;
			}
		}
		return acum;
	}
	
	public String extractInitModelOp() throws IOException {
		BufferedReader br = null;
		StringBuilder res = null;
		if (initialModel != null) {
			res = new StringBuilder();
			try {
				initialModelCode.refreshLocal(IResource.DEPTH_INFINITE, null);
				br = new BufferedReader(new InputStreamReader(initialModelCode.getContents()));
			} catch (CoreException e) {
				e.printStackTrace();
			}
			String line = br.readLine();
			while (!line.startsWith("  op ")) {
				line = br.readLine();
			}

			if (line.startsWith("  op ")) {
				line = line.replaceFirst("  op ", "");
				int i = 0;
				while (line.charAt(i) != ' ' && line.charAt(i) != '\t') {
					res.append(line.charAt(i));
					i++;
				}
			}
		}
		return res==null?"emptyModel":res.toString();
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

	public String extractModuleMMName() throws IOException {
		BufferedReader br = null;
    try {
    	metamodelCode.refreshLocal(IResource.DEPTH_INFINITE, null);
	    br = new BufferedReader(new InputStreamReader(metamodelCode.getContents()));
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

	public IFolder getFolderResult() {
		return folderResult;
	}

	public void setFolderResult(IFolder folderResult) {
		this.folderResult = folderResult;
	}

	public void copyFile(IFile aux, URL resource) throws CoreException, IOException {
		if(!aux.exists()){
			aux.create(new ByteArrayInputStream(FileManager.readFile(resource).getBytes()), true, null);
		} else {
			aux.setContents(new ByteArrayInputStream(FileManager.readFile(resource).getBytes()), true, true, null);
		}
  }

	public IFile getInitialModel() {
	  return initialModel;
  }

	public void setInitialModel(IFile initialModel) {
	  this.initialModel = initialModel;
  }
	
	public IFile createParamsModelFile(String content) throws CoreException {
		IFile modelFile = tmp.getFile(PARAMS_MODEL_FILENAME);
		if (!modelFile.exists()) {
			modelFile.create(new ByteArrayInputStream(content.getBytes()), true, null);
		} else {
			modelFile.setContents(new ByteArrayInputStream(content.getBytes()), true, true, null);
		}
		return modelFile;
	}

	public IPath getInitialModelMaude() {
		return initialModelMaude;
	}

	public void setInitialModelMaude(IPath initialModelMaude) {
		this.initialModelMaude = initialModelMaude;
	}

	public IFile getTickModule() {
		return tickModule;
	}

	public void setTickModule(IFile tickModule) {
		this.tickModule = tickModule;
	}

	public void writeToOutputModelMaude(String contents) throws CoreException {
		createOutputModelMaude();
		if (!outputMaudeModel.exists()) {
			outputMaudeModel.create(new ByteArrayInputStream(contents.getBytes()), true, null);
		} else {
			outputMaudeModel.setContents(new ByteArrayInputStream(contents.getBytes()), true, true, null);
		}
	}
	
	public IFile getOutputMaudeModel() {
		return outputMaudeModel;
	}

	public void setOutputMaudeModel(IFile outputMaudeModel) {
		this.outputMaudeModel = outputMaudeModel;
	}

	public IFile getAuxATLInverseTransformation() throws CoreException {
		String project = this.getBehavior().getProject().getName();
		tmp = this.createFolderTmp(project);
		auxATLInverseTransformation = tmp.getFile(AUX_ATL_INVERSE_TRANSF);
		return auxATLInverseTransformation;
	}
	
	public IFile getAuxATLInverseTransformationASM() throws CoreException {
		String project = this.getBehavior().getProject().getName();
		tmp = this.createFolderTmp(project);
		auxATLInverseTransformationASM = tmp.getFile(AUX_ATL_INVERSE_TRANSF_ASM);
		return auxATLInverseTransformationASM;
	}
}
