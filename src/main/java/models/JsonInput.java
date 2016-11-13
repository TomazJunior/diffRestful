package models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * Represents JSON input data to be compared
 * 	Used in /v1/diff/{id}/{position:left|right} end point
 *
 */
public class JsonInput {
	private String data;

	@JsonCreator
	public JsonInput(@JsonProperty("data") String data) {
		this.setData(data);
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
