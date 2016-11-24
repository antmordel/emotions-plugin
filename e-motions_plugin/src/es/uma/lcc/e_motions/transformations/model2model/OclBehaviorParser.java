package es.uma.lcc.e_motions.transformations.model2model;

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
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.IExtractor;
import org.eclipse.m2m.atl.core.IModel;
import org.eclipse.m2m.atl.core.IReferenceModel;
import org.eclipse.m2m.atl.core.emf.EMFExtractor;
import org.eclipse.m2m.atl.core.emf.EMFModel;
import org.eclipse.m2m.atl.core.emf.EMFModelFactory;
import org.eclipse.m2m.atl.engine.parser.AtlParser;

import es.uma.lcc.e_motions.common.EmotionsPaths;

public class OclBehaviorParser extends ATLTransformation {
	
	private IFile modelBehavior;
	private IFile metamodel;
	private IFile outputCode;
	private IFile outputXMI;
	
	public OclBehaviorParser(IFile modelBehavior, IFile metamodel, IFile outputCode, IFile outputXMI) {
		super();
		this.modelBehavior = modelBehavior;
		this.metamodel = metamodel;
		this.outputCode = outputCode;
		this.outputXMI = outputXMI;
	}
	
	@Override
	public void execute() throws CoreException, ATLCoreException, IOException {
		/* Create ATL transformation */
		String output = parseOclBehavior();
		InputStream contents = new ByteArrayInputStream(output.getBytes("UTF-8"));
		outputCode.setContents(contents, false, false, null);
		
		/* Create ATL transformation into the XMI */
		IReferenceModel atlMM = mf.newReferenceModel();
		injector.inject(atlMM, EmotionsPaths.ATL, new HashMap<String, Object>());
		
		AtlParser atlPars = AtlParser.getDefault();
		IModel atlModel = mf.newModel(atlMM);
		atlPars.inject(atlModel, outputCode.getLocation().toOSString());
		
		IExtractor extractor = new EMFExtractor();

		extractor.extract(atlModel, "platform:/resource/" + outputXMI.getFullPath().toPortableString());

		EMFModelFactory emfMF = (EMFModelFactory) mf;
		emfMF.unload((EMFModel) atlMM);
		emfMF.unload((EMFModel) atlModel);

	}
	
	private String parseOclBehavior() throws ATLCoreException {
		String res = "";

		IReferenceModel mmEcore = mf.newReferenceModel();
		injector.inject(mmEcore, "http://www.eclipse.org/emf/2002/Ecore");
		IModel mmModel = mf.newModel(mmEcore);
		injector.inject(mmModel, metamodel.getFullPath().toPortableString());

		Resource mmResource = ((EMFModel) mmModel).getResource();
		EList<EObject> mmObjectList = mmResource.getContents();
		LinkedList<String> enums = new LinkedList<String>();
		System.out.println(mmResource.getContents());
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
		EMFModelFactory emfMF = (EMFModelFactory) mf;
		emfMF.unload((EMFModel) mmModel);
		emfMF.unload((EMFModel) mmEcore);

		BufferedReader br;
		try {
			URL url = new URL("platform:/resource/" + modelBehavior.getFullPath().toPortableString());
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
			System.out.println(e.getMessage());
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
