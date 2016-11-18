package es.uma.lcc.e_motions.runningdata;

import org.eclipse.core.resources.IFile;

/**
 * 
 * @author Antonio Moreno-Delgado <amoreno@lcc.uma.es>
 *
 */
public abstract class FileManager {
	
	protected IFile behaviorModel;
	protected IFile metamodel;
	
	
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
	
}
