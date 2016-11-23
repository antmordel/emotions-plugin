package es.uma.lcc.e_motions.handlers;

import org.eclipse.core.commands.AbstractHandler;

import es.uma.lcc.e_motions.console.EmotionsConsole;
import es.uma.lcc.e_motions.launchconfiguration.EmotionsLaunchConfiguration;
import es.uma.lcc.e_motions.runningdata.EmotionsFileManager;

public abstract class EmotionsHandler extends AbstractHandler {
	
	protected EmotionsFileManager fm;
	protected EmotionsLaunchConfiguration launch;
	
	
	protected void getConsole() {
		EmotionsConsole.getDefault().setVisible().clean();
	}
}
