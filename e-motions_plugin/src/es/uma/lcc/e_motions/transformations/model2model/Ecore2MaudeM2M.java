package es.uma.lcc.e_motions.transformations.model2model;

import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.IReferenceModel;

public class Ecore2MaudeM2M extends ATLTransformation {
	/**
	 * Header of the ATL transformation
	 * 
	 * module ec2ma; -- Module Template
	 * create OUT : Maude from IN : Ecore; --, INPAR : Parameter;
	 */
	
	public void execute() throws ATLCoreException {
		IReferenceModel ecoreMM = mf.newReferenceModel();
		injector.inject(ecoreMM, "http://www.eclipse.org/emf/2002/Ecore");
		
		
	}
}
