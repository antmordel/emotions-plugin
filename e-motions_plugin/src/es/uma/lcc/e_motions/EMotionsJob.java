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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.Date;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.m2m.atl.core.ATLCoreException;

import es.uma.lcc.e_motions.codegeneration.Model2Maude;
import es.uma.lcc.e_motions.codegeneration.EcoreMM2Maude;
import es.uma.lcc.e_motions.codegeneration.Behavior2Maude;
import es.uma.lcc.e_motions.codegeneration.ModuleGenerators;
import es.uma.lcc.e_motions.common.ExecutionData;
import es.uma.lcc.e_motions.common.FileManager;
import es.uma.lcc.e_motions.common.Printer;
import es.uma.lcc.e_motions.maudeprocess.exceptions.MaudelingException;

public abstract class EMotionsJob extends Job {

	protected DecimalFormat _DF;
	protected Printer _p;
	protected FileManager _fm;
	private static EMotionsJob self;

	public EMotionsJob(String name) {
		super(name);

		this._DF = new DecimalFormat("#0.000");
		this._p = Printer.getDefault();
		this._fm = FileManager.getDefault();
		EMotionsJob.self = this;
	}

	protected String createInitialModel() {
		IFile aux;
		String initialModel = null;
		if (_fm.getInitialModel() == null) {
			// empty initial model, if it is not empty, it has been generated
			// beforehand
			initialModel = "EMPTYMODEL";
			String mmName = null, moduleModelName = null;
			try {
				mmName = _fm.extractMetamodelMaudeName();
				moduleModelName = _fm.extractModuleMMName();
			} catch (IOException e) {
				_p.println("[error]" + e.getMessage());
			}

			String model = ModuleGenerators.emptyModel(initialModel, mmName,
					moduleModelName);

			try {
				long startTime = System.currentTimeMillis();
				if (this instanceof MaudeInfJob) {
					aux = _fm.createFileInMaudeFolder(FileManager.EMPTY_MODEL);
				} else {
					aux = _fm.createFileInTmpFolder(FileManager.EMPTY_MODEL);
				}

				if (aux.exists()) {
					aux.setContents(new ByteArrayInputStream(model.getBytes()),
							true, true, null);
				} else {
					aux.create(new ByteArrayInputStream(model.getBytes()),
							true, null);
				}
				long endTime = System.currentTimeMillis();
				_p.println("EmptyModel created in "
						+ _DF.format((endTime - startTime) / 1000.0)
						+ " seconds.");
			} catch (CoreException e) {
				_p.println("[error]" + e.getMessage());
			}
		}
		return initialModel;
	}

	protected void printSelectedFiles() {
		Printer _p = Printer.getDefault();

		/* Print to e-Motions console date and modules to be used */
		Date fecha = new Date();
		_p.println(fecha.toString());

		_p.println("Files selected:");
		printModulesSelected();
	}

	protected String M2MTransformations() {
		FileManager _fm = FileManager.getDefault();
		Printer _p = Printer.getDefault();

		/* Beh2Maude */
		try {
			Behavior2Maude.behOcl2Maude(_fm.getBehaviorString(),
					_fm.getMetamodelString());
		} catch (MaudelingException | URISyntaxException | CoreException
				| ATLCoreException | IOException e) {
			_p.println("[ERROR] Error in Behavior2Maude transformation process.\n"
					+ e.getMessage());
		}
		/* end BehOcl2Maude */

		/* EcoreMM2Maude */
		try {
			EcoreMM2Maude.ecore2Maude(_fm.getMetamodelString());
		} catch (ATLCoreException | IOException e1) {
			_p.println("[ERROR] Error in EcoreMM2Maude transformation process.\n" + e1.getMessage());
		}

		/* end EcoreMM2Maude */

		/* InitialModel2Maude */
		String NAME_INITIAL_MODULE = "EMPTYMODEL";
		if (_fm.getInitialModel() != null) {
			try {
				NAME_INITIAL_MODULE = Model2Maude.model2Maude(
						_fm.getInitialModel(), _fm.getMetamodel());
			} catch (Exception e) {
				_p.println("[ERROR] Error in Model2Maude transformation process.\n"
						+ e.getMessage());
			}
		}
		/* end InitialModel2Maude */

		return NAME_INITIAL_MODULE;
	}

	protected IStatus oclValidation(IStatus current) {
		IStatus res = current;
		/* OCL Validation */
		if (ExecutionData.getDefault().isValidateOCL()) {
			res = validateOCL();
		}
		/* end OCL Validation */
		return res;
	}

	protected void printModulesSelected() {
		final String noSpec = "No specified.";

		FileManager _fm = FileManager.getDefault();
		Printer _p = Printer.getDefault();

		_p.print("\tBehavior model: ");
		if (_fm.getBehavior() != null) {
			_p.println(_fm.getBehavior().getFullPath().toOSString());
		} else {
			_p.println(noSpec);
		}

		_p.print("\tMetamodel: ");
		if (_fm.getMetamodel() != null) {
			_p.println(_fm.getMetamodel().getFullPath().toOSString());
		} else {
			_p.println(noSpec);
		}

		_p.print("\tInitial model: ");
		if (_fm.getInitialModel() != null) {
			_p.println(_fm.getInitialModel().getFullPath().toOSString());
		} else {
			_p.println(noSpec);
		}

		if (this instanceof MaudeInfJob) {
			_p.print("\tResulting folder: ");
			if (_fm.getFolderResultName() != null) {
				_p.println(_fm.getFolderResultName());
			} else {
				_p.println(noSpec);
			}
		}
	}

	protected abstract void codeGeneration();

	protected IStatus validateOCL() {
		IStatus res = Status.OK_STATUS;
		// OclBehaviorValidationEngine validationEngine = new
		// OclBehaviorValidationEngine(FileManager.getDefault().getBehavior().getLocation()
		// .toOSString(),
		// FileManager.getDefault().getMetamodel().getLocation().toOSString());
		// if (!validationEngine.validateOcl()) {
		// OCLUtil.openOCLErrorsView(validationEngine);
		// res = Status.CANCEL_STATUS;
		// } else {
		// OCLUtil.oclWithoutErrors();
		// res = Status.OK_STATUS;
		// }
		return res;
	}

	protected void createTickRuleModule(String initialModel) {
		String behModuleName = null;
		try {
			behModuleName = _fm.extractBehaviorModuleName();
		} catch (IOException e) {
			_p.println("[error]" + e.getMessage());
		}

		long startTime = System.currentTimeMillis();
		IFile aux;
		try {
			String generatedTickModule = null;
			if (this instanceof MaudeInfJob) {
				generatedTickModule = ModuleGenerators.tickRuleFile(
						initialModel, behModuleName, _fm.getInitialModel(),
						_fm.extractInitModelOp());
				aux = _fm
						.createFileInMaudeFolder(FileManager.TICKMODULE_FILENAME);
			} else {
				// instanceof SimulateJob
				generatedTickModule = ModuleGenerators.tickRuleFile(
						initialModel, behModuleName, _fm.getInitialModel(),
						null);
				aux = _fm
						.createFileInTmpFolder(FileManager.TICKMODULE_FILENAME);
			}
			if (aux.exists()) {
				aux.setContents(
						new ByteArrayInputStream(generatedTickModule.getBytes()),
						true, true, null);
			} else {
				aux.create(
						new ByteArrayInputStream(generatedTickModule.getBytes()),
						true, null);
			}
			_fm.setTickModule(aux);
		} catch (CoreException | IOException e) {
			_p.println("[error creating TickRule module] " + e.getMessage());
		}
		long endTime = System.currentTimeMillis();
		DecimalFormat df = new DecimalFormat("#0.000");
		_p.println("TickRule module created in "
				+ df.format((endTime - startTime) / 1000.0) + " seconds.");
	}

	public static EMotionsJob getSelf() {
		return self;
	}
}
