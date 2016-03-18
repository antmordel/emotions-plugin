/**
 * @copyright Antonio Moreno-Delgado <i>amoreno@lcc.uma.es</i>
 * @date July 31th 2014
 * 
 * 
 *  This file is part of e-Motions. It has been generated with Xtend.
 *
 *  e-Motions is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  e-Motions is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with e-Motions.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.uma.lcc.e_motions;

import java.io.IOException;
import java.text.DecimalFormat;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import es.uma.lcc.e_motions.codegeneration.Model2TextMaude;
import es.uma.lcc.e_motions.common.FileManager;
import es.uma.lcc.e_motions.common.Printer;
import es.uma.lcc.e_motions.maudeprocess.MaudeProcess;


public class MaudeInfJob extends EMotionsJob {

	public MaudeInfJob(String name) {
		super(name);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		IStatus res = Status.OK_STATUS;

		printSelectedFiles();

		Printer _p = Printer.getDefault();
		/* have all files been set in? */
		FileManager _fm = FileManager.getDefault();
		if ((_fm.getBehavior() != null) && (_fm.getMetamodel() != null) && (_fm.getFolderResultName() != null)) {

			/* Create folder that stores the code */
			try {
				_fm.createFolderResult();
			} catch (CoreException e) {
				_p.println("[error] " + e.getMessage());
			}
			_p.println("Folder <" + _fm.getFolderResult().getName() + "> created.");

			String initialModel = M2MTransformations();
			res = oclValidation(res);
			codeGeneration();
			copyEMotionsInfrastructure();
			createInitialModel();
			createTickRuleModule(initialModel);
		}
		return res;
	}

	private void copyEMotionsInfrastructure() {
		FileManager _fm = FileManager.getDefault();
		Printer _p = Printer.getDefault();

		IFile aux;
		long startTime = System.currentTimeMillis();
		try {
			aux = _fm.createFileInMaudeFolder("mOdCL.maude");
			_fm.copyFile(aux, MaudeProcess.class.getResource("mOdCL.maude"));
			_p.println("File <mOdCL.maude> copied.");

			aux = _fm.createFileInMaudeFolder("MGDefinitions.maude");
			_fm.copyFile(aux, MaudeProcess.class.getResource("MGDefinitions.maude"));
			_p.println("File <MGDefinitions.maude> copied.");

			aux = _fm.createFileInMaudeFolder("EcoreMM.maude");
			_fm.copyFile(aux, MaudeProcess.class.getResource("EcoreMM.maude"));
			_p.println("File <EcoreMM.maude> copied.");

			aux = _fm.createFileInMaudeFolder("MGRealTimeMaude24.maude");
			_fm.copyFile(aux, MaudeProcess.class.getResource("MGRealTimeMaude24.maude"));
			_p.println("File <MGRealTimeMaude24.maude> copied.");

			aux = _fm.createFileInMaudeFolder("e-Motions.maude");
			_fm.copyFile(aux, MaudeProcess.class.getResource("e-Motions.maude"));
			_p.println("File <e-Motions.maude> copied.");
		} catch (CoreException | IOException e) {
			_p.println("[error] " + e.getMessage());
		}
		long endTime = System.currentTimeMillis();
		DecimalFormat df = new DecimalFormat("#0.000");
		_p.println("e-Motions infrastructure copied in " + df.format((endTime - startTime) / 1000.0) + " seconds.");

	}

	@Override
	protected void codeGeneration() {
		long startTime = System.currentTimeMillis();
		/* BehaviorMaudeModel 2 MaudeCode */
		IFile behaviorCode = null;
		try {
			behaviorCode = _fm.createBehaviorCodeInMaudeFolder();
			Model2TextMaude.generate(_fm.getBehaviorMaude().toOSString(), behaviorCode.getLocation().toOSString());
		} catch (CoreException e) {
			_p.println("[ERROR] Error in BehaviorMaudeModel2Code transformation process.\n" + e.getMessage());
		}

		/* MetamodelMaudeModel 2 MaudeCode */
		IFile metamodelCode = null;
		try {
			metamodelCode = _fm.createMetamodelCodeInMaudeFolder();
			Model2TextMaude.generate(_fm.getMetamodelMaude().toOSString(), metamodelCode.getLocation().toOSString());
		} catch (CoreException e) {
			_p.println("[ERROR] Error in MetamodelMaudeModel2Code transformation process.\n" + e.getMessage());
		}

		/* InitialMaudeModel 2 MaudeCode */
		if (_fm.getInitialModel() != null) {
			IFile initialModelCode = null;
			try {
				initialModelCode = _fm.createInitialModelCodeInMaudeFolder();
				_p.println("Initial Maude Code saved in " + initialModelCode);
				Model2TextMaude.generate(_fm.getInitialModelMaude().toOSString(), initialModelCode.getLocation().toOSString());
			} catch (CoreException e) {
				_p.println("[ERROR] Error in Model2MaudeCode transformation process.\n" + e.getMessage());
			}
		}
		long endTime = System.currentTimeMillis();
		DecimalFormat df = new DecimalFormat("#0.000");
		_p.println("Model2Text transformations in " + df.format((endTime - startTime) / 1000.0) + " seconds.");
	}
}
