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
	
	private int limitTime;
	private boolean infiniteLimitTime;

	private boolean appliedRules;
	private boolean showAdvisories;
	
	private String folderOutputPathString;
	
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
	public String getFolderOutputPathString() {
		return folderOutputPathString;
	}
	public void setFolderOutputPathString(String outputFolderPathString) {
		this.folderOutputPathString = outputFolderPathString;
	}
	@Override
	public String toString() {
		return "FileManager [behaviorModel=" + behaviorModel + ", metamodel=" + metamodel + ", limitTime=" + limitTime
				+ ", infiniteLimitTime=" + infiniteLimitTime + ", appliedRules=" + appliedRules + ", showAdvisories="
				+ showAdvisories + ", folderOutputPathString=" + folderOutputPathString + "]";
	}
	
	
}
