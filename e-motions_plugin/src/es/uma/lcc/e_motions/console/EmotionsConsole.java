package es.uma.lcc.e_motions.console;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

/**
 * Instead of using the legacy e-Motions console that was a *view*,
 * we will use the built-in Console of Eclipse.
 * 
 * @author Antonio Moreno-Delgado <amoreno@lcc.uma.es>
 *
 */
public class EmotionsConsole {
	
	private static EmotionsConsole self;
	private MessageConsole console;
	private MessageConsoleStream consoleStream;
	
	private EmotionsConsole() {
		console = new MessageConsole("e-Motions Console", null);
		consoleStream = console.newMessageStream();
		
		ConsolePlugin plugin = ConsolePlugin.getDefault();
	    IConsoleManager conMan = plugin.getConsoleManager();
	    conMan.addConsoles(new IConsole[]{console});
	}
	
	public static EmotionsConsole getDefault() {
		if (self == null) {
			self = new EmotionsConsole();
		}
		return self;
	}
	
	public EmotionsConsole setVisible() {
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		IWorkbenchPage page = win.getActivePage();
		
		try {
			IConsoleView view = (IConsoleView) page.showView(IConsoleConstants.ID_CONSOLE_VIEW);
			view.display(console);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public EmotionsConsole clean() {
		console.clearConsole();
		return this;
	}
	
	public void print(String text) {
		consoleStream.print(text);
	}
	
	public void println(String text) {
		consoleStream.println(text);
	}
	
	public void errorln(String text) {
//		Color beforeColor = consoleStream.getColor();
//		consoleStream.setColor(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		consoleStream.println(text);
//		consoleStream.setColor(beforeColor);
	}

	public void successln(String text) {
//		Color beforeColor = consoleStream.getColor();
//		consoleStream.setColor(SWT.COLOR_GREEN);
		consoleStream.print("Success: ");
//		consoleStream.setColor(beforeColor);
		consoleStream.println(text);
	}
}
