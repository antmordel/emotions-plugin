package es.uma.lcc.e_motions.dialogs;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.wb.swt.SWTResourceManager;

import es.uma.lcc.e_motions.runningdata.PalladioFileManager;
import es.uma.lcc.e_motions.runningdata.PalladioRunningInformation;

/**
 * 
 * @author Antonio Moreno-Delgado <amoreno@lcc.uma.es>
 *
 */
public class PalladioDialog extends EmotionsDialog {
	
	private Shell shell;
	
	private Text textUsageModel;
	private Text textRepositoryModel;
	private Text textSystemModel;
	private Text textAllocationModel;
	private Text textResourceEnvModel;
	
	
	private PalladioRunningInformation info;

	public PalladioDialog(Shell parentShell, PalladioFileManager fm) {
		super(parentShell, fm);
		info = PalladioRunningInformation.getDefault();
		shell = parentShell;
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Palladio into e-Motions code generation");
	}
	
	@Override
    protected Point getInitialSize() {
    	return new Point(600, 750);
    }
	
	private void createPalladioFilesGroup(Composite composite) {
		Group grpPalladioFiles = new Group(composite, SWT.NONE);
		grpPalladioFiles.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		grpPalladioFiles.setText("Palladio files");
		grpPalladioFiles.setBounds(20, 170, 560, 240);
		
		Label lblUsageModel = new Label(grpPalladioFiles, SWT.NONE);
		lblUsageModel.setBounds(61, 30, 76, 15);
		lblUsageModel.setText("Usage Model:");
		
		textUsageModel = new Text(grpPalladioFiles, SWT.BORDER);
		textUsageModel.setBounds(143, 23, 300, 25);
		textUsageModel.setEditable(false);
		if (info.getUsageModel() != null) {
			textUsageModel.setText(info.getUsageModel().getFullPath().toOSString());
		}
		
		Button btnUsageModel = new Button(grpPalladioFiles, SWT.NONE);
		btnUsageModel.setBounds(449, 23, 80, 25);
		btnUsageModel.setText("Browse");
		btnUsageModel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(shell, new WorkbenchLabelProvider(),
						new BaseWorkbenchContentProvider());
				dialog.setTitle("Usage Model file");
				dialog.setMessage("Select *.usagemodel file");
				dialog.setInput(ResourcesPlugin.getWorkspace());
				dialog.addFilter(new FilePatternFilter("usagemodel"));
				dialog.open();
				
				IFile resultUsageModel = (IFile) dialog.getResult()[0];
				if (dialog.getReturnCode() == Window.OK) {
					textUsageModel.setText(resultUsageModel.getFullPath().toOSString());
					info.setUsageModel(resultUsageModel);
					checkIfCompleted();
				}
			} 
		});
		
		Label lblRepository = new Label(grpPalladioFiles, SWT.NONE);
		lblRepository.setBounds(38, 69, 99, 15);
		lblRepository.setText("Repository Model:");
		
		textRepositoryModel = new Text(grpPalladioFiles, SWT.BORDER);
		textRepositoryModel.setBounds(143, 62, 300, 25);
		textRepositoryModel.setEditable(false);
		if (info.getRepositoryModel() != null) {
			textRepositoryModel.setText(info.getRepositoryModel().getFullPath().toOSString());
		}
		
		Button btnRepositoryModel = new Button(grpPalladioFiles, SWT.NONE);
		btnRepositoryModel.setBounds(449, 62, 80, 25);
		btnRepositoryModel.setText("Browse");
		btnRepositoryModel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(shell, new WorkbenchLabelProvider(),
						new BaseWorkbenchContentProvider());
				dialog.setTitle("Repository Model file");
				dialog.setMessage("Select *.repository file");
				dialog.setInput(ResourcesPlugin.getWorkspace());
				dialog.addFilter(new FilePatternFilter("repository"));
				dialog.open();
				
				IFile resultRepositoryModel = (IFile) dialog.getResult()[0];
				if (dialog.getReturnCode() == Window.OK) {
					textRepositoryModel.setText(resultRepositoryModel.getFullPath().toOSString());
					info.setRepositoryModel(resultRepositoryModel);
					checkIfCompleted();
				}
			} 
		});
		
		Label lblSystemModel = new Label(grpPalladioFiles, SWT.NONE);
		lblSystemModel.setText("System Model:");
		lblSystemModel.setBounds(55, 105, 82, 15);
		
		textSystemModel = new Text(grpPalladioFiles, SWT.BORDER);
		textSystemModel.setBounds(143, 101, 300, 25);
		textSystemModel.setEditable(false);
		if (info.getSystemModel() != null) {
			textSystemModel.setText(info.getSystemModel().getFullPath().toOSString());
		}
		
		Button btnSystemModel = new Button(grpPalladioFiles, SWT.NONE);
		btnSystemModel.setText("Browse");
		btnSystemModel.setBounds(449, 101, 80, 25);
		btnSystemModel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(shell, new WorkbenchLabelProvider(),
						new BaseWorkbenchContentProvider());
				dialog.setTitle("System Model file");
				dialog.setMessage("Select *.system file");
				dialog.setInput(ResourcesPlugin.getWorkspace());
				dialog.addFilter(new FilePatternFilter("system"));
				dialog.open();
				
				IFile resultSystemModel = (IFile) dialog.getResult()[0];
				if (dialog.getReturnCode() == Window.OK) {
					textSystemModel.setText(resultSystemModel.getFullPath().toOSString());
					info.setSystemModel(resultSystemModel);
					checkIfCompleted();
				}
			} 
		});
		
		Label lblAllocationModel = new Label(grpPalladioFiles, SWT.NONE);
		lblAllocationModel.setText("Allocation Model:");
		lblAllocationModel.setBounds(38, 145, 99, 15);
		
		textAllocationModel = new Text(grpPalladioFiles, SWT.BORDER);
		textAllocationModel.setBounds(143, 140, 300, 25);
		textAllocationModel.setEditable(false);
		if (info.getAllocationModel() != null) {
			textAllocationModel.setText(info.getAllocationModel().getFullPath().toOSString());
		}
		
		Button btnAllocationModel = new Button(grpPalladioFiles, SWT.NONE);
		btnAllocationModel.setText("Browse");
		btnAllocationModel.setBounds(449, 140, 80, 25);
		btnAllocationModel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(shell, new WorkbenchLabelProvider(),
						new BaseWorkbenchContentProvider());
				dialog.setTitle("Allocation Model file");
				dialog.setMessage("Select *.allocation file");
				dialog.setInput(ResourcesPlugin.getWorkspace());
				dialog.addFilter(new FilePatternFilter("allocation"));
				dialog.open();
				
				IFile resultAllocationModel = (IFile) dialog.getResult()[0];
				if (dialog.getReturnCode() == Window.OK) {
					textAllocationModel.setText(resultAllocationModel.getFullPath().toOSString());
					info.setAllocationModel(resultAllocationModel);
					checkIfCompleted();
				}
			} 
		});
		
		
		Label lblResourceenvironmentModel = new Label(grpPalladioFiles, SWT.NONE);
		lblResourceenvironmentModel.setText("Resource Env. Model:");
		lblResourceenvironmentModel.setBounds(20, 182, 117, 15);
		
		textResourceEnvModel = new Text(grpPalladioFiles, SWT.BORDER);
		textResourceEnvModel.setBounds(143, 179, 300, 25);
		textResourceEnvModel.setEditable(false);
		if (info.getResenvModel() != null) {
			textResourceEnvModel.setText(info.getResenvModel().getFullPath().toOSString());
		}
		
		Button btnResourceEnvModel = new Button(grpPalladioFiles, SWT.NONE);
		btnResourceEnvModel.setText("Browse");
		btnResourceEnvModel.setBounds(449, 179, 80, 25);
		btnResourceEnvModel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(shell, new WorkbenchLabelProvider(),
						new BaseWorkbenchContentProvider());
				dialog.setTitle("Resource Environment Model file");
				dialog.setMessage("Select *.resourceenvironment file");
				dialog.setInput(ResourcesPlugin.getWorkspace());
				dialog.addFilter(new FilePatternFilter("resourceenvironment"));
				dialog.open();
				
				IFile resultResourceEnvModel = (IFile) dialog.getResult()[0];
				if (dialog.getReturnCode() == Window.OK) {
					textResourceEnvModel.setText(resultResourceEnvModel.getFullPath().toOSString());
					info.setResenvModel(resultResourceEnvModel);
					checkIfCompleted();
				}
			} 
		});
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(null);
		
		createEmotionsFilesGroup(composite);
		createPalladioFilesGroup(composite);
		createSimulationGroup(composite, 420);
		createOutputGroup(composite);
				
		return composite;
	}
	
	@Override
	protected Control createContents(Composite parent) {
		Control control = super.createContents(parent);
		checkIfCompleted();
		setTextsWhite();
		return control;
	}
	
	private void checkIfCompleted() {
		Button btnOk = getButton(IDialogConstants.OK_ID);
		boolean completed = checkIfCompleted(textBehavior) &&
				checkIfCompleted(textMetamodel) &&
				checkIfCompleted(textUsageModel) &&
				checkIfCompleted(textRepositoryModel) &&
				checkIfCompleted(textSystemModel) &&
				checkIfCompleted(textAllocationModel) &&
				checkIfCompleted(textResourceEnvModel);
		btnOk.setEnabled(completed);
	}
	
	private boolean checkIfCompleted(Text text) {
		return text != null && text.getText() != null && !text.getText().isEmpty();
	}

	private void setTextsWhite() {
		// emotions files
		setTextWhite(textBehavior);
		setTextWhite(textMetamodel);
		// palladio files
		setTextWhite(textUsageModel);
		setTextWhite(textRepositoryModel);
		setTextWhite(textSystemModel);
		setTextWhite(textAllocationModel);
		setTextWhite(textResourceEnvModel);
		
	}
	
	private void setTextWhite(Text t) {
		t.setBackground(new Color(shell.getDisplay(), new RGB(255, 255, 255)));
	}

}
