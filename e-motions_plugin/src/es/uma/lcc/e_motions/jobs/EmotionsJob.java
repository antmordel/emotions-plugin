package es.uma.lcc.e_motions.jobs;

import java.text.DecimalFormat;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.m2m.atl.core.ATLCoreException;

import es.uma.lcc.e_motions.console.EmotionsConsole;
import es.uma.lcc.e_motions.runningdata.EmotionsFileManager;
import es.uma.lcc.e_motions.transformations.model2model.Ecore2MaudeM2M;

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
		init = System.currentTimeMillis();
		Ecore2MaudeM2M trans = new Ecore2MaudeM2M();
		try {
			trans.execute();
		} catch (ATLCoreException e) {
			console.errorln("ERROR: Ecore to Maude Model 2 Model transformation. Stack trace: " + e.getStackTrace());
			monitor.setCanceled(true);
		}
	}
}
