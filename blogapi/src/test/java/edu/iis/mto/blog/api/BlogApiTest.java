package edu.iis.mto.blog.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.persistence.EntityNotFoundException;

import org.junit.Before;
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
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.dto.Id;
import edu.iis.mto.blog.services.BlogService;
import edu.iis.mto.blog.services.DataFinder;

@RunWith(SpringRunner.class)
@WebMvcTest(BlogApi.class)
public class BlogApiTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BlogService blogService;

    @MockBean
    private DataFinder finder;

    UserRequest user;

	private Long newUserId = 44L;
    
    @Before
    public void setUp() {
    	user = new UserRequest();
        user.setEmail("milosz@domain.com");
        user.setFirstName("Milosz");
        user.setLastName("Szyrner");
    }
    @Test
    public void postBlogUserShouldResponseWithStatusCreatedAndNewUserId() throws Exception {
        Mockito.when(blogService.createUser(user)).thenReturn(newUserId );
        String content = writeJson(user);

        mvc.perform(post("/blog/user").contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).content(content)).andExpect(status().isCreated())
                .andExpect(content().string(writeJson(new Id(newUserId ))));
    }
    @Test
    public void thrownDataIntegrityViolationExceptionShouldGenerateStatusConfilt() throws Exception {
    	final int EXPECTED_HTTP_STATUS_CODE = 409;
    	Mockito.when(blogService.createUser(user)).thenThrow(new DataIntegrityViolationException(""));
        String content = writeJson(user);

        MvcResult mvcResult = mvc.perform(post("/blog/user").contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).content(content)).andReturn();
       
    	Mockito.verify(blogService, Mockito.times(1)).createUser(user);
        
		assertThat(mvcResult.getResponse().getStatus(), is(EXPECTED_HTTP_STATUS_CODE));
    }
    @Test
    
    public void getNotExistingBlogUserShouldResponseWithStatusNotFound() throws Exception{
    	final int EXPECTED_HTTP_STATUS_CODE = 404;
    	final long userId = 22L;
    	Mockito.when(finder.getUserData(userId)).thenThrow(new EntityNotFoundException(""));
    	
    	MvcResult mvcResult = mvc.perform(get("/blog/user/{id}", userId).contentType(MediaType.APPLICATION_JSON_UTF8))
    			.andExpect(status().isNotFound())
    			.andReturn();
    	
    	Mockito.verify(finder, Mockito.times(1)).getUserData(22L);
    	
		assertThat(mvcResult.getResponse().getStatus(), is(EXPECTED_HTTP_STATUS_CODE ));
    }
    private String writeJson(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writer().writeValueAsString(obj);
    }

}
