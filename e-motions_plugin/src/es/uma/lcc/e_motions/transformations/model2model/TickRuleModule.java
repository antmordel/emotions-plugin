package es.uma.lcc.e_motions.transformations.model2model;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.m2m.atl.core.ATLCoreException;

public class TickRuleModule extends EmotionsTransformation {

	private String metamodelFile;
	private String behaviorFile;
	private String initalModel;
	private IFile output;
	private boolean printAttrs;
	private boolean printAdvisories;

	public TickRuleModule(String metamodelFile, String behaviorFile, String initalModel, IFile output,
			boolean printAttrs, boolean printAdvisories) {
		this.metamodelFile = metamodelFile;
		this.behaviorFile = behaviorFile;
		this.initalModel = initalModel;
		this.output = output;
		this.printAttrs = printAttrs;
		this.printAdvisories = printAdvisories;
	}

	@Override
	public void execute() throws ATLCoreException, IOException, CoreException {
		StringBuilder generatedTickModule = new StringBuilder();
		
	}

}
