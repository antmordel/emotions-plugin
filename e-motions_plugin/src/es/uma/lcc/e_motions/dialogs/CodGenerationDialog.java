package es.uma.lcc.e_motions.dialogs;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import es.uma.lcc.e_motions.runningdata.CodGenerationFileManager;


public class CodGenerationDialog extends EmotionsDialog {
	
	protected static Text textnombremodelo2, textnombremodelo, textAdditionalModels;
	protected static Button mod2urlbutton, mod2uributton, mmod3uributton, clearAddMM, clearInitMM;
	protected static boolean issetEcoreMM = false;
	static Label UMLWarningLabel;
	static Text textnombremmodelo3;

	public CodGenerationDialog(Shell parentShell, CodGenerationFileManager fm) {
		super(parentShell, fm);
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Maude Infrastructure Code generation");
	}
	
	@Override
	protected Point getInitialSize() {
    	return new Point(600, 500);
    }

	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(null);
		
		createEmotionsFilesGroup(composite);
		createSimulationGroup(composite, 180);
		
		return composite;
	}
}
