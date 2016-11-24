/**
 *  Methods to generate an ATL transformation which handle the OCL expressions

 */

package es.uma.lcc.e_motions.codegeneration;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.IExtractor;
import org.eclipse.m2m.atl.core.IInjector;
import org.eclipse.m2m.atl.core.IModel;
import org.eclipse.m2m.atl.core.IReferenceModel;
import org.eclipse.m2m.atl.core.ModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFExtractor;
import org.eclipse.m2m.atl.core.emf.EMFInjector;
import org.eclipse.m2m.atl.core.emf.EMFModel;
import org.eclipse.m2m.atl.core.emf.EMFModelFactory;
import org.eclipse.m2m.atl.engine.parser.AtlParser;

import es.uma.lcc.e_motions.common.FileManager;
import es.uma.lcc.e_motions.common.Printer;
import es.uma.lcc.e_motions.metamodels.Metamodels;
import es.uma.lcc.e_motions.runningdata.PalladioRunningInformation;

public class OldOclBehaviorParser {

	/**
	 * 
	 * @author Antonio Moreno {amoreno@lcc.uma.es}
	 * 
	 * @date July, 2nd 2014
	 * 
	 *       Creates an HOT to parse the OCL expressions in the behavior model
	 *       to be transformed to Maude.
	 * 
	 * @param metamodel
	 * @param behaviorModel
	 * @throws IOException
	 * @throws CoreException
	 * @throws ATLCoreException
	 */
	// TODO change this please. do not put boolean palladio anymore
	public static void oclParser(String behaviorModel, String metamodel, boolean palladio)
			throws IOException, CoreException, ATLCoreException {

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		
		// TODO change this
		String project = null;
		if (!palladio) {
			project = FileManager.getDefault().getBehavior().getProject().getName();
		} else {
			project = PalladioRunningInformation.getDefault().getBehaviorModel().getProject().getName();
		}
		
		// TODO change this
		IFolder tmp = FileManager.getDefault().createFolderTmp(project);
		
		IFile oclBehaviorATLCode = tmp.getFile(FileManager.OCLBEHAVIOR_ATL);

		String output = OldOclBehaviorParser.parseOclBehavior(behaviorModel, metamodel);
		InputStream contents = new ByteArrayInputStream(
				output.getBytes("UTF-8"));
		if (oclBehaviorATLCode.exists()) {
			oclBehaviorATLCode.setContents(contents, false, false, null);
		} else {
			oclBehaviorATLCode.create(contents, true, null);
		}

		ModelFactory mF = new EMFModelFactory();
		IInjector injector = new EMFInjector();

		IReferenceModel atlMM = mF.newReferenceModel();
		injector.inject(atlMM, Metamodels.class.getResource("ATL.ecore")
				.openStream(), new HashMap<String, Object>());

		AtlParser atlPars = AtlParser.getDefault();
		IModel atlModel = mF.newModel(atlMM);
		atlPars.inject(atlModel, oclBehaviorATLCode.getLocation().toOSString());

		/* create actual file in the .tmp folder */
		IFile oclBehaviorATLModel = tmp.getFile(FileManager.OCLBEHAVIOR_XMI);
		FileManager.getDefault().setOCLParserModel(oclBehaviorATLModel);

		IExtractor extractor = new EMFExtractor();

		extractor.extract(atlModel, oclBehaviorATLModel.getFullPath()
				.toString());

		EMFModelFactory emfMF = (EMFModelFactory) mF;
		emfMF.unload((EMFModel) atlMM);
		emfMF.unload((EMFModel) atlModel);

		root.refreshLocal(IResource.DEPTH_INFINITE, null);
	}

	/**
	 * parseOclBehavior (i) takes a metamodel path as param and parses all enums
	 * and (ii) creates a HOT to parse the OCL expressions in the inFile
	 * (behavior model).
	 * 
	 * @param inFile
	 * @param metamodel
	 * @return the whole atl transformation as a String
	 * @throws IOException
	 * @throws ATLCoreException
	 */
	public static String parseOclBehavior(String inFile, String metamodel)
			throws ATLCoreException {
		String res = "";

		ModelFactory mF = new EMFModelFactory();
		IInjector injector = new EMFInjector();

		IReferenceModel mmEcore = mF.newReferenceModel();
		injector.inject(mmEcore, "http://www.eclipse.org/emf/2002/Ecore");
		IModel mmModel = mF.newModel(mmEcore);
		injector.inject(mmModel, metamodel);

		Resource mmResource = ((EMFModel) mmModel).getResource();
		EList<EObject> mmObjectList = mmResource.getContents();
		LinkedList<String> enums = new LinkedList<String>();

		int d = 0;
		while (d < mmObjectList.size()) {
			EList<EClassifier> classes = ((EPackageImpl) mmObjectList.get(d))
					.getEClassifiers();
			int f = 0;
			while (f < classes.size()) {
				if (classes.get(f).getClass().getName()
						.equals("org.eclipse.emf.ecore.impl.EEnumImpl"))
					enums.add(classes.get(f).getName());
				f++;
			}
			d++;
		}

		/* Unload models */
		EMFModelFactory emfMF = (EMFModelFactory) mF;
		emfMF.unload((EMFModel) mmModel);
		emfMF.unload((EMFModel) mmEcore);

		BufferedReader br;
		try {
			URL url = new URL(inFile);
			InputStream inputStream = url.openConnection().getInputStream();
			br = new BufferedReader(new InputStreamReader(inputStream));
			res += "library oclBehavior; -- Library Template\n\n";

			String linea = br.readLine();
			while (linea != null) {

				linea = linea.replaceAll("&lt;", "<");
				for (String c : enums) {
					linea = linea.replaceAll(c + "::", c + "#");
				}
				if (linea.contains("oclExpression=")) {
					String cadena = linea.substring(
							linea.indexOf("oclExpression=\"") + 15,
							linea.indexOf("\"/>"));

					// helper Ocl troceado
					String contexto = cadena
							.substring(cadena.indexOf("context") + 8,
									cadena.indexOf("::"));

					String funcion = cadena.substring(cadena.indexOf("::") + 2);
					funcion = funcion.substring(0, funcion.indexOf("("));

					String cadenaNb = cadena.replaceAll(" ", "");
					String argumentos = cadenaNb.substring(
							cadenaNb.indexOf("("), cadenaNb.indexOf("):") + 1);

					String resultado = cadena.substring(0,
							cadena.indexOf("body"));
					resultado = resultado.substring(
							resultado.lastIndexOf(":") + 1, resultado.length());
					resultado = resultado.trim();

					String body = cadena.substring(cadena.indexOf("body") + 5);

					String[] tBasicos = { "Integer", "Real", "Boolean",
							"String" };
					String[] tCollection = { "Set", "Sequence", "Bag",
							"OrderedSet" };

					// tratamos el contexto
					contexto = contexto(contexto, tBasicos, tCollection);

					// tratamos los argumentos
					argumentos = argumentos(argumentos, tBasicos, tCollection);

					resultado = resultado(resultado, tBasicos, tCollection);

					res += "\n";
					res += "helper context " + contexto + " def: " + funcion
							+ "(" + argumentos + ") : " + resultado + " = \n"
							+ body + ";\n\n";
				}

				String[] cadena = { "value=", "duration=", "maxDuration=",
						"startingTime=", "endingTime=", "oclValue=", "pos=" };
				/*
				 * Antonio Moreno: por que no "minDuration="?
				 */

				for (int i = 0; i < cadena.length; i++) {
					if (linea.contains(cadena[i])) {
						res += analiza(linea, cadena[i]);
					}
				}
				linea = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			Printer.getDefault().println(
					e.getMessage() + "\nError generating OCL parser.");
			Printer.getDefault().println(e.getStackTrace().toString());
		}

		return res;
	}

	private static String argumentos(String argumentos, String[] tBasicos,
			String[] tCollection) {
		int contB = 0;
		Boolean encontradoB = false;
		StringTokenizer ast = new StringTokenizer(argumentos, ",");
		argumentos = "";
		String token, initoken = "";
		String result = "";
		while (ast.hasMoreTokens()) {
			token = ast.nextToken();
			if (token.startsWith("(")) {
				token = token.substring(token.indexOf("(") + 1);
			}

			initoken = token.substring(0, token.indexOf(":") + 1);
			token = token.substring(token.indexOf(":") + 1);

			if (token.endsWith(")")) {
				token = token.substring(0, token.lastIndexOf(")"));
			}

			contB = 0;
			encontradoB = false;
			while (contB < tBasicos.length && !encontradoB) {
				if (token.equalsIgnoreCase(tBasicos[contB])) {
					encontradoB = true;
				}
				contB++;
			}

			boolean encontradoC = false;
			if (!encontradoB && token.contains("(")) {
				String tokenAux = token.substring(0, token.indexOf("("));
				int contC = 0;

				while (contC < tCollection.length && !encontradoC) {
					if (tokenAux.equalsIgnoreCase(tCollection[contC])) {
						encontradoC = true;
					}
					contC++;
				}
			}

			if (!encontradoB && !encontradoC) {
				if (!token.equals(""))
					token = token + "!Ecore";
			}

			if (encontradoC) {

				String tokenAux = token.substring(0, token.indexOf("("));
				String tokenAux2;
				if (token.endsWith(")")) {
					tokenAux2 = token.substring(token.indexOf("(") + 1,
							token.indexOf(")"));
				} else {
					tokenAux2 = token.substring(token.indexOf("(") + 1);
				}

				contB = 0;
				encontradoB = false;
				while (contB < tBasicos.length && !encontradoB) {
					if (tokenAux2.equalsIgnoreCase(tBasicos[contB])) {
						encontradoB = true;
					}
					contB++;
				}
				if (!encontradoB) {
					token = tokenAux + "(" + tokenAux2 + "!Ecore)";
				}

			}
			result = result + initoken + token + ", ";
		}
		result = result.substring(0, result.length() - 2);

		return result;
	}

	private static String contexto(String contexto, String[] tBasicos,
			String[] tCollection) {
		int contB = 0;
		Boolean encontradoB = false;
		while (contB < tBasicos.length && !encontradoB) {
			if (contexto.contains(tBasicos[contB])
					|| contexto.equalsIgnoreCase(tCollection[contB])) {
				encontradoB = true;
			}
			contB++;
		}

		if (!encontradoB) {
			if (contexto.endsWith(")")) {
				contexto = contexto.substring(0, contexto.lastIndexOf(")"))
						+ "!Ecore)";
			} else {
				contexto = contexto + "!Ecore";
			}
		}
		return contexto;
	}

	private static String resultado(String contexto, String[] tBasicos,
			String[] tCollection) {
		int contB = 0;
		Boolean encontradoB = false;
		while (contB < tBasicos.length && !encontradoB) {
			if (contexto.contains(tBasicos[contB])) {
				encontradoB = true;
			}
			contB++;
		}

		if (!encontradoB) {
			if (contexto.endsWith(")")) {
				contexto = contexto.substring(0, contexto.lastIndexOf(")"))
						+ "!Ecore)";
			} else {
				contexto = contexto + "!Ecore";
			}
		}
		return contexto;
	}

	private static String analiza(String linea, String valor)
			throws IOException {
		String lineaAux;
		lineaAux = linea.substring(linea.indexOf(valor), linea.length());
		lineaAux = lineaAux.replace("\"\"", "\" \"");
		StringTokenizer lineaT = new StringTokenizer(lineaAux, "\"");
		String token, res = "";
		while (lineaT.hasMoreTokens()) {
			token = lineaT.nextToken();
			if (token.contains(valor)) {
				token = lineaT.nextToken();
				if (!token.contentEquals(" ")) {
					res += "helper def: \"" + token + "\" : String =\n";
					res += token + ";\n\n";
				}
			}
		}
		return res;
	}
}
