package es.uma.lcc.e_motions.jobs;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.text.DecimalFormat;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.m2m.atl.core.ATLCoreException;

import es.uma.lcc.e_motions.codegeneration.Model2TextMaude;
import es.uma.lcc.e_motions.console.EmotionsConsole;
import es.uma.lcc.e_motions.runningdata.EmotionsFileManager;
import es.uma.lcc.e_motions.transformations.model2model.Ecore2MaudeM2M;
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
			
			
		} catch (CoreException | ATLCoreException | IOException e) {
			e.printStackTrace();
		}
	}
}
