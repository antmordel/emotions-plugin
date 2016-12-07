package es.uma.lcc.e_motions.jobs;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.m2m.atl.core.ATLCoreException;

import es.uma.lcc.e_motions.Activator;
import es.uma.lcc.e_motions.common.CommonOperations;
import es.uma.lcc.e_motions.runningdata.PalladioFileManager;
import es.uma.lcc.e_motions.transformations.creation.TickRuleModulePalladio;
import es.uma.lcc.e_motions.transformations.model2model.Behavior2MaudePalladioM2M;
import es.uma.lcc.e_motions.transformations.model2model.OclBehaviorParser;
import es.uma.lcc.e_motions.transformations.model2model.PalladioFlattenInputModel;

public class PalladioJob extends EmotionsJob {

	public PalladioJob(String name, PalladioFileManager fm) {
		super(name, fm);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		console.println("Maude ATL path: " + Activator.getDefault().getBundle().getEntry("lib/metamodels/Maude.ecore"));
		
		PalladioFileManager fm = (PalladioFileManager) super.fm;
		monitor.beginTask("Palladio code generation", 1);

		/* Flattening Palladio models */
		monitor.subTask("Flattening input models");
		monitor.worked(1);
		init = System.currentTimeMillis();
		PalladioFlattenInputModel palladioFlatten;
		try {
			/* Firstly, we create a init model */
			fm.createInitModel();
			palladioFlatten = new PalladioFlattenInputModel(fm.getUsageModel(),
					fm.getRepositoryModel(), fm.getSystemModel(), fm.getAllocationModel(), fm.getResenvModel(),
					fm.getNewUsageModel(), fm.getNewRepositoryModel(), fm.getNewSystemModel(), fm.getNewAllocationModel(), fm.getNewResenvModel(),
					fm.getPalladioResourceTypes(), fm.getPalladioPrimitiveTypes(), fm.getInitModel());
			palladioFlatten.execute();
		} catch (CoreException | IOException e) {
			console.errorln("Flatten input model failed. " + e.getMessage());
		}
		console.successln("Flatten Palladio input model in " + df.format((System.currentTimeMillis() - init) / 1000.0)
				+ " seconds.");
		/* end Flattening Palladio models */
		
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
		
		/* Init model 2 Maude */
		/* we do know that the init model is not empty, unlike that in the CodgenerationJob */
		init = System.currentTimeMillis();
		runInitModel2MaudeCode(monitor);
		console.successln("InitModel 2 Maude transformation generated in "
				+ df.format((System.currentTimeMillis() - init) / 1000.0) + " seconds.");
		
		/* Infraestructure */
		monitor.subTask("Copying Maude infrastructure");
		monitor.worked(1);
		init = System.currentTimeMillis();
		try {
			copyInfrastuctureFiles(monitor);
		} catch (IOException | CoreException e) {
			console.errorln(e.getMessage());
		}
		console.successln("Maude infraestructure copied in "
				+ df.format((System.currentTimeMillis() - init) / 1000.0) + " seconds.");
		
		/* Create run Maude file */
		monitor.subTask("Creating run.maude file for Palladio");
		monitor.worked(1);
		init = System.currentTimeMillis();
		TickRuleModulePalladio tickRuleMod;
		try {
			tickRuleMod = new TickRuleModulePalladio(fm.getMetamodelMaudeCode().getName(), 
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

	protected void runBehavior2MaudeM2M(IProgressMonitor monitor) {
		monitor.subTask("Behavior 2 Maude M2M");
		monitor.worked(1);
		try {
			OclBehaviorParser oclParser = new OclBehaviorParser(fm.getBehaviorModel(), fm.getMetamodel(),
					fm.getOclParserCode(), fm.getOclParserXMI());
			oclParser.execute();
			Behavior2MaudePalladioM2M beh2maudeM2M = new Behavior2MaudePalladioM2M(fm.getBehaviorModel(), fm.getOclParserXMI(),
					fm.getBehaviorMaudeXMI());
			beh2maudeM2M.execute();
		} catch (CoreException | ATLCoreException | IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void copyInfrastuctureFiles(IProgressMonitor monitor) throws IOException, CoreException {
		super.copyInfrastuctureFiles(monitor);
		fm.copyFile("PalladioGrammar.maude");
	}
	
	
}
