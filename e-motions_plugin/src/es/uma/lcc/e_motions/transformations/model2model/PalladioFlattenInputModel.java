package es.uma.lcc.e_motions.transformations.model2model;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.m2m.atl.emftvm.EmftvmFactory;
import org.eclipse.m2m.atl.emftvm.ExecEnv;
import org.eclipse.m2m.atl.emftvm.Metamodel;
import org.eclipse.m2m.atl.emftvm.Model;
import org.eclipse.m2m.atl.emftvm.impl.resource.EMFTVMResourceFactoryImpl;
import org.eclipse.m2m.atl.emftvm.util.DefaultModuleResolver;
import org.eclipse.m2m.atl.emftvm.util.ModuleResolver;
import org.eclipse.m2m.atl.emftvm.util.TimingData;

import es.uma.lcc.e_motions.Activator;
import es.uma.lcc.e_motions.common.EmotionsPaths;

public class PalladioFlattenInputModel extends EmotionsTransformation {

	private IFile originalUsageModel;
	private IFile originalRepositoryModel;
	private IFile originalSystemModel;
	private IFile originalAllocationModel;
	private IFile originalResenvModel;

	private IFile newUsageModel;
	private IFile newRepositoryModel;
	private IFile newSystemModel;
	private IFile newAllocationModel;
	private IFile newResenvModel;

	private IFile palladioResourceTypes;
	private IFile palladioPrimitiveTypes;
	
	private IFile output;

	public PalladioFlattenInputModel(IFile originalUsageModel, IFile originalRepositoryModel, IFile originalSystemModel,
			IFile originalAllocationModel, IFile originalResenvModel, IFile newUsageModel, IFile newRepositoryModel,
			IFile newSystemModel, IFile newAllocationModel, IFile newResenvModel, IFile palladioResourceTypes,
			IFile palladioPrimitiveTypes, IFile output) {
		super();
		this.originalUsageModel = originalUsageModel;
		this.originalRepositoryModel = originalRepositoryModel;
		this.originalSystemModel = originalSystemModel;
		this.originalAllocationModel = originalAllocationModel;
		this.originalResenvModel = originalResenvModel;
		this.newUsageModel = newUsageModel;
		this.newRepositoryModel = newRepositoryModel;
		this.newSystemModel = newSystemModel;
		this.newAllocationModel = newAllocationModel;
		this.newResenvModel = newResenvModel;
		this.palladioResourceTypes = palladioResourceTypes;
		this.palladioPrimitiveTypes = palladioPrimitiveTypes;
		this.output = output;
	}

	@Override
	public void execute() throws CoreException, IOException {
		copyFilesRemovePathmaps();
		copyPalladioFiles();
		flattenPalladioFiles();
	}

	private void copyFilesRemovePathmaps() throws CoreException, IOException {
		copyFileRemovePathmaps(originalUsageModel, newUsageModel);
		copyFileRemovePathmaps(originalRepositoryModel, newRepositoryModel);
		copyFileRemovePathmaps(originalSystemModel, newSystemModel);
		copyFileRemovePathmaps(originalAllocationModel, newAllocationModel);
		copyFileRemovePathmaps(originalResenvModel, newResenvModel);
	}

	private void copyFileRemovePathmaps(IFile source, IFile target) throws CoreException, IOException {
		// Reading file
		InputStream inp = source.getContents();
		String fileContents = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(inp));
		String line = br.readLine();
		while (line != null) {
			fileContents = fileContents + line + "\n";
			line = br.readLine();
		}
		br.close();

		// Changing pathmaps
		fileContents = fileContents.replaceAll("pathmap://PCM_MODELS/", "");

		// Saving output
		if (target.exists()) {
			target.setContents(new ByteArrayInputStream(fileContents.getBytes()), true, true, null);
		} else {
			target.create(new ByteArrayInputStream(fileContents.getBytes()), true, null);
		}
	}

	private void copyPalladioFiles() throws IOException, CoreException {
		copyFile(Activator.getDefault().getBundle().getEntry(EmotionsPaths.PALLADIO_RESOURCETYPE).openStream(),
				palladioResourceTypes);
		copyFile(Activator.getDefault().getBundle().getEntry(EmotionsPaths.PALLADIO_PRIMITIVETYPES).openStream(),
				palladioPrimitiveTypes);
	}

	private void copyFile(InputStream inp, IFile output) throws IOException, CoreException {
		String fileContentsRepository = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(inp));
		String line = br.readLine();
		while (line != null) {
			fileContentsRepository = fileContentsRepository + line + "\n";
			line = br.readLine();
		}
		if (output.exists()) {
			output.setContents(new ByteArrayInputStream(fileContentsRepository.getBytes()), true, true, null);
		} else {
			output.create(new ByteArrayInputStream(fileContentsRepository.getBytes()), true, null);
		}
	}

	private void registerMetamodel(ResourceSet rs, String path) {
		Resource Rpcm = rs.getResource(URI.createURI(path), true);
		Iterator<EObject> eo = Rpcm.getAllContents();
		while (eo.hasNext()) {
			EObject next = eo.next();
			if (next instanceof EPackage) {
				rs.getPackageRegistry().put(((EPackage) next).getNsURI(), ((EPackage) next));
			}
		}
	}

	private void flattenPalladioFiles() {
		/*
		 * Creates the environment
		 */
		ExecEnv env = EmftvmFactory.eINSTANCE.createExecEnv();
		ResourceSet rs = new ResourceSetImpl();

		/*
		 * Load metamodels
		 */

		/* Input metamodels */
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());

		final ExtendedMetaData extendedMetaData = new BasicExtendedMetaData(rs.getPackageRegistry());
		rs.getLoadOptions().put(XMLResource.OPTION_EXTENDED_META_DATA, extendedMetaData);

		registerMetamodel(rs, EmotionsPaths.MM_UNITS);
		registerMetamodel(rs, EmotionsPaths.MM_STOEX);
		registerMetamodel(rs, EmotionsPaths.MM_IDENTIFIER);
		registerMetamodel(rs, EmotionsPaths.MM_PCM);

		Metamodel MMPcm = EmftvmFactory.eINSTANCE.createMetamodel();
		MMPcm.setResource(rs.getResource(URI.createURI(EmotionsPaths.MM_PCM), true));
		env.registerMetaModel("PCM", MMPcm);

		// Output metamodels
		registerMetamodel(rs, EmotionsPaths.MM_FLATTEN);

		Metamodel MMFlattenPcm = EmftvmFactory.eINSTANCE.createMetamodel();
		MMFlattenPcm.setResource(rs.getResource(URI.createURI(EmotionsPaths.MM_FLATTEN), true));
		env.registerMetaModel("FlattenPCM", MMFlattenPcm);

		/*
		 * Create and register resource factories
		 */
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("emftvm", new EMFTVMResourceFactoryImpl());

		/*
		 * Load models
		 *
		 * create OUTPCM : FlattenPCM from INUSAGEMODEL : PCM, INREPOSITORY :
		 * PCM, INSYSTEM : PCM, INALLOCATION : PCM, INRESOURCEENVIRONTMENT :
		 * PCM, INRESOURCETYPE : PCM, INPRIMITIVETYPES : PCM;
		 *
		 */
		Model inUsageModel = EmftvmFactory.eINSTANCE.createModel();
		inUsageModel.setResource(rs.getResource(URI.createFileURI(newUsageModel.getLocation().toOSString()), true));
		// it is not working with the cross-references
		// inUsageModel.setResource(rs.getResource(URI.createURI("file:///" +
		// info.getNewUsageModel().getRawLocation().makeAbsolute().toOSString(),
		// true), true));
		env.registerInputModel("INUSAGEMODEL", inUsageModel);

		Model inRepository = EmftvmFactory.eINSTANCE.createModel();
		inRepository
				.setResource(rs.getResource(URI.createFileURI(newRepositoryModel.getLocation().toPortableString()), true));
		env.registerInputModel("INREPOSITORY", inRepository);

		Model inSystem = EmftvmFactory.eINSTANCE.createModel();
		inSystem.setResource(rs.getResource(URI.createFileURI(newSystemModel.getLocation().toPortableString()), true));
		env.registerInputModel("INSYSTEM", inSystem);

		Model inAllocation = EmftvmFactory.eINSTANCE.createModel();
		inAllocation
				.setResource(rs.getResource(URI.createFileURI(newAllocationModel.getLocation().toPortableString()), true));
		env.registerInputModel("INALLOCATION", inAllocation);

		Model inResourceEnvironment = EmftvmFactory.eINSTANCE.createModel();
		inResourceEnvironment
				.setResource(rs.getResource(URI.createFileURI(newResenvModel.getLocation().toPortableString()), true));
		env.registerInputModel("INRESOURCEENVIRONTMENT", inResourceEnvironment);

		Model inResourceType = EmftvmFactory.eINSTANCE.createModel();
		inResourceType
				.setResource(rs.getResource(URI.createFileURI(palladioResourceTypes.getLocation().toPortableString()), true));
		env.registerInputModel("INRESOURCETYPE", inResourceType);

		Model inPrimitiveTypes = EmftvmFactory.eINSTANCE.createModel();
		inPrimitiveTypes.setResource(
				rs.getResource(URI.createFileURI(palladioPrimitiveTypes.getLocation().toPortableString()), true));
		env.registerInputModel("INPRIMITIVETYPES", inPrimitiveTypes);

		// Output model
		Model outFlatten = EmftvmFactory.eINSTANCE.createModel();
		outFlatten.setResource(rs.createResource(URI.createURI("file:///" + output.getLocation().toPortableString())));
		env.registerOutputModel("OUTPCM", outFlatten);

		EcoreUtil.resolveAll(rs);

		/*
		 * Load the transformation
		 */
		final ModuleResolver mr = new DefaultModuleResolver(EmotionsPaths.TRANSFORMATIONS, rs);

		TimingData td = new TimingData();
		env.loadModule(mr, "MM2FlattenMM");

		td.finishLoading();
		env.run(td);
		td.finish();

		/*
		 * Save output model
		 */
		try {
			outFlatten.getResource().save(Collections.emptyMap());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
