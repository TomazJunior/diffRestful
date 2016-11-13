package models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.context.request.async.DeferredResult;

import diff.IThreadListener;
import diff.NotifyThread;
import diff.WorkerCreator;

/**
 * 
 * Represents a comparison container
 *
 */
public class Comparison {

	/**
	 * Store files in current project folder and under comparisons folder
	 */
	private final Path COMPARISONS_FOLDER = Paths.get(System.getProperty("user.dir"), "comparisons");
	private Path path;

	public static Comparison get (String id) throws NullContainerException {
		return new Comparison(id);
	}
	
	/**
	 * Creates comparison container folder under COMPARISONS_FOLDER constant
	 * 
	 * @param id 
	 * 	      container id
	 * @throws NullContainerException 
	 * 		   throws when id is null	
	 */
	private Comparison(String id) throws NullContainerException {
		if (id == null) { throw new NullContainerException(); }
		this.path = Paths.get(COMPARISONS_FOLDER.toString(), id);
		if (!exists()) {
			createDirectory();
		}
	}

	private Boolean createDirectory() {
		// create any intermediate directory if it does not exist
		return new File(path.toString()).mkdirs();
	}

	/**
	 * Checks existence of container folder
	 * 
	 * @return  {@code true} if the file exists; {@code false} if the file does
     *          not exist or its existence cannot be determined.
	 */
	private Boolean exists() {
		return Files.exists(path);
	}

	/**
	 * Creates a left file into container folder with encoded data
	 * 
	 * @param encoded 
	 * 		  Base64 encoded data
	 * 		  	
	 * @throws IOException
	 *         I/O exception of some sort has occurred
	 */
	public void setLeft(String encoded) throws IOException {
		storeFile(encoded, "left");
	}

	/**
	 * Creates a right file into container folder with encoded data
	 * 
	 * @param encoded 
	 * 		  Base64 encoded data
	 * 		  	
	 * @throws IOException
	 *         I/O exception of some sort has occurred
	 */
	public void setRight(String encoded) throws IOException {
		storeFile(encoded, "right");
	}

	/**
	 * Invokes the worker creator in order to compare left and right files which returns comparison result by onComplete event 
	 * @return Deferred JsonResult  
	 */
	public DeferredResult<JsonResult> getResult() {

		DeferredResult<JsonResult> deferredResult = new DeferredResult<JsonResult>();

		WorkerCreator.run(new IThreadListener() {
			@Override
			public void onComplete(NotifyThread thread) {
				deferredResult.setResult(new JsonResult(thread.output));
			}
		}, this);

		return deferredResult;
	}

	/**
	 * 
	 * @param encoded
	 * 		  Base64 encoded data
	 * 
	 * @param fileName
	 * 		  The name of the file to use as the destination of file.
	 * 
	 * @throws FileNotFoundException
	 * 		   If the given fileName does not exist
	 * 	
	 */
	private void storeFile(String encoded, String fileName) throws FileNotFoundException {

		// create file with encoded data
		PrintWriter out = new PrintWriter(Paths.get(path.toString(), fileName).toString());
		try {
			out.print(encoded);
		} finally {
			out.close();
		}
	}

	/**
	 * Full left file path
	 * @return path of left file
	 * 
	 */
	public Path getLeftPath() {
		return Paths.get(path.toString(), "left");
	}

	/**
	 * Full right file path
	 * @return path of right file
	 * 
	 */
	public Path getRightPath() {
		return Paths.get(path.toString(), "right");
	}

}
