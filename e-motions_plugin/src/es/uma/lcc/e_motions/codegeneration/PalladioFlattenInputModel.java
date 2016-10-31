package es.uma.lcc.e_motions.codegeneration;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;

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

import es.uma.lcc.e_motions.common.PalladioFileManager;
import es.uma.lcc.e_motions.console.EmotionsConsole;
import es.uma.lcc.e_motions.metamodels.Metamodels;

public class PalladioFlattenInputModel {
	
	private EmotionsConsole console;
	private PalladioFileManager fileManager;
	
	public PalladioFlattenInputModel() {
		console = EmotionsConsole.getDefault();
		fileManager = PalladioFileManager.getDefault();
	}
	
	public void run() {
		flattenPalladioFiles();
	}
	
	private void registerMetamodel(ResourceSet rs, String path) {
		Resource Rpcm = rs.getResource(URI.createURI(path), true);
		Iterator<EObject> eo = Rpcm.getAllContents();
		while (eo.hasNext()) {
			EObject next = eo.next();
			if (next instanceof EPackage) {
				rs.getPackageRegistry().put(((EPackage) next).getNsURI(), ((EPackage) next));
				console.println("URI: " + ((EPackage) next).getNsURI().toString());
			}
		}
	}
	
	private void flattenPalladioFiles() {
		/*
		 * Removes the strings "pathmap://PCM_MODELS/" in repository and resource environment files.
		 */
		try {
			fileManager.copyIFilesRemovePathmaps();
			fileManager.copyFixedFiles();
		} catch (CoreException | IOException e) {
			e.printStackTrace();
		}
		/* 
		 * Creates the environment
		 */
		ExecEnv env = EmftvmFactory.eINSTANCE.createExecEnv();
		ResourceSet rs = new ResourceSetImpl();
		console.println("Flattening Palladio files\n-------------------------");
		
		/*
		 * Load metamodels
		 */
		// Input metamodels
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
		final ExtendedMetaData extendedMetaData = new BasicExtendedMetaData(rs.getPackageRegistry());
		rs.getLoadOptions().put(XMLResource.OPTION_EXTENDED_META_DATA,
		    extendedMetaData);
		
		registerMetamodel(rs, Metamodels.class.getResource("units.ecore").toString());
		//registerMetamodel(rs, Metamodels.class.getResource("probabilityfunction.ecore").toString());
		registerMetamodel(rs, Metamodels.class.getResource("stoex.ecore").toString());
		registerMetamodel(rs, Metamodels.class.getResource("identifier.ecore").toString());
		registerMetamodel(rs, Metamodels.class.getResource("pcm.ecore").toString());
		
		Metamodel MMPcm = EmftvmFactory.eINSTANCE.createMetamodel();
		MMPcm.setResource(rs.getResource(URI.createURI(Metamodels.class.getResource("pcm.ecore").toString()), true));
		env.registerMetaModel("PCM", MMPcm);
		
		// Output metamodels
		registerMetamodel(rs, Metamodels.class.getResource("flatten.ecore").toString());
		
		Metamodel MMFlattenPcm = EmftvmFactory.eINSTANCE.createMetamodel();
		MMFlattenPcm.setResource(rs.getResource(URI.createURI(Metamodels.class.getResource("flatten.ecore").toString()), true));
		env.registerMetaModel("FlattenPCM", MMFlattenPcm);

		/*
		 * Create and register resource factories
		 */
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("emftvm", new EMFTVMResourceFactoryImpl());
		
		/*
		 * Load models
		 * 
		 * create OUTPCM : FlattenPCM from INUSAGEMODEL : PCM, INREPOSITORY : PCM, 
		 * 			INSYSTEM : PCM, INALLOCATION : PCM, INRESOURCEENVIRONTMENT : PCM, INRESOURCETYPE : PCM, INPRIMITIVETYPES : PCM;
		 * 
		 */
		Model inUsageModel = EmftvmFactory.eINSTANCE.createModel();
//		inUsageModel.setResource(rs.getResource(URI.createFileURI(fileManager.getNewUsageModel().getFullPath().toOSString()), true));
		inUsageModel.setResource(rs.getResource(URI.createFileURI(fileManager.getNewUsageModel().getLocation().toOSString()), true));
		// it is not working with the cross-references
		//inUsageModel.setResource(rs.getResource(URI.createURI("file:///" + fileManager.getNewUsageModel().getRawLocation().makeAbsolute().toOSString(), true), true));
		env.registerInputModel("INUSAGEMODEL", inUsageModel);
		console.println(" - Model UsageModel loaded");
		
		Model inRepository = EmftvmFactory.eINSTANCE.createModel();
//		inRepository.setResource(rs.getResource(URI.createFileURI(fileManager.getNewRepositoryModel().getFullPath().toOSString()), true));
		inRepository.setResource(rs.getResource(URI.createFileURI(fileManager.getNewRepositoryModel().getLocation().toOSString()), true));
		//inRepository.setResource(rs.getResource(URI.createURI("file:///" + fileManager.getNewRepositoryModel().getRawLocation().makeAbsolute().toOSString(), true), true));
		env.registerInputModel("INREPOSITORY", inRepository);
		console.println(" - Model Repository loaded");
		
		Model inSystem = EmftvmFactory.eINSTANCE.createModel();
//		inSystem.setResource(rs.getResource(URI.createFileURI(fileManager.getNewSystemModel().getFullPath().toOSString()), true));
		inSystem.setResource(rs.getResource(URI.createFileURI(fileManager.getNewSystemModel().getLocation().toOSString()), true));
		//inSystem.setResource(rs.getResource(URI.createURI("file:///" + fileManager.getNewSystemModel().getRawLocation().makeAbsolute().toOSString(), true), true));
		env.registerInputModel("INSYSTEM", inSystem);
		console.println(" - Model System loaded");
		
		Model inAllocation = EmftvmFactory.eINSTANCE.createModel();
//		inAllocation.setResource(rs.getResource(URI.createFileURI(fileManager.getNewAllocationModel().getFullPath().toOSString()), true));
		inAllocation.setResource(rs.getResource(URI.createFileURI(fileManager.getNewAllocationModel().getLocation().toOSString()), true));
		//inAllocation.setResource(rs.getResource(URI.createURI("file:///" + fileManager.getNewAllocationModel().getRawLocation().makeAbsolute().toOSString(), true), true));
		env.registerInputModel("INALLOCATION", inAllocation);
		console.println(" - Model Allocation loaded");
		
		Model inResourceEnvironment = EmftvmFactory.eINSTANCE.createModel();
//		inResourceEnvironment.setResource(rs.getResource(URI.createFileURI(fileManager.getNewResenvModel().getFullPath().toOSString()), true));
		inResourceEnvironment.setResource(rs.getResource(URI.createFileURI(fileManager.getNewResenvModel().getLocation().toOSString()), true));
		//inResourceEnvironment.setResource(rs.getResource(URI.createURI("file:///" + fileManager.getNewResenvModel().getRawLocation().makeAbsolute().toOSString(), true), true));
		env.registerInputModel("INRESOURCEENVIRONTMENT", inResourceEnvironment);
		console.println(" - Model ResourceEnvironment loaded");
		
		Model inResourceType = EmftvmFactory.eINSTANCE.createModel();
//		inResourceType.setResource(rs.getResource(URI.createFileURI(fileManager.getNewPalladioResourceType().getFullPath().toOSString()), true));
		inResourceType.setResource(rs.getResource(URI.createFileURI(fileManager.getNewPalladioResourceType().getLocation().toOSString()), true));
		//inResourceType.setResource(rs.getResource(URI.createURI("file:///" + fileManager.getNewPalladioResourceType().getRawLocation().makeAbsolute().toOSString(), true), true));
		env.registerInputModel("INRESOURCETYPE", inResourceType);
		console.println(" - Model ResourceType loaded");
		
		Model inPrimitiveTypes = EmftvmFactory.eINSTANCE.createModel();
//		inPrimitiveTypes.setResource(rs.getResource(URI.createFileURI(fileManager.getNewPalladioRepository().getFullPath().toOSString()), true));
		inPrimitiveTypes.setResource(rs.getResource(URI.createFileURI(fileManager.getNewPalladioRepository().getLocation().toOSString()), true));
		//inPrimitiveTypes.setResource(rs.getResource(URI.createURI("file:///" + fileManager.getNewPalladioRepository().getRawLocation().makeAbsolute().toOSString(), true), true));
		env.registerInputModel("INPRIMITIVETYPES", inPrimitiveTypes);
		console.println(" - Model PrimitiveTypes loaded");
		
		// Output model
		Model outFlatten = EmftvmFactory.eINSTANCE.createModel();
		outFlatten.setResource(rs.createResource(URI.createURI("file:///" + fileManager.getFolderTmp().getLocation().toOSString() + "/outFlatten.xmi")));
		env.registerOutputModel("OUTPCM", outFlatten);
		console.println(" - Model OUTPCM loaded");
		
		EcoreUtil.resolveAll(rs);
		
		/*
		 * Load the transformation
		 */
		final ModuleResolver mr = new DefaultModuleResolver("platform:/plugin/es.uma.lcc.e-motions/transformations/", rs);
		
		TimingData td = new TimingData();
		env.loadModule(mr, "MM2FlattenMM");
		console.println("Transformation MM2FlattenMM loaded");
		
		td.finishLoading();
		env.run(td);
		td.finish();
		
		/* 
		 * Save output model
		 */
		try {
			outFlatten.getResource().save(Collections.emptyMap());
			console.println(" - Ouptut flatten model saved");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
