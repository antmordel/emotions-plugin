package es.uma.lcc.e_motions.dialogs;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.wb.swt.SWTResourceManager;

import es.uma.lcc.e_motions.runningdata.FileManager;

/**
 * 
 * @author Antonio Moreno-Delgado <amoreno@lcc.uma.es>
 *
 */
public class EmotionsDialog extends Dialog {
	
	protected FileManager fm;
	protected Text textBehavior;
	protected Text textMetamodel;

	protected EmotionsDialog(Shell parentShell) {
		super(parentShell);
	}
	
	protected EmotionsDialog(Shell parentShell, FileManager fm) {
		this(parentShell);
		this.fm = fm;
	}
	
	protected void createEmotionsFilesGroup(Composite composite) {
		Group groupEmotionsFiles = new Group(composite, SWT.NONE);
		groupEmotionsFiles.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		groupEmotionsFiles.setText("e-Motions files");
		groupEmotionsFiles.setBounds(20, 34, 560, 130);
		
		Label lblEmotionsBehavior = new Label(groupEmotionsFiles, SWT.NONE);
		lblEmotionsBehavior.setBounds(20, 40, 126, 15);
		lblEmotionsBehavior.setText("e-Motions behavior:");
		
		textBehavior = new Text(groupEmotionsFiles, SWT.BORDER);
		textBehavior.setBounds(143, 37, 300, 25);
		textBehavior.setEditable(false);
		if (fm.getBehaviorModel() != null) {
			textBehavior.setText(fm.getBehaviorModel().getFullPath().toPortableString());
		}
		
		Button btnBehModel = new Button(groupEmotionsFiles, SWT.NONE);
		btnBehModel.setBounds(449, 35, 80, 25);
		btnBehModel.setText("Browse");
		btnBehModel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(shell, new WorkbenchLabelProvider(),
						new BaseWorkbenchContentProvider());
				dialog.setTitle("e-Motions behavior model");
				dialog.setMessage("Select *.behavior file");
				dialog.setInput(ResourcesPlugin.getWorkspace());
				dialog.addFilter(new FilePatternFilter("behavior"));
				dialog.open();
				
				IFile resultBehavior = (IFile) dialog.getResult()[0];
				if (dialog.getReturnCode() == Window.OK) {
					textBehavior.setText(resultBehavior.getFullPath().toPortableString());
					fm.setBehaviorModel(resultBehavior);
					// TODO checkIfCompleted();
				}
			} 
		});
		
		Label lblMetamodel = new Label(groupEmotionsFiles, SWT.NONE);
		lblMetamodel.setBounds(67, 78, 70, 15);
		lblMetamodel.setText("Metamodel:");

		textMetamodel = new Text(groupEmotionsFiles, SWT.BORDER);
		textMetamodel.setBounds(143, 75, 300, 21);
		textMetamodel.setEditable(false);
		if (fm.getMetamodel() != null) {
			textMetamodel.setText(fm.getMetamodel().getFullPath().toPortableString());
		}
		
		Button btnMetamodel = new Button(groupEmotionsFiles, SWT.NONE);
		btnMetamodel.setBounds(449, 73, 80, 25);
		btnMetamodel.setText("Browse");
		btnMetamodel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(shell, new WorkbenchLabelProvider(),
						new BaseWorkbenchContentProvider());
				dialog.setTitle("Metamodel");
				dialog.setMessage("Select *.ecore file");
				dialog.setInput(ResourcesPlugin.getWorkspace());
				dialog.addFilter(new FilePatternFilter("ecore"));
				dialog.open();
				
				IFile resultMetamodel = (IFile) dialog.getResult()[0];
				if (dialog.getReturnCode() == Window.OK) {
					textMetamodel.setText(resultMetamodel.getFullPath().toOSString());
					info.setMetamodel(resultMetamodel);
					checkIfCompleted();
				}
			} 
		});
	}
}
