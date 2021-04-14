import cz.test.applifting.Application;
import cz.test.applifting.jpa.EndpointRepository;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EndpointControllerTest {
     private MockMvc mockMvc;
     private final static String URI = "/endpoints/";

     @Autowired
     private WebApplicationContext webApplicationContext;

     @Autowired
     private EndpointRepository endpointRepository;

     @Before
     public void setUp() {
          this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
     }

     @Test
     public void testGetEndpoints() throws Exception {
          this.mockMvc.perform(MockMvcRequestBuilders.get(URI)
               .accept(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$", hasSize(2)))
               .andReturn();
     }

     @Test
     public void testGetEndpointWithValidAccessToken() throws Exception {
          final HttpHeaders headers = new HttpHeaders();
          headers.put("accessToken", Collections.singletonList("07618a19-b17c-4437-8619-505af0f6906d"));

          this.mockMvc.perform(MockMvcRequestBuilders.get(URI + "1")
               .headers(headers)
               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("Google"))
               .andExpect(jsonPath("$.url").value("https://www.google.com"))
               .andExpect(jsonPath("$.timeInterval").value("19"))
               .andReturn();
     }

     @Test
     public void testGetEndpointWithInvalidAccessTokenAndNotExistingUUID() throws Exception {
          final HttpHeaders headers = new HttpHeaders();
          headers.put("accessToken", Collections.singletonList("07618a15-b17c-4437-8619-505af0f6906d"));

          this.mockMvc.perform(MockMvcRequestBuilders.get(URI + "1")
               .headers(headers)
               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest())
               .andExpect(status().reason(containsString("Couldn't find a user by this token")))
               .andReturn();
     }

     @Test
     public void testGetEndpointWithInvalidAccessTokenAndExistingUUID() throws Exception {
          final HttpHeaders headers = new HttpHeaders();
          headers.put("accessToken", Collections.singletonList("0c360165-b418-4caa-acea-92be46dc15a0"));

          this.mockMvc.perform(MockMvcRequestBuilders.get(URI + "1")
               .headers(headers)
               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isForbidden())
               .andExpect(status().reason(containsString("Access Denied.")))
               .andReturn();
     }

     @Test
     public void testPostEndpointWithValidToken() throws Exception {
          final HttpHeaders headers = new HttpHeaders();
          headers.put("accessToken", Collections.singletonList("0c360165-b418-4caa-acea-92be46dc15a0"));

          this.mockMvc.perform(MockMvcRequestBuilders.post(URI)
               .headers(headers)
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"name\": \"Test\", \"url\": \"https://stackoverflow.com/\", " +
                    "\"timeInterval\": \"50\"}")
               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
     }

     @Test
     public void testPostEndpointWithInvalidToken() throws Exception {
          final HttpHeaders headers = new HttpHeaders();
          headers.put("accessToken", Collections.singletonList("0c360166-b418-4caa-acea-92be46dc15a0"));

          this.mockMvc.perform(MockMvcRequestBuilders.post(URI)
               .headers(headers)
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"name\": \"Test\", \"url\": \"https://stackoverflow.com/\", " +
                    "\"timeInterval\": \"50\"}")
               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest())
               .andExpect(status().reason(containsString("Couldn't find a user by this token")))
               .andReturn();
     }

     @Test
     public void testUpdateEndpointWithValidToken() throws Exception {
          final HttpHeaders headers = new HttpHeaders();
          headers.put("accessToken", Collections.singletonList("07618a19-b17c-4437-8619-505af0f6906d"));

          this.mockMvc.perform(MockMvcRequestBuilders.put(URI + "1")
               .headers(headers)
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"name\": \"Google\", \"url\": \"https://www.google.com\", " +
                    "\"timeInterval\": \"19\"}")
               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andReturn();
     }

     @Test
     public void testUpdateEndpointWithInvalidToken() throws Exception {
          final HttpHeaders headers = new HttpHeaders();
          headers.put("accessToken", Collections.singletonList("0c360165-b418-4caa-acea-92be46dc15a0"));

          this.mockMvc.perform(MockMvcRequestBuilders.put(URI + "1")
               .headers(headers)
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"name\": \"Test\", \"url\": \"https://www.google.com\", " +
                    "\"timeInterval\": \"19\"}")
               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isForbidden())
               .andReturn();
     }

     @Test
     public void testUpdateEndpointWithInvalidURL() throws Exception {
          final HttpHeaders headers = new HttpHeaders();
          headers.put("accessToken", Collections.singletonList("0c360165-b418-4caa-acea-92be46dc15a0"));

          this.mockMvc.perform(MockMvcRequestBuilders.put(URI + "1")
               .headers(headers)
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"name\": \"Test\", \"url\": \"abcabc\", " +
                    "\"timeInterval\": \"18\"}")
               .accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andReturn();
     }

     @Test
     public void verifyDeleteEndpointWithValidURL() throws Exception {
          final HttpHeaders headers = new HttpHeaders();
          headers.put("accessToken", Collections.singletonList("0c360165-b418-4caa-acea-92be46dc15a0"));

          var endpoint = endpointRepository.findEndpointByUrl("https://stackoverflow.com/")
               .orElseThrow(() -> {
                    throw new IllegalArgumentException("Not found tested data");
               });

          this.mockMvc.perform(MockMvcRequestBuilders.delete(URI + endpoint.getId().toString())
               .headers(headers)
               .accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isOk())
               .andReturn();
     }
}
