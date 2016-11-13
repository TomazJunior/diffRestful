package models;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 
 * Represents rest api response object.
 * All rest api methods with returns an instance of it. 
 *
 */
// Jackson library will ignore null fields on serialization
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonResult {
	
	private String output;
	private String errorCode = null;

	public JsonResult(StringBuilder output) {
		this.output = output.toString();
	}
	
	public JsonResult(String output) {
		this.output = output;
	}
	
	public JsonResult(String output, String errorCode) {
		this(output);
		this.errorCode = errorCode;
	}
	
	public String getOutput() {
		return output;
	}

	public String getErrorCode() {
		return errorCode;
	}

}
