package diff;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.assertj.core.util.Files;

import junit.framework.TestCase;
import models.Comparison;
import models.NullContainerException;

public class ComparisonTest extends TestCase {

	private final Path COMPARISONS_FOLDER = Paths.get(System.getProperty("user.dir"), "comparisons");

	protected void setUp() {
		// delete comparisons folder
		Files.delete(COMPARISONS_FOLDER.toFile());
	}

	public void testCreateComparisonWithNullId() throws IOException, NullContainerException {
		try {
			// create new comparison without id
			new Comparison(null);
	        fail("Expected an NullContainerException to be thrown");
	    } catch (NullContainerException nullContainerException) {
	        assertEquals(nullContainerException.getMessage(), "Container id not informed");
	    }
	}
}
