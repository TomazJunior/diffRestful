package api.v1;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import models.Comparison;
import models.JsonInput;
import models.JsonResult;

/**
 * 
 * Diff Controller responsible for store left/right base64 encoded binary data and provides diff-ed result of them
 * 
 *
 */
@RestController
public class DiffController {
	
	/**
	 * Store left/right base64 encoded binary data
	 * 
	 * @param inputData JSON object with data content as base64 encoded binary data. e.g: { "data" : "SGVsbG8gV29yZCE=" }
	 * @param id comparison id
	 * @param position position encoded binary data to compare, possible values: left or right
	 * @return JSON Object
	 */
    @RequestMapping(value = "/v1/diff/{id}/{position:left|right}", method = RequestMethod.POST)
    public ResponseEntity<JsonResult> insert(@RequestBody JsonInput inputData, @PathVariable("id") String id, @PathVariable("position") String position) {
    	Comparison comparison = new Comparison(id);
    	try {
	    	switch (position) {
			case "left":
				comparison.setLeft(inputData.getData());
				break;
			case "right":
				comparison.setRight(inputData.getData());
				break;
			}
    	} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new JsonResult(e.getMessage()));
		}
    	return ResponseEntity.ok(new JsonResult(position + " successfully settled"));
    }
    
    /**
     * Return diff-ed of left/right base64 encoded data 
     * 
     * @param id comparison id
     * @return JSON Object with diff-ed result
     */
    @RequestMapping(value = "/v1/diff/{id}", method = RequestMethod.GET)
    public DeferredResult<JsonResult> result(@PathVariable("id") String id) {
    	Comparison comparison = new Comparison(id);
    	return comparison.getResult();
    }
}