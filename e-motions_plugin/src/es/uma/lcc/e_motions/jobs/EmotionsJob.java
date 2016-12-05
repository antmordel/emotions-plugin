package es.uma.lcc.e_motions.jobs;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.m2m.atl.core.ATLCoreException;

import es.uma.lcc.e_motions.codegeneration.Model2TextMaude;
import es.uma.lcc.e_motions.common.CommonOperations;
import es.uma.lcc.e_motions.common.EmotionsPaths;
import es.uma.lcc.e_motions.console.EmotionsConsole;
import es.uma.lcc.e_motions.runningdata.EmotionsFileManager;
import es.uma.lcc.e_motions.transformations.creation.EmptyModel;
import es.uma.lcc.e_motions.transformations.model2model.Behavior2MaudeM2M;
import es.uma.lcc.e_motions.transformations.model2model.Ecore2MaudeM2M;
import es.uma.lcc.e_motions.transformations.model2model.InitModel2MaudeM2M;
import es.uma.lcc.e_motions.transformations.model2model.OclBehaviorParser;

public abstract class EmotionsJob extends Job {

	protected long init;
	protected DecimalFormat df;
	protected EmotionsConsole console;
	protected EmotionsFileManager fm;

	public EmotionsJob(String name) {
		super(name);
		df = new DecimalFormat("#0.000");
		console = EmotionsConsole.getDefault();
	}

	public EmotionsJob(String name, EmotionsFileManager fm) {
		this(name);
		this.fm = fm;
	}

	protected void runEcore2MaudeM2M(IProgressMonitor monitor) {
		monitor.subTask("Ecore 2 Maude M2M");
		monitor.worked(1);
		try {
			Ecore2MaudeM2M trans = new Ecore2MaudeM2M(fm.getMetamodel(), fm.getMetamodelMaudeXMI());
			trans.execute();
		} catch (ATLCoreException | IOException e) {
			console.errorln("ERROR: Ecore to Maude Model 2 Model transformation. Stack trace: " + e.getMessage());
			monitor.setCanceled(true);
		}
	}

	protected void runEcoreMaude2Code(IProgressMonitor monitor) {
		monitor.subTask("Ecore 2 Maude M2T");
		monitor.worked(1);

		try {
			String inputModelXMI = fm.getMetamodelMaudeXMI().getFullPath().toPortableString();
			String outputModelCode = fm.getMetamodelMaudeCode().getLocation().toPortableString();
			Model2TextMaude.generate(inputModelXMI, outputModelCode);
		} catch (NoSuchFileException | CoreException e) {
			e.printStackTrace();
		}
	}

	protected void runBehavior2MaudeM2M(IProgressMonitor monitor) {
		monitor.subTask("Behavior 2 Maude M2M");
		monitor.worked(1);
		try {
			OclBehaviorParser oclParser = new OclBehaviorParser(fm.getBehaviorModel(), fm.getMetamodel(),
					fm.getOclParserCode(), fm.getOclParserXMI());
			oclParser.execute();
			Behavior2MaudeM2M beh2maudeM2M = new Behavior2MaudeM2M(fm.getBehaviorModel(), fm.getOclParserXMI(),
					fm.getBehaviorMaudeXMI());
			beh2maudeM2M.execute();
		} catch (CoreException | ATLCoreException | IOException e) {
			e.printStackTrace();
		}
	}

	protected void runBehavior2MaudeCode(IProgressMonitor monitor) {
		monitor.subTask("Behavior 2 Maude M2T");
		monitor.worked(1);

		try {
			String inputModelXMI = fm.getBehaviorMaudeXMI().getFullPath().toPortableString();
			String outputModelCode = fm.getBehaviorMaudeCode().getLocation().toPortableString();
			Model2TextMaude.generate(inputModelXMI, outputModelCode);
		} catch (NoSuchFileException | CoreException e) {
			e.printStackTrace();
		}
	}

	protected void copyInfrastuctureFiles(IProgressMonitor monitor) throws IOException, CoreException {
		monitor.subTask("Copying Maude infrastructure");
		monitor.worked(1);

		List<String> filesToCopy = Arrays.asList("mOdCL.maude", "MGDefinitions.maude", "EcoreMM.maude",
				"MGRealTimeMaude24.maude", "e-Motions.maude");
		for (String file : filesToCopy) {
			fm.copyFile(file);
		}
	}

	protected void runInitModel2MaudeCode(IProgressMonitor monitor) {
		monitor.subTask("Initial Model 2 Maude");
		monitor.worked(1);
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
		} else {
			/* init model not null */
			try {
				fm.getInitModel().refreshLocal(IResource.DEPTH_INFINITE, null);
				InitModel2MaudeM2M initM2M = new InitModel2MaudeM2M(EmotionsPaths.MM_MAUDE, EmotionsPaths.MM_ATLPARAMS,
						EmotionsPaths.MODEL2MAUDE, fm.getMetamodel(), fm.getInitModel(), fm.getParamsAuxFile(),
						fm.getInitModelXmi());
				initM2M.execute();
			} catch (CoreException | ATLCoreException | IOException e) {
				console.errorln("Error in the InitModel2Maude M2M transformation. " + e.getMessage());
			}
			try {
				String inputModelXMI = fm.getInitModelXmi().getFullPath().toPortableString();
				String outputModelCode = fm.getInitModelCode().getFullPath().toPortableString();
				outputModelCode = fm.getInitModelCode().getLocation().toPortableString();
				Model2TextMaude.generate(inputModelXMI, outputModelCode);
			} catch (NoSuchFileException | CoreException e) {
				console.errorln("Error in the InitModel2Maude model 2 text transformation. " + e.getMessage());
			}
		}
	}
}
