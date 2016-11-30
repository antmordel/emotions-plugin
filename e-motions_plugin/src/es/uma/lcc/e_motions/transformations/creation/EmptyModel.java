package es.uma.lcc.e_motions.transformations.creation;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

public class EmptyModel {
	
	private String metamodelModuleName;
	private String metamodelOperatorName;
	private IFile output;
	
	public EmptyModel(String metamodelModuleName, String metamodelOperatorName, IFile output) {
		super();
		this.metamodelModuleName = metamodelModuleName;
		this.metamodelOperatorName = metamodelOperatorName;
		this.output = output;
	}

	public void create() throws CoreException {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append("mod EMPTY-MODEL is\n");
		sb.append("  pr ").append(metamodelModuleName).append(" .\n");
		sb.append("  op emptyModel : -> @Model .\n");
		sb.append("  eq emptyModel = _`{_`}(").append(metamodelOperatorName).append(", none) .\n");
		sb.append("endm\n");
		
		if (output.exists()) {
			output.setContents(new ByteArrayInputStream(sb.toString().getBytes()), true, true, null);
		} else {
			output.create(new ByteArrayInputStream(sb.toString().getBytes()), true, null);
		}
	}
}
