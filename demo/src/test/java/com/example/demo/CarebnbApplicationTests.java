package com.example.demo;

import com.example.demo.Controllers.AuthController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc
class CarebnbApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Test
	void contextLoads() {
	}


	@Test
	public void shouldNotRegisterExistingUser() throws Exception{
		mockMvc.perform(post("/api/v1/auth/host/register")
				.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\": \"chinedu\",\"password\": \"Ragnarok\"}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void shouldRegisterNonExistingUser() throws Exception{
		mockMvc.perform(post("/api/v1/auth/host/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\": \"joh doe\",\"password\": \"12345\"}"))
				.andExpect(status().isOk());

	}

	@Test
	public void shouldDenyWithoutJwtCookie()throws Exception{
		mockMvc.perform(get("/api/v1/auth/test"))
				.andExpect(status().isUnauthorized());
	}

}
