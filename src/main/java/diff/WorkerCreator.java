package diff;

import models.Comparison;

/**
 * Responsible for start a diff worker
 * 
 */
public class WorkerCreator{

	/**
	 * Starts a worker thread
	 * 
	 * @param listener instance of IThreadListener that will be informed when Worker is done
	 * @param comparison container of left/right data to be compared
	 */
	public static void run(IThreadListener listener, Comparison comparison) {
		NotifyThread worker = new Worker(comparison);
		worker.setListener(listener);
		worker.run();
	}
}
