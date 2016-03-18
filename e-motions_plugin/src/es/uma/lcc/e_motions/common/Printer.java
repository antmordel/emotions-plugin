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
 */
package es.uma.lcc.e_motions.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;


import org.eclipse.swt.widgets.Display;
import org.eclipse.xtend2.lib.StringConcatenation;
public class Printer {
	
	private ByteArrayOutputStream baos, baosMock;
	private PrintStream ps, psMock;
	private static Printer self;
	
	private Printer() {
		baos = new ByteArrayOutputStream();
		baosMock = new ByteArrayOutputStream();
		
		ps = new PrintStream(baos);
		psMock = new PrintStream(baosMock);
		
		// Redirecting the output to our console
		System.setOut(ps);
	}
	
	public static Printer getDefault() {
		if(self == null) {
			self = new Printer();
		}
		return self;
	}
	
	private void _print(String s){
		System.out.print(s);
		printToConsole(baos);
	}
	
	public void print(String s) {
		_print(s);
	}
	
	public void println(String s) {
		_print(s+"\n");
	}
	
	public void debug(String s){
		if(Debug.DEBUG){
			println(s);
		}
	}
	
	/*
	 * Print to the e-Motions console 
	 */
	private static void printToConsole(final ByteArrayOutputStream b) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				Console.print(b.toString());
			}
		});
	}
	
	public void close() {
		try {
			baos.close();
			baosMock.close();
		} catch (IOException e) {}
		ps.close();
		psMock.close();
	}
	
	public static void clean() {
		if(self != null){
			self = null;
		}
	}
	
  public static String header() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("---- ----------------------------------------------- ----");
    _builder.newLine();
    _builder.append("---- Programmatically generated code using e-Motions ----");
    _builder.newLine();
    _builder.append("---- @date ");
    Date _date = new Date();
    _builder.append(_date, "");
    _builder.append("             ----");
    _builder.newLineIfNotEmpty();
    _builder.append("---- ----------------------------------------------- ----");
    _builder.newLine();
    return _builder.toString();
  }
}
