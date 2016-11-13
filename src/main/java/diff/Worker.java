package diff;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.tomcat.util.codec.binary.Base64;

import models.Comparison;

/**
 * 
 * Responsible for fork "diff" utility passing files to be compared by parameter
 *
 */
public class Worker extends NotifyThread{

	/**
	 * 
	 * @param comparison
	 * 		  comparison container
	 */
	public Worker(Comparison comparison){
		super(comparison);
	}

	/**
	 * Decode and store path with _decoded suffix
	 * 
	 * @param path 
	 *		  path of file to be decoded and stored with _decoded suffix
	 * @return file content
	 * 
	 * @throws Exception
	 */
	private String decodeAndStore(Path path) throws Exception {
		
		if (!Files.exists(path)) { throw new Exception("Content [ " + path.getFileName() + " ] not found."); }
		
		String encoded = new String(Files.readAllBytes(path));
		
		byte[] backToBytes = Base64.decodeBase64(encoded);
		FileOutputStream fos = new FileOutputStream(path.toString() + "_decoded");
		try{
			fos.write(backToBytes);
			fos.write(System.lineSeparator().getBytes());
		} finally {
			fos.close();	
		}
		
		return encoded;
	}
	
	
	
	/**
	 * Fork and run diff library
	 * 
	 * @param options diff utility options. e.g: -ed
	 * @param left path of encoded left file
	 * @param right path of encoded right file
	 * @throws IOException
	 * 		   I/O exception of some sort has occurred
	 */
	@Override
	public void doRun(String options, Path left, Path right) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("doRun start");
		
		ProcessBuilder processBuilder;
		Process process;
		BufferedReader reader = null;
		try{
			String leftEncoded = decodeAndStore(left);
			String rigthEncoded = decodeAndStore(right);
			
			if (leftEncoded.length() != rigthEncoded.length()) {
				this.output.append("not of equal size");
				return;
			}
			
			processBuilder = new ProcessBuilder("diff", options, left.toString() + "_decoded", right.toString() + "_decoded");	
			processBuilder.redirectErrorStream(true);
			process = processBuilder.start();
			
			String line;
			reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while ((line = reader.readLine()) != null) {
			    this.output.append(line);
			}
			
			// wait for thread (native process) execution
			int exitCode = process.waitFor();
			
			// mean left/right are equals
			if (exitCode == 0) {
				this.output.append("equals");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.output.append(e.getMessage());
		} finally {
			if (reader != null) { reader.close(); }
		}
	}
}
