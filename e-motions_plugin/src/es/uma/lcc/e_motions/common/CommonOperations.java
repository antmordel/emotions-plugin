package es.uma.lcc.e_motions.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

public class CommonOperations {

	public static String extractModuleName(IFile file) throws IOException {
		BufferedReader br = null;
		try {
			file.refreshLocal(IResource.DEPTH_INFINITE, null);
			br = new BufferedReader(new InputStreamReader(file.getContents()));
		} catch (CoreException e) {
			e.printStackTrace();
		}
		String line = br.readLine();
		while (!line.startsWith("mod ")) {
			line = br.readLine();
		}

		String acum = "";
		if (line.startsWith("mod ")) {
			line = line.replaceFirst("mod ", "");
			int i = 0;
			while (line.charAt(i) != ' ' && line.charAt(i) != '\t') {
				acum = acum + line.charAt(i);
				i++;
			}
		}
		return acum;
	}
	
	public static String extractMetamodelOperator(IFile file) throws IOException {
		BufferedReader br = null;
    try {
    	file.refreshLocal(IResource.DEPTH_INFINITE, null);
	    br = new BufferedReader(new InputStreamReader(file.getContents()));
    } catch (CoreException e) {
	    e.printStackTrace();
    }
		String line = br.readLine();
		while (!line.startsWith("  op ")) {
			line = br.readLine();
		}

		String acum = "";
		if (line.startsWith("  op ")) {
			line = line.replaceFirst("  op ", "");
			int i = 0;
			while (line.charAt(i) != ' ' && line.charAt(i) != '\t') {
				acum = acum + line.charAt(i);
				i++;
			}
		}
		return acum;
	}
}
