package edu.iis.mto.blog.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.persistence.EntityNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.dto.Id;
import edu.iis.mto.blog.services.BlogService;
import edu.iis.mto.blog.services.DataFinder;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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
    
    @Test
    public void invallidDataShouldGenerate409Error() throws Exception {
    	when(finder.getUserData(100L)).thenThrow(new DataIntegrityViolationException("data integrity violation"));
    	mvc.perform(get("/blog/user/100")).andExpect(status().isConflict());
    }
    
    @Test
    public void notExistingUserSearchShouldGenerate404Error() throws Exception {
    	when(finder.getUserData(100L)).thenThrow(new EntityNotFoundException());
    	mvc.perform(get("/blog/user/100")).andExpect(status().isNotFound());
    }

    private String writeJson(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writer().writeValueAsString(obj);
    }

}
