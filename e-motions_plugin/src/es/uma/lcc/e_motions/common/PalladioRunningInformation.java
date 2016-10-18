package es.uma.lcc.e_motions.common;

import org.eclipse.core.resources.IFile;

/**
 * Stores those files needed for transformation and simulation.
 * 
 * 
 * @author Antonio Moreno-Delgado <amoreno@lcc.uma.es>
 *
 */
public class PalladioRunningInformation {
	
	private static PalladioRunningInformation self;
	
	private IFile behaviorModel;
	private IFile metamodel;
	
	private IFile usageModel;
	private IFile repositoryModel;
	private IFile systemModel;
	private IFile allocationModel;
	private IFile resenvModel;
	
	private int limitTime;
	
	private boolean appliedRules;
	private boolean showAdvisories;
	
	private String outputFolder;
	
	private PalladioRunningInformation(){
		appliedRules = true;
		showAdvisories = false;
	}
	public static PalladioRunningInformation getDefault() {
		if (self == null) {
			self = new PalladioRunningInformation();
		}
		return self;
	}
	
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
	public String getOutputFolder() {
		return outputFolder;
	}
	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}
	
}
