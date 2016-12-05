package es.uma.lcc.e_motions.transformations.model2model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.IModel;
import org.eclipse.m2m.atl.core.IReferenceModel;
import org.eclipse.m2m.atl.core.launch.ILauncher;
import org.eclipse.m2m.atl.engine.emfvm.launch.EMFVMLauncher;

import es.uma.lcc.e_motions.Activator;

public class InitModel2MaudeM2M extends EmotionsTransformation {
	/**
	 * ATL transformation header:
	 * 
	 * create OUT : Maude from IN : MM, INMM : Ecore, INPAR : Parameter;
	 */

	private String maudeMetamodelPath;
	private String atlParametersPath;
	private String atlTransformationPath;

	private IFile userDefinedMetamodel;
	private IFile initialModel;

	private IFile paramsFile;
	private IFile initModelXmi;

	

	public InitModel2MaudeM2M(String maudeMetamodelPath, String atlParametersPath, String atlTransformationPath,
			IFile userDefinedMetamodel, IFile initialModel, IFile paramsFile, IFile initModelXmi) {
		super();
		this.maudeMetamodelPath = maudeMetamodelPath;
		this.atlParametersPath = atlParametersPath;
		this.atlTransformationPath = atlTransformationPath;
		this.userDefinedMetamodel = userDefinedMetamodel;
		this.initialModel = initialModel;
		this.paramsFile = paramsFile;
		this.initModelXmi = initModelXmi;
	}

	@Override
	public void execute() throws ATLCoreException, IOException, CoreException {
		/* Loading Maude Metamodel */
		IReferenceModel maudeMM = mf.newReferenceModel();
		injector.inject(maudeMM, maudeMetamodelPath, new HashMap<String, Object>());

		/* Loading User Metamodel */
		IReferenceModel userMM = mf.newReferenceModel();
		injector.inject(userMM, userDefinedMetamodel.getFullPath().toPortableString(), new HashMap<String, Object>());
		
		/* Loading Ecore Metamodel */
		IReferenceModel ecoreMM = mf.newReferenceModel();
		injector.inject(ecoreMM, "http://www.eclipse.org/emf/2002/Ecore");

		/* Loading User Metamodel */
		IModel userMetamodel = mf.newModel(ecoreMM);
		injector.inject(userMetamodel, userDefinedMetamodel.getFullPath().toPortableString());

		/* Loading Parameter Metamodel */
		IReferenceModel parameterMM = mf.newReferenceModel();
		injector.inject(parameterMM, atlParametersPath, new HashMap<String, Object>());

		/* Loading User Model */
		IModel userInitialModel = mf.newModel(userMM);
		injector.inject(userInitialModel, initialModel.getFullPath().toPortableString());


		/* Loading Params model */
		String modelName = initialModel.getName();
		modelName = modelName.substring(0, modelName.indexOf('.')).toUpperCase();
		createParamsFile(modelName);

		IModel paramsModel = mf.newModel(parameterMM);
		injector.inject(paramsModel, paramsFile.getFullPath().toPortableString());

		/* Create output model */
		IModel maudeModel = mf.newModel(maudeMM);

		ILauncher transformationLauncher = new EMFVMLauncher();

		transformationLauncher.initialize(new HashMap<String, Object>());
		transformationLauncher.addInModel(userInitialModel, "IN", "MM");
		transformationLauncher.addInModel(userMetamodel, "INMM", "Ecore");
		transformationLauncher.addInModel(paramsModel, "INPAR", "Parameter");
		transformationLauncher.addOutModel(maudeModel, "OUT", "Maude");
		
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("allowInterModelReferences", Boolean.TRUE);
		System.out.println("atlTransformationPath: " + Activator.getDefault().getBundle().getEntry(atlTransformationPath));
		
		URL trafoURL = Activator.getDefault().getBundle().getEntry(atlTransformationPath);
		transformationLauncher.launch(ILauncher.RUN_MODE, null, options, new Object[] { trafoURL.openStream() });
		
		/* serialize model */
		extractor.extract(maudeModel, path(initModelXmi));
	}

	private void createParamsFile(String modelName) throws CoreException {
		StringBuilder model = new StringBuilder();

		model.append(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<ATLParameters:ParametersList xmi:version=\"2.0\"\n    ")
				.append("xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:ATLParameters=\"http://www.lcc.uma.es/ATLParameters/1.0\">\n");
		model.append("  <params name=\"modelName\" value=\"");
		model.append(modelName);
		model.append("\"/>\n");
		model.append("</ATLParameters:ParametersList>");

		if (!paramsFile.exists()) {
			paramsFile.create(new ByteArrayInputStream(model.toString().getBytes()), true, null);
		} else {
			paramsFile.setContents(new ByteArrayInputStream(model.toString().getBytes()), true, true, null);
		}
	}

}
