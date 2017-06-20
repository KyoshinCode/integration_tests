package edu.iis.mto.blog.api;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
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
import org.springframework.test.web.servlet.MvcResult;

import javax.persistence.EntityNotFoundException;

@RunWith(SpringRunner.class)
@WebMvcTest(BlogApi.class)
public class BlogApiTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BlogService blogService;

    @MockBean
    private DataFinder finder;
    UserRequest user = new UserRequest();

    @Test
    public void postBlogUserShouldResponseWithStatusCreatedAndNewUserId() throws Exception {
        Long newUserId = 1L;
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
    public void shouldGenerateConflictWhenThrownDataIntegrityViolationException() throws Exception {
        Mockito.when(blogService.createUser(user)).thenThrow(DataIntegrityViolationException.class);
        String content = writeJson(user);

        MvcResult mvcResult = mvc.perform(post("/blog/user").contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8).content(content)).andReturn();

        assertThat(mvcResult.getResponse().getStatus(), is(409));
    }

    @Test
    public void shouldResponseWithStatusNotFoundWhenTryingToGetNotExistingUser() throws Exception{
        Mockito.when(finder.getUserData(55L)).thenThrow(new EntityNotFoundException());

        MvcResult mvcResult = mvc.perform(get("/blog/user/55").accept(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isNotFound()).andReturn();

        assertThat(mvcResult.getResponse().getStatus(), is(404 ));
    }

}
