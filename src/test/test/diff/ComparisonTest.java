package test.diff;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.assertj.core.util.Files;
import junit.framework.TestCase;
import models.Comparison;
import models.JsonResult;
import models.NullContainerException;

import org.springframework.web.context.request.async.DeferredResult;

public class ComparisonTest extends TestCase {

	private final Path COMPARISONS_FOLDER = Paths.get(System.getProperty("user.dir"), "comparisons");

	protected void setUp() {
		// delete comparisons folder
		Files.delete(COMPARISONS_FOLDER.toFile());			
	}

	public void testCreateComparisonWithNullId() throws IOException, NullContainerException {
		try {
			// create new comparison without id
			Comparison.get(null);
	        fail("Expected an NullContainerException to be thrown");
	    } catch (NullContainerException nullContainerException) {
	        assertEquals(nullContainerException.getMessage(), "Container id not informed");
	    }
	}
	
	public void testResultOfComparison() throws IOException, NullContainerException {
		String leftEncodedContent =  "SGVsbG8gV29yZCENCg==";
		String rightEncodedContent = "SGVsbG8gV29yZCEhDQo=";
		
		Comparison comparison = Comparison.get("1");
		// set left side base64 content
		comparison.setLeft(leftEncodedContent);
		// set right side base64 content
		comparison.setRight(rightEncodedContent);
		DeferredResult<JsonResult> result = comparison.getResult();
		assertEquals(true, result.hasResult());
		assertEquals("1cHello Word!!.", ((JsonResult)result.getResult()).getOutput());
		
		
	}
	
	public void testCreateComparisonStructureFolder() throws IOException, NullContainerException {
		String leftEncodedContent =  "SGVsbG8gV29yZCENCg==";
		String rightEncodedContent = "SGVsbG8gV29yZCEhDQo=";
		
		String containerId = "1";
		// create new comparison without id
		Comparison comparison = Comparison.get(containerId);
		//verify id comparisons folder was created
		assertTrue(COMPARISONS_FOLDER.toFile().exists());
		//verify if id container was created
		assertTrue(Paths.get(COMPARISONS_FOLDER.toString(), containerId).toFile().exists());
		// set left side base64 content
		comparison.setLeft(leftEncodedContent);
		// verify if left content exists
		assertTrue(comparison.getLeftPath().toFile().exists());
		// verify left content file
		assertEquals(leftEncodedContent, Files.contentOf(comparison.getLeftPath().toFile(), Charset.defaultCharset()));
		
		// set right side base64 content
		comparison.setRight(rightEncodedContent);
		// verify if right content exists
		assertTrue(comparison.getRightPath().toFile().exists());
		// verify left content file
		assertEquals(rightEncodedContent, Files.contentOf(comparison.getRightPath().toFile(), Charset.defaultCharset()));
	
	}
}
