package test.api;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
//import org.powermock.api.easymock.PowerMock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;

import api.v1.DiffController;
import junit.framework.TestCase;
import models.Comparison;
import models.JsonInput;

@WebAppConfiguration
@RunWith(PowerMockRunner.class)
@PrepareForTest({Comparison.class, DiffController.class})
public class DiffControllerTest extends TestCase {

	private MockMvc mockMvc;
	
	@InjectMocks
	DiffController controllerUnderTest;
	
	@Before
    public void setup() {
		MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controllerUnderTest).build();
    }

	public void testErrorInsertingWithoutData() throws Exception {
		mockMvc.perform(post("/v1/diff/{id}/{position:left|right}", "1", "left")).andExpect(status().is4xxClientError());		
	}
	
	public void testErrorInsertingLeftData() throws Exception {
		
		PowerMockito.mockStatic(Comparison.class);
		PowerMockito.when(Comparison.get(isA(String.class))).thenThrow(IOException.class);
		
		Gson gson = new Gson();
	    String json = gson.toJson(new JsonInput("SGVsbG8gV29yZCENCg=="));
	    
		mockMvc.perform(post("/v1/diff/{id}/{position:left|right}", "1", "left")
				.contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().is5xxServerError());
	}

	public void testErrorInsertingRightData() throws Exception {
		
		PowerMockito.mockStatic(Comparison.class);
		PowerMockito.when(Comparison.get(isA(String.class))).thenThrow(IOException.class);
		
		Gson gson = new Gson();
	    String json = gson.toJson(new JsonInput("SGVsbG8gV29yZCENCg=="));
	    
		mockMvc.perform(post("/v1/diff/{id}/{position:left|right}", "1", "right")
				.contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().is5xxServerError());
	}
	
	public void testInsertingLeftData() throws Exception {
		
		PowerMockito.mockStatic(Comparison.class);
		Comparison comp = mock(Comparison.class);
		
		PowerMockito.when(Comparison.get(isA(String.class))).thenReturn(comp);
		doNothing().when(comp).setLeft(isA(String.class));
		
		Gson gson = new Gson();
	    String json = gson.toJson(new JsonInput("SGVsbG8gV29yZCENCg=="));
	    
		mockMvc.perform(post("/v1/diff/{id}/{position:left|right}", "1", "left")
				.contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().is2xxSuccessful());
	}
	
	public void testInsertingRightData() throws Exception {
		
		PowerMockito.mockStatic(Comparison.class);
		Comparison comp = mock(Comparison.class);
		
		PowerMockito.when(Comparison.get(isA(String.class))).thenReturn(comp);
		doNothing().when(comp).setRight(isA(String.class));
		
		Gson gson = new Gson();
	    String json = gson.toJson(new JsonInput("SGVsbG8gV29yZCENCg=="));
	    
		mockMvc.perform(post("/v1/diff/{id}/{position:left|right}", "1", "right")
				.contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().is2xxSuccessful());
	}
}
