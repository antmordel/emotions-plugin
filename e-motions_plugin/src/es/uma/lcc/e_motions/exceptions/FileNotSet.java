package es.uma.lcc.e_motions.exceptions;

public class FileNotSet extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FileNotSet() {
		super();
	}
	
	public FileNotSet(String msg) {
		super(msg);
	}
}
