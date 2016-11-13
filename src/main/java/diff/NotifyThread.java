package diff;

import java.io.IOException;
import java.nio.file.Path;

import models.Comparison;

/**
 * 
 * Thread listener
 * The main goal is wrapper doRun method to send event when thread is done 
 *
 */
public abstract class NotifyThread implements Runnable {
	
	// store diff output result
	public StringBuilder output;
	
	// comparison container
	private Comparison comparison;
	
	/**
	 * 
	 * @param comparison
	 * 		  comparison container that will be used in thread
	 */
	public NotifyThread(Comparison comparison){
		this.comparison = comparison;
		this.output = new StringBuilder();
	}

	private IThreadListener listener = null;

	public final void setListener(final IThreadListener listener) {
		this.listener = listener;
	}

	/**
	 * Triggers on complete event
	 */
	private final void notifyListener() {
		listener.onComplete(this);
	}

	/**
	 * wrapper doRun method in order to notify listener when thread is done
	 */
	@Override
	public final void run() {
		try {
			doRun("-ed", comparison.getLeftPath(), comparison.getRightPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			notifyListener();
		}
	}

	/**
	 * To be implemented
	 * 
	 * @param options diff utility options. e.g: -ed
	 * @param left path of encoded left file
	 * @param right path of encoded right file
	 * @throws IOException
	 * 		   I/O exception of some sort has occurred	
	 */
	public abstract void doRun(String options, Path left, Path right) throws IOException;
}