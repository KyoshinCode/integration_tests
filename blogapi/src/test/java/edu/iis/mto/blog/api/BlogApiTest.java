package edu.iis.mto.blog.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.dto.Id;
import edu.iis.mto.blog.services.BlogService;
import edu.iis.mto.blog.services.DataFinder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.fail;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BlogApi.class)
public class BlogApiTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BlogService blogService;

    @MockBean
    private DataFinder finder;

    @Test
    public void postBlogUserShouldResponseWithStatusCreatedAndNewUserId() throws Exception {
        Long newUserId = 1L;
        UserRequest user = new UserRequest();
        user.setEmail("john@domain.com");
        user.setFirstName("John");
        user.setLastName("Steward");
        Mockito.when(blogService.createUser(user)).thenReturn(newUserId);
        String content = writeJson(user);

        mvc.perform(post("/blog/user").contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).content(content)).andExpect(status().isCreated())
                .andExpect(content().string(writeJson(new Id(newUserId))));
    }

    private String writeJson(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writer().writeValueAsString(obj);
    }

    @Test
    public void dataIntegrityViolationExceptionShouldReturnHTTPStatus409() throws Exception{
        UserRequest user = new UserRequest();
        user.setEmail("john@domain.com");
        user.setFirstName("John");
        user.setLastName("Steward");
        Mockito.when(blogService.createUser(user)).thenThrow(new DataIntegrityViolationException("DataIntegrityViolationExceptionTest"));
        String content = writeJson(user);
        mvc.perform(post("/blog/user").contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).content(content))
                .andExpect(status().isConflict());
    }

    @Test
    public void notExistingUserShouldReturn404() throws Exception{
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);

        mockServer.expect(requestTo("/blog/user/-1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        try {
            restTemplate.getForObject("/blog/user/{id}", String.class, -1);
            fail("Get should fail while going to not existing id");
        } catch (HttpStatusCodeException e) {
            mockServer.verify();
            Assert.assertThat(e.getStatusCode(), is(equalTo(HttpStatus.NOT_FOUND)));
        }
    }
}
