package es.uma.lcc.e_motions.jobs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.m2m.atl.core.ATLCoreException;

import es.uma.lcc.e_motions.MaudeInfJob;
import es.uma.lcc.e_motions.codegeneration.Behavior2Maude;
import es.uma.lcc.e_motions.codegeneration.EcoreMM2Maude;
import es.uma.lcc.e_motions.codegeneration.Model2Maude;
import es.uma.lcc.e_motions.codegeneration.Model2TextMaude;
import es.uma.lcc.e_motions.codegeneration.ModuleGenerators;
import es.uma.lcc.e_motions.common.FileManager;
import es.uma.lcc.e_motions.console.EmotionsConsole;
import es.uma.lcc.e_motions.maudeprocess.MaudeProcess;
import es.uma.lcc.e_motions.maudeprocess.exceptions.MaudelingException;
import es.uma.lcc.e_motions.runningdata.PalladioRunningInformation;
import es.uma.lcc.e_motions.transformations.model2model.PalladioFlattenInputModel;

/**
 * This class implements the flattening transformation for Palladio.
 * 
 * @author Antonio Moreno-Delgado <amoreno@lcc.uma.es>
 *
 */
public class OldPalladioJob extends Job {

	private EmotionsConsole console;
	private PalladioRunningInformation info;

	public OldPalladioJob(String name) {
		super(name);
		console = EmotionsConsole.getDefault();
		info = PalladioRunningInformation.getDefault();
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		long init;
		DecimalFormat df = new DecimalFormat("#0.000");

		/* Flattening Palladio models */
		init = System.currentTimeMillis();
		new PalladioFlattenInputModel().run();
		console.println("Flatten Palladio input model in " + df.format((System.currentTimeMillis() - init) / 1000.0)
				+ " seconds.");
		/* end Flattening Palladio models */

		/* Ecore 2 Maude model */
		try {
			init = System.currentTimeMillis();
			EcoreMM2Maude.ecore2MaudePalladio("file:///" + info.getMetamodel().getLocation().toOSString());
			console.println("Ecore to Maude M2M transformation finished in "
					+ df.format((System.currentTimeMillis() - init) / 1000.0) + " seconds.");
		} catch (ATLCoreException | IOException e1) {
			console.println("[ERROR] Error in EcoreMM2Maude transformation process.\n" + e1.getMessage());
		}
		/* end Ecore 2 Maude model */

		/* EcoreMaudeModel 2 MaudeCode */
		IFile metamodelCode = null;
		try {
			init = System.currentTimeMillis();
			metamodelCode = info.createMetamodelCodeInMaudeFolder();
			Model2TextMaude.generate(info.getMetamodelMaude().toOSString(), metamodelCode.getLocation().toOSString());
			console.println("Ecore to Maude M2T transformation finished in "
					+ df.format((System.currentTimeMillis() - init) / 1000.0) + " seconds.");
		} catch (CoreException e) {
			console.println("[ERROR] Error in MetamodelMaudeModel2Code transformation process.\n" + e.getMessage());
		}
		/* end EcoreMaudeModel 2 MaudeCode */

		/* Beh2Maude */
		try {
			init = System.currentTimeMillis();
			/*
			 * This snippet shoud be changed. It saves the output model in the
			 * FileManager, but not in the PalladioFileManager, but in the
			 * regular one. TODO reduce the cohesion of such class.
			 */
			Behavior2Maude.behOcl2MaudePalladio("file:///" + info.getBehaviorModel().getRawLocation().toOSString(),
					"file:///" + info.getMetamodel().getLocation().toOSString());
			console.println("Behavior to Maude M2M (Palladio) transformation finished in "
					+ df.format((System.currentTimeMillis() - init) / 1000.0) + " seconds.");
		} catch (MaudelingException | URISyntaxException | CoreException | ATLCoreException | IOException e) {
			console.println("[ERROR] Error in Behavior2Maude transformation process.\n" + e.getMessage());
		}
		/* end BehOcl2Maude */

		/* BehaviorMaudeModel 2 MaudeCode */
		init = System.currentTimeMillis();
		IFile behaviorCode;
		try {
			behaviorCode = PalladioRunningInformation.getDefault().createBehaviorCodeInFolder();

			Model2TextMaude.generate(PalladioRunningInformation.getDefault().getBehaviorMaude().toOSString(),
					behaviorCode.getLocation().toOSString());

			console.println("Behavior to Maude M2T transformation finished in "
					+ df.format((System.currentTimeMillis() - init) / 1000.0) + " seconds.");
		} catch (CoreException e) {
			e.printStackTrace();
		}
		/* end BehaviorMaudeModel 2 MaudeCode */

		/* InitialModel 2 Maude M2M */
		/*
		 * We know that the initial model for this Palladio Job is not going to
		 * be empty ever. Since the initial model is the Palladio views
		 * themselves.
		 */
		try {
			init = System.currentTimeMillis();
			IFolder folderTmp = info.getFolderTmp();
			folderTmp.getFile(PalladioRunningInformation.FLATTEN_MODEL);
			String name_initial_model = Model2Maude.model2MaudePalladio(
					folderTmp.getFile(PalladioRunningInformation.FLATTEN_MODEL), info.getMetamodel());
			console.println("Initial (flatten) model to Maude M2M transformation finished in "
					+ df.format((System.currentTimeMillis() - init) / 1000.0) + " seconds.");
		} catch (Exception e) {
			console.println("[ERROR] Error in Model2Maude transformation process.\n" + e.getMessage());
		}
		/* end InitialModel2Maude M2M */

		/* InitialModel 2 Maude code M2T */
		try {
			init = System.currentTimeMillis();
			IFile initialModelCode = info.createInitialModelCodeInMaudeFolder();
			Model2TextMaude.generate(info.getInitialModelMaude().toOSString(),
					initialModelCode.getLocation().toOSString());
			console.println("Initial (flatten) model to Maude code M2T transformation finished in "
					+ df.format((System.currentTimeMillis() - init) / 1000.0) + " seconds.");
		} catch (CoreException e) {
			console.println("[ERROR] Error in Model2MaudeCode transformation process.\n" + e.getMessage());
		}
		/* end InitialModel 2 Maude code M2T */
		
		/* Copying e-Motions infrastructure */
		init = System.currentTimeMillis();
		List<String> filesToCopy = Arrays.asList("mOdCL.maude", "MGDefinitions.maude", "EcoreMM.maude", 
				"MGRealTimeMaude24.maude", "e-Motions.maude", "PalladioGrammar.maude");
		for (String file : filesToCopy) {
			try {
				IFile aux = info.createFileInMaudeFolder(file);
				info.copyFile(aux, MaudeProcess.class.getResource(file));
			} catch (CoreException | IOException e) {
				e.printStackTrace();
			}
		}
		console.println("e-Motions Maude files copied in " + df.format((System.currentTimeMillis() - init) / 1000.0)
				+ " seconds. Files: " + filesToCopy.toString());
		/* end Copying e-Motions infrastructure */
		
		/* Creating TickRule Module */
		init = System.currentTimeMillis();
		createTickRuleModule("FLATTENPALLADIO");
		console.println(
				"TickMo dule generated in " + df.format((System.currentTimeMillis() - init) / 1000.0) + " seconds.");
		/* end of Creating TickRule Module */
		
		return Status.OK_STATUS;
	}
	
	protected void createTickRuleModule(String initialModel) {
		String behModuleName = null;
		try {
			behModuleName = info.extractBehaviorModuleName();
		} catch (IOException e) {
			console.println("[error]" + e.getMessage());
		}

		IFile aux;
		try {
			String generatedTickModule = ModuleGenerators.tickRuleFilePalladio(initialModel, behModuleName,
					"flattenPalladio", "FLATTENPALLADIO");
			aux = info.createFileInMaudeFolder(FileManager.TICKMODULE_FILENAME);
			
			if (aux.exists()) {
				aux.setContents(new ByteArrayInputStream(generatedTickModule.getBytes()), true, true, null);
			} else {
				aux.create(new ByteArrayInputStream(generatedTickModule.getBytes()), true, null);
			}
		} catch (CoreException e) {
			console.println("[error creating TickRule module] " + e.getMessage());
		}
	}

}
