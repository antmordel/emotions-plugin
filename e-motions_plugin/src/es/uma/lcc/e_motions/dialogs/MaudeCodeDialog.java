package es.uma.lcc.e_motions.dialogs;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.jface.dialogs.Dialog;


public class MaudeCodeDialog extends Dialog {
	protected IAdaptable input;
	private Shell shell;
	protected static Text textnombremodelo2, textnombremodelo, textAdditionalModels;
	protected static Button mod2urlbutton, mod2uributton, mmod3uributton, clearAddMM, clearInitMM;
	protected static boolean issetEcoreMM = false;
	static Label UMLWarningLabel;
	static Text textnombremmodelo3;

	public MaudeCodeDialog(Shell parentShell) {
		super(parentShell);
		shell = parentShell;
		//configureShell(shell);
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Maude Infrastructure Code Generation");
	}

	protected Control createDialogArea(Composite parent) {
		
		//final FileManager _fm = FileManager.getDefault();
		Composite composite = (Composite) super.createDialogArea(parent);

		GridLayout layout = new GridLayout();
		layout.numColumns = 5;
		composite.setLayout(layout);

		Label[] vacios = new Label[40];
		int i = 0;
		
		/*
		 * *******************************************************
		 * *  Select Behavior model
		 *
		 */
		vacios[i] = new Label(composite, SWT.HORIZONTAL); i++;
		// 1x2
		vacios[i] = new Label(composite, SWT.HORIZONTAL); i++;
		// 1x3
		vacios[i] = new Label(composite, SWT.HORIZONTAL); i++;
		// 1x4
		vacios[i] = new Label(composite, SWT.HORIZONTAL); i++;
		// 1x5
		vacios[i] = new Label(composite, SWT.HORIZONTAL); i++;

		// 2x1
		// Label Select modelo file:
		Label labelModeloBehavior = new Label(composite, SWT.HORIZONTAL);
		labelModeloBehavior.setText("Behavior model *:");

		GridData gridmodelo1 = new GridData();
		gridmodelo1.horizontalAlignment = GridData.END;
		gridmodelo1.horizontalSpan = 1;

		labelModeloBehavior.setLayoutData(gridmodelo1);

		// 2x2 - 2x3
		// Cuadro de texto del modelo
		final Text textModelBehavior = new Text(composite, SWT.SINGLE);
		textModelBehavior.setText("");
		textModelBehavior.setEditable(false);
		final Color blanco = new Color(shell.getDisplay(), new RGB(255, 255, 255));
		textModelBehavior.setBackground(blanco);

		GridData _gridTextModelBehavior = new GridData();
		_gridTextModelBehavior.horizontalAlignment = GridData.END;
		_gridTextModelBehavior.horizontalSpan = 2;
		_gridTextModelBehavior.widthHint = 350;
		textModelBehavior.setLayoutData(_gridTextModelBehavior);
		//if (_fm.getBehavior() != null) {
		//	textModelBehavior.setText(_fm.getBehavior().getFullPath().toOSString());
		//}

		// 2x4
		// Button to define the behavior model by url
		final Button behModelButton = new Button(composite, SWT.PUSH);
		behModelButton.setText("Browse");
		behModelButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {

				ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(shell, new WorkbenchLabelProvider(),
						new BaseWorkbenchContentProvider());
				dialog.setTitle("Model");
				dialog.setMessage("Select model file");
				dialog.setInput(ResourcesPlugin.getWorkspace());
				FilePatternFilter filter = new FilePatternFilter("behavior");
				dialog.addFilter(filter);
				dialog.open();

				IFile result = (IFile) dialog.getResult()[0];
				if (dialog.getReturnCode() == Window.OK) {
					//_fm.setBehavior(result);
					//textModelBehavior.setText(_fm.getBehavior().getFullPath().toOSString());
				}
			}
		});
		// 3x4
		vacios[i] = new Label(composite, SWT.HORIZONTAL);
		i++;

		/*
		 * *******************************************************
		 * *  Select Metamodel
		 *
		 */

		// 2x1
		Label _labelMM = new Label(composite, SWT.HORIZONTAL);
		_labelMM.setText("Metamodel *:");

		GridData gridmm = new GridData();
		gridmm.horizontalAlignment = GridData.END;
		gridmm.horizontalSpan = 1;

		_labelMM.setLayoutData(gridmm);

		// 2x2 - 2x3
		// Cuadro de texto del modelo
		final Text textMM = new Text(composite, SWT.SINGLE);
		textMM.setText("");
		textMM.setEditable(false);
		final Color _blancomm = new Color(shell.getDisplay(), new RGB(255, 255, 255));
		textMM.setBackground(_blancomm);

		GridData gridtextmodmm = new GridData();
		gridtextmodmm.horizontalAlignment = GridData.CENTER;
		gridtextmodmm.horizontalSpan = 2;
		gridtextmodmm.widthHint = 350;
		textMM.setLayoutData(gridtextmodmm);
//		if (_fm.getMetamodel() != null) {
//			textMM.setText(_fm.getMetamodel().getFullPath().toOSString());
//		}

		// 2x4
		// Bot�n para definir el modelo por URL
		final Button mod1urlbutton1mm = new Button(composite, SWT.PUSH);
		mod1urlbutton1mm.setText("Browse");

		mod1urlbutton1mm.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {

				ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(shell, new WorkbenchLabelProvider(),
						new BaseWorkbenchContentProvider());
				dialog.setTitle("Model");
				dialog.setMessage("Select model file");
				dialog.setInput(ResourcesPlugin.getWorkspace());
				FilePatternFilter filter = new FilePatternFilter("ecore");
				dialog.addFilter(filter);
				dialog.open();

				IFile result = (IFile) dialog.getResult()[0];
				if (dialog.getReturnCode() == Window.OK) {
//					_fm.setMetamodel(result);
//					textMM.setText(_fm.getMetamodel().getFullPath().toOSString());
				}
			}
		});

		// 3x4
		vacios[i] = new Label(composite, SWT.HORIZONTAL);
		i++;

		/*
		 * *******************************************************
		 * *  Select Initial Model
		 *
		 */
		// 8x1
		// Label Select metamodel file:
		Label initialModelLabel = new Label(composite, SWT.HORIZONTAL);
		initialModelLabel.setText("Initial model:");
		GridData gridmmodelo3 = new GridData();
		gridmmodelo3.horizontalAlignment = GridData.END;
		gridmmodelo3.horizontalSpan = 1;
		initialModelLabel.setLayoutData(gridmmodelo3);

		// 8x2 - 8x3
		// Text field initial model
		final Text initialModelText = new Text(composite, SWT.SINGLE);
		initialModelText.setEditable(false);
		initialModelText.setBackground(_blancomm);
		GridData gridtextmmod3 = new GridData();
		gridtextmmod3.horizontalAlignment = GridData.CENTER;
		gridtextmmod3.horizontalSpan = 2;
		gridtextmmod3.widthHint = 350;
		initialModelText.setLayoutData(gridtextmmod3);
//		if (_fm.getInitialModel() != null) {
//			initialModelText.setText(_fm.getInitialModel().getFullPath().toOSString());
//		}

		// 8x4
		// Bot�n para definir el modelo inicial por URL
		Button modurlbutton3 = new Button(composite, SWT.PUSH);
		modurlbutton3.setText("Browse");
		modurlbutton3.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(shell, new WorkbenchLabelProvider(),
						new BaseWorkbenchContentProvider());
				dialog.setTitle("Initial Model");
				dialog.setMessage("Select metamodel file");
				dialog.setInput(ResourcesPlugin.getWorkspace());
				dialog.open(); 

				if (dialog.getReturnCode() == Window.OK) {
					IFile result = (IFile) dialog.getResult()[0];
//					_fm.setInitialModel(result);
					initialModelText.setText(result.getFullPath().toOSString());
				}
			}
		});

		// 5x4
		// Clear the initial model
		clearInitMM = new Button(composite, SWT.PUSH);
		clearInitMM.setText(" Clear ");
		clearInitMM.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
//				_fm.setInitialModel(null);
				initialModelText.setText("");
			}
		});
		
		/*
		 * *******************************************************
		 * *  Set limit time 
		 * 
		 */
		
		// 11x2
		Label labelTimeLimit = new Label(composite, SWT.FILL);
		labelTimeLimit.setText("Time limit: ");
		GridData gridTimeLimit = new GridData();
		gridTimeLimit.horizontalAlignment = GridData.END;
		gridTimeLimit.horizontalSpan = 1;
		labelTimeLimit.setLayoutData(gridTimeLimit);

		// 11x1
		final Text timelimit = new Text(composite, SWT.SINGLE);
		timelimit.setText("");
		timelimit.setEditable(true);
		timelimit.setBackground(_blancomm);
//		if (SimulateAction.timelimit != null) {
//			timelimit.setText(ExecutionData.getDefault().getTimeLimitString());
//		}

		GridData gridtimelimit = new GridData();
		gridtimelimit.horizontalAlignment = GridData.BEGINNING;
		gridtimelimit.horizontalSpan = 1;
		gridtimelimit.widthHint = 100;
		timelimit.setLayoutData(gridtimelimit);
		timelimit.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				String tl = timelimit.getText();
				tl = tl.trim();
//				if (tl.equals("")) {
//					ExecutionData.getDefault().setTimeLimit(-1);
//				} else {
//					ExecutionData.getDefault().setTimeLimit(Integer.valueOf(tl));
//				}
			}
		});

		// 12x4
		vacios[i] = new Label(composite, SWT.HORIZONTAL);
		i++;
		vacios[i] = new Label(composite, SWT.HORIZONTAL);
		i++;
		vacios[i] = new Label(composite, SWT.HORIZONTAL);
		i++;

		/*
		 * *******************************************************
		 * *  Set default elapse time
		 *
		 */

		// 11x2
		Label labeldefaulttime = new Label(composite, SWT.FILL);
		labeldefaulttime.setText("Default time elapse: ");
		GridData gridmmodelodt = new GridData();
		gridmmodelodt.horizontalAlignment = GridData.END;
		gridmmodelodt.horizontalSpan = 1;
		labeldefaulttime.setLayoutData(gridmmodelodt);

		// 11x1
		final Text defaulttime = new Text(composite, SWT.SINGLE);
		defaulttime.setText("");
		defaulttime.setEditable(true);
		defaulttime.setBackground(_blancomm);
//		if (SimulateAction.defaultTime != null) {
//			defaulttime.setText(ExecutionData.getDefault().getDefaultElapsetimeString());
//		}

		GridData gridDefaultTime = new GridData();
		gridDefaultTime.horizontalAlignment = GridData.BEGINNING;
		gridDefaultTime.horizontalSpan = 1;
		gridDefaultTime.widthHint = 100;
		defaulttime.setLayoutData(gridDefaultTime);
		defaulttime.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				String dt = defaulttime.getText();
				dt = dt.trim();
//				if (dt.equals("")) {
//					ExecutionData.getDefault().setDefaultElapseTime(-1);
//				} else {
//					ExecutionData.getDefault().setDefaultElapseTime(Integer.valueOf(dt));
//				}
			}
		});

		// 12x4
		vacios[i] = new Label(composite, SWT.HORIZONTAL);
		i++;
		vacios[i] = new Label(composite, SWT.HORIZONTAL);
		i++;
		vacios[i] = new Label(composite, SWT.HORIZONTAL);
		i++;

		/*
		 * *******************************************************
		 * *  Set resulting model
		 *
		 */

		// 12x1
		// Label Select metamodel file:
		Label nombremod5 = new Label(composite, SWT.HORIZONTAL);
		nombremod5.setText("Output Folder *:");
		GridData gridmmodelo5 = new GridData();
		gridmmodelo5.horizontalAlignment = GridData.END;
		gridmmodelo5.horizontalSpan = 1;
		nombremod5.setLayoutData(gridmmodelo5);

		// 12x2 - 12x3
		final Text resultingModelText = new Text(composite, SWT.SINGLE);
		resultingModelText.setEditable(true);
		resultingModelText.setBackground(_blancomm);
		GridData gridtextmmod5 = new GridData();
		gridtextmmod5.horizontalAlignment = GridData.CENTER;
		gridtextmmod5.horizontalSpan = 2;
		gridtextmmod5.widthHint = 350;
		resultingModelText.setLayoutData(gridtextmmod5);
//		if (_fm.getFolderResultName() != null) {
//			resultingModelText.setText(_fm.getFolderResultName());
//		}
//		resultingModelText.addListener(SWT.Modify, new Listener() {
//			public void handleEvent(Event event) {
//				_fm.setFolderResultName(resultingModelText.getText());
//			}
//		});

		// 12x4
		vacios[i] = new Label(composite, SWT.HORIZONTAL);
		i++;

		vacios[i] = new Label(composite, SWT.HORIZONTAL);
		i++;
		
		/*
		 * *******************************************************
		 * *  Last line
		 *
		 */
		// 13x1
		Label nombremod6 = new Label(composite, SWT.HORIZONTAL);
		nombremod6.setText("(*) = mandatory");
		GridData gridmmodelo6 = new GridData();
		gridmmodelo6.horizontalAlignment = GridData.END;
		gridmmodelo6.horizontalSpan = 1;
		nombremod6.setLayoutData(gridmmodelo6);

		vacios[i] = new Label(composite, SWT.HORIZONTAL);
		i++;
		vacios[i] = new Label(composite, SWT.HORIZONTAL);
		i++;
		vacios[i] = new Label(composite, SWT.HORIZONTAL);
		i++;
		vacios[i] = new Label(composite, SWT.HORIZONTAL);
		i++;
		
		
		vacios[i] = new Label(composite, SWT.HORIZONTAL);
		i++;

		final Button cb = new Button(composite, SWT.CHECK);
		cb.setText("Keep track of realized actions");
//		cb.setSelection(ExecutionData.getDefault().isTrackingActions());
		GridData gridcb = new GridData();
		gridcb.horizontalAlignment = GridData.BEGINNING;
		gridcb.horizontalSpan = 2;
		cb.setLayoutData(gridcb);
		cb.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
//				ExecutionData.getDefault().setTrackingActions(cb.getSelection());
			}
		});

		final Button validate = new Button(composite, SWT.CHECK);
		validate.setText("Validate OCL");
//		validate.setSelection(ExecutionData.getDefault().isValidateOCL());
		GridData gridValidate = new GridData();
		gridValidate.horizontalAlignment = GridData.BEGINNING;
		gridValidate.horizontalSpan = 2;
		validate.setLayoutData(gridValidate);
		validate.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
//				ExecutionData.getDefault().setValidateOCL(validate.getSelection());
			}
		});
		
		// new line
		vacios[i] = new Label(composite, SWT.HORIZONTAL);
		i++;
		final Button printAppliedRulesButton = new Button(composite, SWT.CHECK);
		printAppliedRulesButton.setText("Print applied rules");
//		printAppliedRulesButton.setSelection(ExecutionData.getDefault().isPrintAppliedRules());
		GridData gridPrintAppliedRulesLabel = new GridData();
		gridPrintAppliedRulesLabel.horizontalAlignment = GridData.BEGINNING;
		gridPrintAppliedRulesLabel.horizontalSpan = 2;
		printAppliedRulesButton.setLayoutData(gridPrintAppliedRulesLabel);
		printAppliedRulesButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
//				ExecutionData.getDefault().setPrintAppliedRules(printAppliedRulesButton.getSelection());
			}
		});
		
		
		return composite;
	}

	class FilePatternFilter extends ViewerFilter {
		String fileExtension;

		public FilePatternFilter(String ext) {
			super();
			fileExtension = ext;
		}

		public boolean select(Viewer viewer, Object parentElement, Object element) {
			IResource resource = null;
			if (element instanceof IResource) {
				resource = (IResource) element;
			} else if (element instanceof IAdaptable) {
				IAdaptable adaptable = (IAdaptable) element;
				resource = (IResource) adaptable.getAdapter(IResource.class);
			}
			if (resource != null && !resource.isDerived()) {
				if (resource.getType() != IResource.FILE)
					return true;
				String extension = resource.getFileExtension();
				if (extension == null)
					return true;
				if (extension.equals(fileExtension))
					return true;
			}
			return false;
		}
	}
}
