package test.diff;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.assertj.core.util.Files;

import diff.IThreadListener;
import diff.NotifyThread;
import diff.WorkerCreator;
import junit.framework.TestCase;
import models.Comparison;
import models.NullContainerException;

public class WorkerTest extends TestCase {

	private final Path COMPARISONS_FOLDER = Paths.get(System.getProperty("user.dir"), "comparisons");

	protected void setUp() {
		// delete comparisons folder
		Files.delete(COMPARISONS_FOLDER.toFile());
	}

	public void testCompareWithoutLeftBase64() throws IOException, NullContainerException {
		// create new comparison
		Comparison comp = Comparison.get("1");

		WorkerCreator.run(new IThreadListener() {
			@Override
			public void onComplete(NotifyThread thread) {
				assertEquals("Content [ left ] not found.", thread.output.toString());
			}
		}, comp);
	}

	public void testCompareWithoutRightBase64() throws IOException, NullContainerException {
		// create new comparison
		Comparison comp = Comparison.get("1");
		comp.setLeft("SGVsbG8gV29yZCENCg==");

		WorkerCreator.run(new IThreadListener() {
			@Override
			public void onComplete(NotifyThread thread) {
				assertEquals("Content [ right ] not found.", thread.output.toString());
			}
		}, comp);
	}

	public void testCompareIdenticalBase64() throws IOException, NullContainerException {
		// create new comparison
		Comparison comp = Comparison.get("1");
		comp.setLeft("SGVsbG8gV29yZCENCg==");
		comp.setRight("SGVsbG8gV29yZCENCg==");

		WorkerCreator.run(new IThreadListener() {
			@Override
			public void onComplete(NotifyThread thread) {
				assertEquals("equals", thread.output.toString());
			}
		}, comp);
	}

	public void testCompareDifferentBase64() throws IOException, NullContainerException {
		// create new comparison
		Comparison comp = Comparison.get("1");
		comp.setLeft("SGVsbG8gV29yZCENCg==");
		comp.setRight("SGVsbG8gV29yZDEhDQo=");

		WorkerCreator.run(new IThreadListener() {
			@Override
			public void onComplete(NotifyThread thread) {
				assertEquals("1cHello Word1!.", thread.output.toString());
			}
		}, comp);
	}
	
	public void testCompareDifferentSizeBase64() throws IOException, NullContainerException {
		// create new comparison
		Comparison comp = Comparison.get("1");
		comp.setLeft("SGVsbG8gV29yZCENCggtrrr==");
		comp.setRight("SGVsbG8gV29yZDEhDQo=");

		WorkerCreator.run(new IThreadListener() {
			@Override
			public void onComplete(NotifyThread thread) {
				assertEquals("not of equal size", thread.output.toString());
			}
		}, comp);
	}
}