package es.uma.lcc.e_motions.transformations.creation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.m2m.atl.core.ATLCoreException;

public class TickRuleModule {

	private String metamodelFile;
	private String behaviorFile;
	private String initialModel;
	private IFile output;
	private boolean printAttrs;
	private boolean printAdvisories;
	private String nameInitialModule;
	private String nameBehaviorModule;

	public TickRuleModule(String metamodelFile, String behaviorFile, String initialModel, IFile output,
			boolean printAttrs, boolean printAdvisories, String nameInitialModule, String nameBehaviorModule) {
		this.metamodelFile = metamodelFile;
		this.behaviorFile = behaviorFile;
		this.initialModel = initialModel;
		this.output = output;
		this.printAttrs = printAttrs;
		this.printAdvisories = printAdvisories;
		this.nameInitialModule = nameInitialModule;
		this.nameBehaviorModule = nameBehaviorModule;
	}

	public void create() throws ATLCoreException, IOException, CoreException {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\n");

		sb.append("set show advisories ").append(printAdvisories?"on":"off").append(" .\n");
		sb.append("set print attributes ").append(printAttrs?"on":"off").append(" .\n");
		
		/* File importation */
		sb.append("\n");
		Arrays.asList("mOdCL.maude", "MGDefinitions.maude", "EcoreMM.maude", "MGRealTimeMaude24.maude", "e-Motions.maude").stream()
			.map(file -> sb.append("load ").append(file).append("\n")).collect(Collectors.joining());
		sb.append("\n");
		
		sb.append("load ").append(metamodelFile).append("\n");
		sb.append("load ").append(behaviorFile).append("\n");
		sb.append("load ").append(initialModel).append("\n");

		/* Module TICK RULE */
		sb.append("\n\nmod RUN is\n");
		sb.append("  pr ").append(nameInitialModule).append(" . ---- Also importing the metamodel\n");
		sb.append("  pr ").append(nameBehaviorModule).append(" .\n\n");
		sb.append("  var T TE : Time .\n");
		sb.append("  var MODEL : @Model .\n\n");		
		
		if (output.exists()) {
			output.setContents(new ByteArrayInputStream(sb.toString().getBytes()), true, true, null);
		} else {
			output.create(new ByteArrayInputStream(sb.toString().getBytes()), true, null);
		}
	}

}
