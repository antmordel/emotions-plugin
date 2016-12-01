package es.uma.lcc.e_motions.jobs;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.m2m.atl.core.ATLCoreException;

import es.uma.lcc.e_motions.common.CommonOperations;
import es.uma.lcc.e_motions.runningdata.CodGenerationFileManager;
import es.uma.lcc.e_motions.transformations.creation.EmptyModel;
import es.uma.lcc.e_motions.transformations.creation.TickRuleModule;

public class CodGenerationJob extends EmotionsJob {

	public CodGenerationJob(String name, CodGenerationFileManager fm) {
		super(name, fm);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		CodGenerationFileManager fm = (CodGenerationFileManager) super.fm;
		monitor.beginTask("e-Motions code generation", 1);

		/* Ecore 2 Maude */
		init = System.currentTimeMillis();
		runEcore2MaudeM2M(monitor);
		runEcoreMaude2Code(monitor);
		console.successln("Ecore 2 Maude transformation generated in "
				+ df.format((System.currentTimeMillis() - init) / 1000.0) + " seconds.");
		
		/* Behavior 2 Maude */
		init = System.currentTimeMillis();
		runBehavior2MaudeM2M(monitor);
		runBehavior2MaudeCode(monitor);
		console.successln("Behavior 2 Maude transformation generated in "
				+ df.format((System.currentTimeMillis() - init) / 1000.0) + " seconds.");
		
		/* Input model */
		monitor.subTask("Initial Model 2 Maude");
		monitor.worked(1);
		init = System.currentTimeMillis();
		if (fm.getInitModel() == null) {
			/* generate empty model */
			EmptyModel creator;
			try {
				creator = new EmptyModel(CommonOperations.extractModuleName(fm.getMetamodelMaudeCode()), 
						CommonOperations.extractMetamodelOperator(fm.getMetamodelMaudeCode()), fm.getInitModelCode());
				creator.create();
			} catch (IOException | CoreException e) {
				console.errorln("Error creating an EmptyModel");
			}
		}
		/* assert: init model is not null */
		console.successln("Init model created in "
				+ df.format((System.currentTimeMillis() - init) / 1000.0) + " seconds.");
		
		/* Infraestructure */
		monitor.subTask("Copying Maude infrastructure");
		monitor.worked(1);
		init = System.currentTimeMillis();
		copyInfrastuctureFiles();
		console.successln("Maude infraestructure copied in "
				+ df.format((System.currentTimeMillis() - init) / 1000.0) + " seconds.");
		
		/* Create run Maude file */
		monitor.subTask("Creating run.maude file");
		monitor.worked(1);
		init = System.currentTimeMillis();
		TickRuleModule tickRuleMod;
		try {
			tickRuleMod = new TickRuleModule(fm.getMetamodelMaudeCode().getName(), 
					fm.getBehaviorMaudeCode().getName(), 
					fm.getInitModelCode().getName(), 
					fm.getRunMaudeCode(), 
					fm.isAppliedRules(), 
					fm.isShowAdvisories(),
					CommonOperations.extractModuleName(fm.getInitModelCode()),
					CommonOperations.extractModuleName(fm.getBehaviorMaudeCode()),
					fm.isInfiniteLimitTime(),
					fm.getLimitTime(),
					CommonOperations.extractInitModelOperator(fm.getInitModelCode()));
			tickRuleMod.create();
		} catch (CoreException | ATLCoreException | IOException e) {
			e.printStackTrace();
		}
		console.successln("Maude run file created in "
				+ df.format((System.currentTimeMillis() - init) / 1000.0) + " seconds.");
		
		monitor.done();
		return Status.OK_STATUS;
	}

}
