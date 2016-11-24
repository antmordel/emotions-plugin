package es.uma.lcc.e_motions.dialogs;

import org.eclipse.core.internal.registry.osgi.Activator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.wb.swt.SWTResourceManager;

import es.uma.lcc.e_motions.runningdata.EmotionsFileManager;

/**
 * 
 * @author Antonio Moreno-Delgado <amoreno@lcc.uma.es>
 *
 */
@SuppressWarnings("restriction")
public class EmotionsDialog extends Dialog {

	protected Shell parentShell;

	protected EmotionsFileManager fm;
	protected Text textBehavior;
	protected Text textMetamodel;
	private Text textTimeLimit;
	private Text textOutputFolder;

	protected EmotionsDialog(Shell parentShell) {
		super(parentShell);
		this.parentShell = parentShell;
	}

	protected EmotionsDialog(Shell parentShell, EmotionsFileManager fm) {
		this(parentShell);
		this.fm = fm;
	}

	protected void createEmotionsFilesGroup(Composite composite) {
		Group groupEmotionsFiles = new Group(composite, SWT.NONE);
		groupEmotionsFiles.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		groupEmotionsFiles.setText("e-Motions files");
		groupEmotionsFiles.setBounds(20, 34, 560, 130);

		Label lblEmotionsBehavior = new Label(groupEmotionsFiles, SWT.NONE);
		lblEmotionsBehavior.setBounds(20, 40, 120, 15);
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
				ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(parentShell,
						new WorkbenchLabelProvider(), new BaseWorkbenchContentProvider());
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
				ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(parentShell,
						new WorkbenchLabelProvider(), new BaseWorkbenchContentProvider());
				dialog.setTitle("Metamodel");
				dialog.setMessage("Select *.ecore file");
				dialog.setInput(ResourcesPlugin.getWorkspace());
				dialog.addFilter(new FilePatternFilter("ecore"));
				dialog.open();

				IFile resultMetamodel = (IFile) dialog.getResult()[0];
				if (dialog.getReturnCode() == Window.OK) {
					textMetamodel.setText(resultMetamodel.getFullPath().toOSString());
					fm.setMetamodel(resultMetamodel);
					// TODO checkIfCompleted();
				}
			}
		});
	}

	protected void createSimulationGroup(Composite composite, int offset) {
		Group grpSimulationOptions = new Group(composite, SWT.NONE);
		grpSimulationOptions.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		grpSimulationOptions.setText("Simulation options");
		grpSimulationOptions.setBounds(20, offset, 560, 110);

		Label lblSystemTimeLimit = new Label(grpSimulationOptions, SWT.NONE);
		lblSystemTimeLimit.setBounds(39, 32, 103, 15);
		lblSystemTimeLimit.setText("System time limit:");

		// Label lblemptyForInfinite = new Label(grpSimulationOptions,
		// SWT.NONE);
		// lblemptyForInfinite.setFont(SWTResourceManager.getFont("Segoe UI", 7,
		// SWT.NORMAL));
		// lblemptyForInfinite.setBounds(39, 50, 77, 15);
		// lblemptyForInfinite.setText("(empty for infinite)");

		textTimeLimit = new Text(grpSimulationOptions, SWT.BORDER);
		textTimeLimit.setBounds(143, 28, 76, 21);
		textTimeLimit.setText(String.valueOf(fm.getLimitTime()));
		textTimeLimit.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				try {
					fm.setLimitTime(Integer.parseInt(textTimeLimit.getText()));
				} catch (NumberFormatException nfe) {
				}
			}
		});

		final Button btnInfiniteSimulation = new Button(grpSimulationOptions, SWT.CHECK);
		btnInfiniteSimulation.setBounds(143, 55, 100, 16);
		btnInfiniteSimulation.setFont(SWTResourceManager.getFont("Segoe UI", 7, SWT.NORMAL));
		btnInfiniteSimulation.setText("Infinite simulation");
		btnInfiniteSimulation.setSelection(fm.isInfiniteLimitTime());
		btnInfiniteSimulation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fm.setInfiniteLimitTime(btnInfiniteSimulation.getSelection());
				textTimeLimit.setEnabled(!fm.isInfiniteLimitTime());
				if (fm.isInfiniteLimitTime()) {
					textTimeLimit.setText("");
				} else {
					textTimeLimit.setText(String.valueOf(fm.getLimitTime()));
				}
			}
		});

		final Button btnPrintAppliedMtes = new Button(grpSimulationOptions, SWT.CHECK);
		btnPrintAppliedMtes.setBounds(321, 50, 200, 16);
		btnPrintAppliedMtes.setText("Print applied mte's and rules");
		btnPrintAppliedMtes.setSelection(fm.isAppliedRules());
		btnPrintAppliedMtes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fm.setAppliedRules(btnPrintAppliedMtes.getSelection());
			}
		});

		final Button btnShowAdvisories = new Button(grpSimulationOptions, SWT.CHECK);
		btnShowAdvisories.setBounds(321, 69, 165, 16);
		btnShowAdvisories.setSelection(fm.isShowAdvisories());
		btnShowAdvisories.setText("Show Maude advisories");
		btnShowAdvisories.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fm.setShowAdvisories(btnShowAdvisories.getSelection());
			}
		});

		Label lblAdvancedOptions = new Label(grpSimulationOptions, SWT.NONE);
		lblAdvancedOptions.setBounds(321, 20, 103, 15);
		lblAdvancedOptions.setText("Advanced options");

		Label lblSeparatorAdvanced = new Label(grpSimulationOptions, SWT.SEPARATOR | SWT.HORIZONTAL);
		lblSeparatorAdvanced.setBounds(321, 40, 100, 2);
	}

	protected void createOutputGroup(Composite composite, int offset) {
		Group grpOutputOptions = new Group(composite, SWT.NONE);
		grpOutputOptions.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		grpOutputOptions.setText("Output options");
		grpOutputOptions.setBounds(20, offset, 560, 90);

		Label lblOutputFolder = new Label(grpOutputOptions, SWT.NONE);
		lblOutputFolder.setBounds(35, 42, 82, 15);
		lblOutputFolder.setText("Output folder:");

		textOutputFolder = new Text(grpOutputOptions, SWT.BORDER);
		textOutputFolder.setBounds(143, 36, 300, 25);
		textOutputFolder.setEditable(false);
		if (fm.getFolderResult() != null) {
			textOutputFolder.setText(fm.getFolderResult().getFullPath().toPortableString());
		}

		Button btnOutputFolder = new Button(grpOutputOptions, SWT.NONE);
		btnOutputFolder.setBounds(449, 36, 80, 25);
		btnOutputFolder.setText("Browse");
		btnOutputFolder.setEnabled(true);
		btnOutputFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// DirectoryDialog dialog = new DirectoryDialog(parentShell);
				// dialog.setFilterPath(ResourcesPlugin.getWorkspace().getRoot().getLocation().toString());
				// String resultingDir = dialog.open();
				// if (resultingDir != null) {
				// textOutputFolder.setText(resultingDir);
				// fm.setFolderOutputPathString(textOutputFolder.getText());
				// }
				ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(parentShell,
						new WorkbenchLabelProvider(), new BaseWorkbenchContentProvider());
				dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
				dialog.setTitle("Select output folder");
				dialog.setValidator(new ISelectionStatusValidator() {

					@Override
					public IStatus validate(Object[] selection) {
						return selection.length == 1 && selection[0] instanceof IFolder
								? new Status(IStatus.OK, Activator.getContext().getBundle().getSymbolicName(), "Ok :-)")
								: new Status(IStatus.ERROR, Activator.getContext().getBundle().getSymbolicName(),
										"Folder not selected.");
					}
				});
				if (dialog.open() == Dialog.OK) {
					fm.setFolderResult((IFolder) dialog.getFirstResult());
					textOutputFolder.setText(fm.getFolderResult().getFullPath().toPortableString());
				}
				// TODO checkIfCompleted();
			}
		});
	}
}
