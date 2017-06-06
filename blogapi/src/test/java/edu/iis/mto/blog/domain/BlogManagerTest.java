package edu.iis.mto.blog.domain;

import edu.iis.mto.blog.api.request.PostRequest;
import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.DataMapper;
import edu.iis.mto.blog.services.BlogService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BlogPostRepository blogPostRepository;

    @Autowired DataMapper dataMapper;

    @Autowired
    private BlogService blogService;

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        Assert.assertThat(user.getAccountStatus(), equalTo(AccountStatus.NEW));
    }

    @Test (expected = DomainError.class)
    public void likePostByUserNotConfirmedThrowException() {
        // given
        // Dodaj użytkowników
        blogService.createUser(new UserRequest("Test", "Test", "test@o2.pl"));
        blogService.createUser(new UserRequest("Test2", "Test2", "test2@o2.pl"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(2)).save(userParam.capture());
        List<User> users = userParam.getAllValues();

        // Dodaj post
        blogService.createPost(users.get(0).getId(), new PostRequest());
        ArgumentCaptor<BlogPost> postParam = ArgumentCaptor.forClass(BlogPost.class);
        verify(blogPostRepository).save(postParam.capture());
        BlogPost blogPost = postParam.getValue();
        blogPost.setId(1L);
        blogPost.setUser(users.get(0));
        verify(blogPostRepository).save(postParam.capture());
        blogPost = postParam.getValue();

        BlogService spyBlogService = Mockito.spy(BlogService.class);
        when(spyBlogService.addLikeToPost(users.get(1).getId(), blogPost.getId())).thenThrow(new DomainError("User is not confirmed."));

        // when
        Assert.assertThat(users.get(1).getAccountStatus(), is(not(equalTo(AccountStatus.CONFIRMED))));

        // then
        spyBlogService.addLikeToPost(users.get(1).getId(), blogPost.getId());
    }

    @Test
    public void likePostByUserConfirmed() {
        // given
        // Dodaj użytkowników
        blogService.createUser(new UserRequest("Test", "Test", "test@o2.pl"));
        blogService.createUser(new UserRequest("Test2", "Test2", "test2@o2.pl"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(2)).save(userParam.capture());
        List<User> users = userParam.getAllValues();
        users.get(1).setAccountStatus(AccountStatus.CONFIRMED);
        verify(userRepository, times(2)).save(userParam.capture());
        users = userParam.getAllValues();

        // Dodaj post
        blogService.createPost(users.get(0).getId(), new PostRequest());
        ArgumentCaptor<BlogPost> postParam = ArgumentCaptor.forClass(BlogPost.class);
        verify(blogPostRepository).save(postParam.capture());
        BlogPost blogPost = postParam.getValue();
        blogPost.setId(1L);
        blogPost.setUser(users.get(0));
        verify(blogPostRepository).save(postParam.capture());
        blogPost = postParam.getValue();

        BlogService spyBlogService = Mockito.spy(BlogService.class);
        when(spyBlogService.addLikeToPost(users.get(1).getId(), blogPost.getId())).thenReturn(true);

        // when
        Assert.assertThat(users.get(1).getAccountStatus(), is(equalTo(AccountStatus.CONFIRMED)));

        // then
        Assert.assertThat(spyBlogService.addLikeToPost(users.get(1).getId(), blogPost.getId()), is(true));
    }
}
