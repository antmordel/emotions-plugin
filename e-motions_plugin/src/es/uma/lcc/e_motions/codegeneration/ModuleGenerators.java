package es.uma.lcc.e_motions.codegeneration;

import org.eclipse.core.resources.IFile;

import es.uma.lcc.e_motions.common.ExecutionData;
import es.uma.lcc.e_motions.common.FileManager;
import es.uma.lcc.e_motions.common.Printer;

/**
 * @copyright Antonio Moreno-Delgado <i>amoreno@lcc.uma.es</i>
 * @date July 31th 2014
 * 
 * 
 *  This file is part of e-Motions. It has been generated with Xtend.
 *
 *  e-Motions is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  e-Motions is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with e-Motions.  If not, see <http://www.gnu.org/licenses/>.
 */
public class ModuleGenerators {

	public static String tickRuleFile(final String NAME_INITIAL_MODULE, String behModuleName, IFile initialModel,
	    String initTerm) {
		StringBuilder generatedTickModule = new StringBuilder();
		generatedTickModule.append(Printer.header());
		
		if(ExecutionData.getDefault().isPrintAppliedRules()){
			generatedTickModule.append("set print attribute on .");
		}
		
		/* File importation */
		generatedTickModule.append("\nload mOdCL.maude\n").append("load model-checker.maude\n")
		    .append("load MGDefinitions.maude\n").append("load MGRealTimeMaude24.maude\n").append("load EcoreMM.maude\n")
		    .append("load e-Motions.maude\n\n");

		generatedTickModule.append("load ").append(FileManager.getDefault().getMetamodelCode().getName()).append("\n");
		generatedTickModule.append("load ").append(FileManager.getDefault().getBehaviorCode().getName()).append("\n");

		if (initialModel == null) {
			generatedTickModule.append("load EmptyModel.maude\n");
		} else {
			generatedTickModule.append("load " + initialModel.getName()).append(".maude\n");
		}

		generatedTickModule.append("\n");

		generatedTickModule.append("mod TICKRULE is\n").append("  pr ").append(NAME_INITIAL_MODULE).append(" .\n")
			    .append("  pr ").append(behModuleName).append(" .\n").append("  var T TE : Time .\n")
			    .append("  var MODEL : @Model .\n\n").append("  crl [tick] :\n ").append("    { MODEL } in time T\n")
			    .append("  =>\n").append("    { delta(MODEL, TE) } in time (T plus TE)\n")
			    .append("  if TE := minT (mte(MODEL), toRat(").append(ExecutionData.getDefault().getDefaultElapseTime())
			    .append("))\n").append("	   /\\ TE =/= zero /\\ (T plus TE) le toRat(")
			    .append(ExecutionData.getDefault().getTimeLimit()).append(") .\n").append("endm\n\n");
		
		if (initTerm != null) {
			generatedTickModule.append("rewrite init(").append(initTerm).append(") .\n");
		}

		return generatedTickModule.toString();
	}
	
	public static String emptyModel(String NAME_INITIAL_MODULE, String mmName, String moduleModelName) {
	 return Printer.header() + "\nmod " + NAME_INITIAL_MODULE + " is\n" +
	  											"  pr " + moduleModelName + " .\n" +
	  		                  "  op emptyModel : -> @Model .\n" + 
	                        "  eq emptyModel = _`{_`}( " + mmName + " , none ) .\nendm\n";
  }


	public static String createParametersModel(String modelName) {
		StringBuilder model = new StringBuilder();

		model.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<ATLParameters:ParametersList xmi:version=\"2.0\"\n    ")
		    .append(
		        "xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:ATLParameters=\"http://www.lcc.uma.es/ATLParameters/1.0\">\n");
		model.append("  <params name=\"modelName\" value=\"");
		model.append(modelName);
		model.append("\"/>\n");
		model.append("</ATLParameters:ParametersList>");

		return model.toString();
	}
}
