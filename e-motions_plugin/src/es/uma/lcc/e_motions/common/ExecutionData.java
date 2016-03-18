/**
 * @copyright Antonio Moreno-Delgado <i>amoreno@lcc.uma.es</i>
 * @date July 31th 2014
 *
 *  This file is part of e-Motions.
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
 *
 */

package es.uma.lcc.e_motions.common;
 /**
  *  This class store the information needed through the transformation and execution process.
  *  
  */
public class ExecutionData {
	
	private int timeLimit;
	private int defaultElapseTime;
	private boolean validateOCL;
	private boolean trackingActions = true;
	private boolean printAppliedRules = true;
	
	public boolean isValidateOCL() {
		return validateOCL;
	}

	public void setValidateOCL(boolean validateOCL) {
		this.validateOCL = validateOCL;
	}

	private static ExecutionData self;
	
	private ExecutionData() {
		this.timeLimit = -1;
		this.defaultElapseTime = -1;
		this.validateOCL = false;
	}
	
	public static ExecutionData getDefault() {
		if(self == null){
			self = new ExecutionData();
		}
		return self;
	}

	public int getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}
	
	public String getTimeLimitString() {
		return timeLimit == -1 ? "" : Integer.toString(timeLimit);
	}
	
	public int getDefaultElapseTime() {
		return timeLimit;
	}

	public void setDefaultElapseTime(int defaultElapsetime) {
		this.defaultElapseTime = defaultElapsetime;
	}
	
	public String getDefaultElapsetimeString() {
		return defaultElapseTime == -1 ? "" : Integer.toString(defaultElapseTime);
	}

	public boolean isTrackingActions() {
		return trackingActions;
	}

	public void setTrackingActions(boolean trackingActions) {
		this.trackingActions = trackingActions;
	}

	public boolean isPrintAppliedRules() {
		return printAppliedRules;
	}

	public void setPrintAppliedRules(boolean printAppliedRules) {
		this.printAppliedRules = printAppliedRules;
	}

}
