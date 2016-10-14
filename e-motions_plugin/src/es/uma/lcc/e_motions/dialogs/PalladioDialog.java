package es.uma.lcc.e_motions.dialogs;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;

public class PalladioDialog extends Dialog {
	
	private Shell shell;
	private Text textBehavior;
	private Text textMetamodel;
	private Text textUsageModel;
	private Text textRepositoryModel;
	private Text textSystemModel;
	private Text textAllocationModel;
	private Text textResourceEnvModel;
	private Text textTimeLimit;

	public PalladioDialog(Shell parentShell) {
		super(parentShell);
		shell = parentShell;
	}
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Palladio into e-Motions code generation");
	}
	
	@Override
    protected Point getInitialSize() {
    	return new Point(600, 700);
    }
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(null);
		
		/*
		 * e-Motions files
		 */
		Group groupEmotionsFiles = new Group(composite, SWT.NONE);
		groupEmotionsFiles.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		groupEmotionsFiles.setText("e-Motions files");
		groupEmotionsFiles.setBounds(20, 34, 513, 108);
		
		Label lblEmotionsBehavior = new Label(groupEmotionsFiles, SWT.NONE);
		lblEmotionsBehavior.setBounds(30, 40, 106, 15);
		lblEmotionsBehavior.setText("e-Motions behavior:");
		
		textBehavior = new Text(groupEmotionsFiles, SWT.BORDER);
		textBehavior.setBounds(143, 37, 300, 25);
		textBehavior.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				checkIfCompleted();
			}
		});
		
		Button btnBehModel = new Button(groupEmotionsFiles, SWT.NONE);
		btnBehModel.setBounds(449, 35, 54, 25);
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
					textBehavior.setText(resultBehavior.getFullPath().toOSString());
					checkIfCompleted();
				}
			} 
		});
		
		textMetamodel = new Text(groupEmotionsFiles, SWT.BORDER);
		textMetamodel.setBounds(143, 75, 300, 21);
		textMetamodel.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				checkIfCompleted();
			}
		});
		
		Label lblMetamodel = new Label(groupEmotionsFiles, SWT.NONE);
		lblMetamodel.setBounds(71, 78, 66, 15);
		lblMetamodel.setText("Metamodel:");
		
		Button btnMetamodel = new Button(groupEmotionsFiles, SWT.NONE);
		btnMetamodel.setBounds(449, 73, 54, 25);
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
					checkIfCompleted();
				}
			} 
		});
		
		/*
		 * Palladio files
		 */
		Group grpPalladioFiles = new Group(composite, SWT.NONE);
		grpPalladioFiles.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		grpPalladioFiles.setText("Palladio files");
		grpPalladioFiles.setBounds(20, 162, 513, 221);
		
		Label lblUsageModel = new Label(grpPalladioFiles, SWT.NONE);
		lblUsageModel.setBounds(61, 30, 76, 15);
		lblUsageModel.setText("Usage Model:");
		
		textUsageModel = new Text(grpPalladioFiles, SWT.BORDER);
		textUsageModel.setBounds(143, 23, 300, 25);
		textUsageModel.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				checkIfCompleted();
			}
		});
		
		Button btnUsageModel = new Button(grpPalladioFiles, SWT.NONE);
		btnUsageModel.setBounds(449, 23, 54, 25);
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
					checkIfCompleted();
				}
			} 
		});
		
		Label lblRepository = new Label(grpPalladioFiles, SWT.NONE);
		lblRepository.setBounds(38, 69, 99, 15);
		lblRepository.setText("Repository Model:");
		
		textRepositoryModel = new Text(grpPalladioFiles, SWT.BORDER);
		textRepositoryModel.setBounds(143, 62, 300, 25);
		textRepositoryModel.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				checkIfCompleted();
			}
		});
		
		Button btnRepositoryModel = new Button(grpPalladioFiles, SWT.NONE);
		btnRepositoryModel.setBounds(449, 62, 54, 25);
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
					checkIfCompleted();
				}
			} 
		});
		
		Label lblSystemModel = new Label(grpPalladioFiles, SWT.NONE);
		lblSystemModel.setText("System Model:");
		lblSystemModel.setBounds(55, 105, 82, 15);
		
		textSystemModel = new Text(grpPalladioFiles, SWT.BORDER);
		textSystemModel.setBounds(143, 101, 300, 25);
		textSystemModel.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				checkIfCompleted();
			}
		});
		
		Button btnSystemModel = new Button(grpPalladioFiles, SWT.NONE);
		btnSystemModel.setText("Browse");
		btnSystemModel.setBounds(449, 101, 54, 25);
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
					checkIfCompleted();
				}
			} 
		});
		
		Label lblAllocationModel = new Label(grpPalladioFiles, SWT.NONE);
		lblAllocationModel.setText("Allocation Model:");
		lblAllocationModel.setBounds(38, 145, 99, 15);
		
		textAllocationModel = new Text(grpPalladioFiles, SWT.BORDER);
		textAllocationModel.setBounds(143, 140, 300, 25);
		textAllocationModel.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				checkIfCompleted();
			}
		});
		
		Button btnAllocationModel = new Button(grpPalladioFiles, SWT.NONE);
		btnAllocationModel.setText("Browse");
		btnAllocationModel.setBounds(449, 140, 54, 25);
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
					checkIfCompleted();
				}
			} 
		});
		
		
		Label lblResourceenvironmentModel = new Label(grpPalladioFiles, SWT.NONE);
		lblResourceenvironmentModel.setText("Resource Env. Model:");
		lblResourceenvironmentModel.setBounds(20, 182, 117, 15);
		
		textResourceEnvModel = new Text(grpPalladioFiles, SWT.BORDER);
		textResourceEnvModel.setBounds(143, 179, 300, 25);
		textResourceEnvModel.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				checkIfCompleted();
			}
		});
		
		Button btnResourceEnvModel = new Button(grpPalladioFiles, SWT.NONE);
		btnResourceEnvModel.setText("Browse");
		btnResourceEnvModel.setBounds(449, 179, 54, 25);
		
		/*
		 * Simulation options
		 */
		Group grpSimulationOptions = new Group(composite, SWT.NONE);
		grpSimulationOptions.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		grpSimulationOptions.setText("Simulation options");
		grpSimulationOptions.setBounds(20, 396, 513, 95);
		
		Label lblSystemTimeLimit = new Label(grpSimulationOptions, SWT.NONE);
		lblSystemTimeLimit.setBounds(39, 32, 103, 15);
		lblSystemTimeLimit.setText("System time limit:");
		
		Label lblemptyForInfinite = new Label(grpSimulationOptions, SWT.NONE);
		lblemptyForInfinite.setFont(SWTResourceManager.getFont("Segoe UI", 7, SWT.NORMAL));
		lblemptyForInfinite.setBounds(39, 50, 77, 15);
		lblemptyForInfinite.setText("(empty for infinite)");
		
		textTimeLimit = new Text(grpSimulationOptions, SWT.BORDER);
		textTimeLimit.setBounds(143, 28, 76, 21);
		
		Button btnPrintAppliedMtes = new Button(grpSimulationOptions, SWT.CHECK);
		btnPrintAppliedMtes.setBounds(321, 50, 171, 16);
		btnPrintAppliedMtes.setText("Print applied mte's and rules");
		
		Button btnShowAdvisories = new Button(grpSimulationOptions, SWT.CHECK);
		btnShowAdvisories.setBounds(321, 69, 145, 16);
		btnShowAdvisories.setText("Show Maude advisories");
		
		Label lblAdvancedOptions = new Label(grpSimulationOptions, SWT.NONE);
		lblAdvancedOptions.setBounds(321, 20, 103, 15);
		lblAdvancedOptions.setText("Advanced options");
		
		Label lblSeparatorAdvanced = new Label(grpSimulationOptions, SWT.SEPARATOR | SWT.HORIZONTAL);
		lblSeparatorAdvanced.setBounds(321, 40, 100, 2);
		
		Group grpOutputOptions = new Group(composite, SWT.NONE);
		grpOutputOptions.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		grpOutputOptions.setText("Output options");
		grpOutputOptions.setBounds(20, 504, 513, 82);
		
		Label lblOutputFolder = new Label(grpOutputOptions, SWT.NONE);
		lblOutputFolder.setBounds(35, 42, 82, 15);
		lblOutputFolder.setText("Output folder:");
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
					checkIfCompleted();
				}
			} 
		});
				
		return composite;
	}
	
	@Override
	protected Control createContents(Composite parent) {
		Control control = super.createContents(parent);
		checkIfCompleted();
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
}
