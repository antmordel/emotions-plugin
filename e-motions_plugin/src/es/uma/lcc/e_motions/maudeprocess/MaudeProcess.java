package es.uma.lcc.e_motions.maudeprocess;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import es.uma.lcc.e_motions.common.FileManager;
import es.uma.lcc.e_motions.common.Printer;
import es.uma.lcc.e_motions.maudeprocess.exceptions.MaudeErrorException;
import es.uma.lcc.e_motions.maudeprocess.exceptions.MaudePrefsException;
import es.uma.lcc.e_motions.maudeprocess.exceptions.JobFailedException;
import es.upv.dsic.issi.moment.maudedaemon.core.MaudeDaemonPlugin;
import es.upv.dsic.issi.moment.maudedaemon.maude.IMaudeJob;
import es.upv.dsic.issi.moment.maudedaemon.maude.IMaudeProcessBatch;
import es.upv.dsic.issi.moment.maudedaemon.parser.ParseException;

public class MaudeProcess {

	private IMaudeProcessBatch pBatch;
	private static MaudeProcess self;
	private static ArrayList<MaudeProcess> processes;
	
	private MaudeProcess() {
		processes = new ArrayList<>();
	}
	
	public static MaudeProcess getNewMaudeProcess() {
		if(self == null) {
			self = new MaudeProcess();
		}
		MaudeProcess res = new MaudeProcess();
		processes.add(res);
		return res;
	}

	/**
	 * Inits a Maude proccess
	 */
	public void initMaudeProcess() throws MaudePrefsException, MaudeErrorException {

		// creates the Maude proccess
		pBatch = MaudeDaemonPlugin.getDefault().getNewMaudeProcessBatch();

		// we configure it
		if (!pBatch.configMaudeFromPrefs()) {
			throw new MaudePrefsException("[error] Maude error configuring preferences.");
		}
		pBatch.setCoreMaude();
		pBatch.setAutoConfig(false);

		try {
			pBatch.execMaude();
		} catch (IOException e) {
			throw new MaudeErrorException(e.getMessage());
		}
	}

	/**
	 * Load all Maude infrastracture
	 * 
	 * @throws IOException
	 * @throws MaudePrefsException
	 */
	public void loadEMotions() throws JobFailedException, ParseException, MaudeErrorException, MaudePrefsException, IOException {
		Printer _p = Printer.getDefault();
		loadFileInternal("mOdCL.maude");
		_p.println("<mOdCL.maude> loaded.");
		loadFileInternal("MGDefinitions.maude");
		_p.println("<MGDefinitions.maude> loaded.");
		loadFileInternal("EcoreMM.maude");
		_p.println("<EcoreMM.maude> loaded.");
		loadFileInternal("MGRealTimeMaude24.maude");
		_p.println("<MGRealTimeMaude24.maude> loaded.");
		loadFileInternal("e-Motions.maude");
		_p.println("<e-Motions.maude> loaded.");
	}

	public Boolean isRunning() {
		return pBatch.isRunning();
	}

	private String loadFileInternal(String filePath) throws MaudeErrorException, MaudePrefsException, IOException, ParseException, JobFailedException {
		URL urlArchivo = MaudeProcess.class.getResource(filePath);
		return loadFile(urlArchivo);
	}

	/**
	 * Send a file to the Maude process
	 */
	public String loadFile(URL filePath) throws JobFailedException, ParseException, IOException, MaudeErrorException, MaudePrefsException {
		// we load the file
		String file = FileManager.readFile(filePath);
		return sendFile2Maude(file);
	}

	public String loadFile(String filePath) throws JobFailedException, ParseException, IOException, MaudeErrorException, MaudePrefsException {
		// we load the file
		String file = FileManager.readFile(filePath);

		return sendFile2Maude(file);
	}

	private String sendFile2Maude(String file) throws ParseException, JobFailedException, MaudeErrorException {
		// we send it to Maude
		IMaudeJob mj = pBatch.createAndRunJobs(file).get(0);
		if (pBatch.getLastError() != null) {
			throw new JobFailedException("[error] Error loading file in Maude.");
		}

		// output
//		String error = mj.getError(); // solo para debugging
//		if (!error.equals("")) {
//			throw new MaudeErrorException("[error] Maude returned errors when loading files: " + error);
//		}
		return mj.getOut();
	}

	/**
	 * Load a String on Maude
	 */
	public String load(String string) throws JobFailedException, ParseException, IOException, MaudeErrorException, MaudePrefsException {

		Printer.getDefault().debug("[debug] We're going to send to Maude: " + string);
		// we send such command to maude
		IMaudeJob mj = pBatch.createAndRunJobs(string).get(0);
		mj.waitUntilFinish();

		String output = mj.getOut();
		Printer.getDefault().debug("[debug] Maude returns as error: " + mj.getError());
		Printer.getDefault().debug("[debug] Maude returns as output: " + output);
		return output;
	}
	
	/**
	 * Stops Maude
	 */
	public void stop() {
		pBatch.killMaude();
	}
	
	public static void stopAll() {
		for(MaudeProcess p : processes) {
			p.stop();
		}
		processes.clear();
	}

}