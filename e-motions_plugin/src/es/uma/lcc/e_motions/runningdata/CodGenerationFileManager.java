package es.uma.lcc.e_motions.runningdata;

import java.nio.file.NoSuchFileException;

import org.eclipse.core.resources.IFile;

/**
 * 
 * @author Antonio Moreno-Delgado <amoreno@lcc.uma.es>
 *
 */
public class CodGenerationFileManager extends EmotionsFileManager {
	
	/* Input data */
	protected IFile initModel;
	protected IFile initModelCode;

	public IFile getInitModel() {
		return initModel;
	}

	public void setInitModel(IFile initModel) {
		this.initModel = initModel;
	}
	
	public IFile getInitModelCode() throws NoSuchFileException {
		if (initModelCode == null) {
			if (getMetamodel() == null) {
				throw new NoSuchFileException("Init model cannot be found");
			}
			if (initModelCode == null) {
				initModelCode = getFolderResult().getFile("EmptyModel.maude");
			} else {
				initModelCode = getFolderResult().getFile(getInitModel().getName() + ".maude");
			}
		}
		return initModelCode;
	}
}
