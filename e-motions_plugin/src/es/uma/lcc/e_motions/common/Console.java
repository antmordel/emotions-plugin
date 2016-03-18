package es.uma.lcc.e_motions.common;


import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.*;
import org.eclipse.ui.part.ViewPart;
/**
* Console for JavaCC
* @see ViewPart
*/
public class Console extends ViewPart {
public static final String CONSOLE_ID = "javaccsupport.console";
public Document document = null;
public TextViewer viewer = null;
/**
* Creates the SWT controls for this workbench part.
* @see ViewPart#createPartControl
*/
public void createPartControl(Composite parent) {
  viewer = new TextViewer(parent, SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL);
  GridData viewerData = new GridData(GridData.FILL_BOTH);
  viewer.getControl().setLayoutData(viewerData);
  viewer.setEditable(false);
  document = new Document();
  viewer.setDocument(document);
}
/**
* Prints the string to the console
* Clears the last text before printing the new one.
*/
public static void print(String output) {
  try {
  IWorkbench workbench = PlatformUI.getWorkbench();
  IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
  IWorkbenchPage page = windows[0].getActivePage();
  Console console = (Console) page.findView(Console.CONSOLE_ID);
  if (console == null) {
    page.showView(Console.CONSOLE_ID);
    console = (Console) page.findView(Console.CONSOLE_ID);
  }
  console.document.set(output);
  console.viewer.setTopIndex(console.document.getNumberOfLines());
  } catch (PartInitException e) {}
}
/**
* @see org.eclipse.ui.IWorkbenchPart#setFocus()
*/
public void setFocus() {}
}

