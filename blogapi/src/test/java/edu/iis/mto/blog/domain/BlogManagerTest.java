package edu.iis.mto.blog.domain;

import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

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
import static org.mockito.Mockito.times;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

    @MockBean
    UserRepository userRepository;
    
    @MockBean
    BlogPostRepository blogPostRepository;

    @Autowired
    DataMapper dataMapper;

    @Autowired
    BlogService blogService;
    
    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        Assert.assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }
    
    @Test (expected = DomainError.class)
    public void newUserShouldNotBeAbleToLikePost() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        blogService.createUser(new UserRequest("Wujek", "Sado", "wujek@sado.com"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository, times(2)).save(userParam.capture());
        List<User> users = userParam.getAllValues();
        users.get(0).setId((long) 0);
        users.get(1).setId((long) 1);
        Mockito.verify(userRepository, times(2)).save(userParam.capture());
        users = userParam.getAllValues();
        
        blogService.createPost(users.get(0).getId(), new PostRequest());
        ArgumentCaptor<BlogPost> blogPostParam = ArgumentCaptor.forClass(BlogPost.class);
        Mockito.verify(blogPostRepository).save(blogPostParam.capture());
        BlogPost blogPost = blogPostParam.getValue();
        blogPost.setId((long) 3);
        blogPost.setUser(users.get(0));
        Mockito.verify(blogPostRepository).save(blogPostParam.capture());
        blogPost = blogPostParam.getValue();
        
        BlogService aSpy = Mockito.spy(BlogService.class);
        Mockito.when(aSpy.addLikeToPost(users.get(1).getId(), blogPost.getId())).thenThrow(new DomainError("User not confirmed!"));
        
        Assert.assertThat(users.get(1).getAccountStatus(), is((not(equalTo(AccountStatus.CONFIRMED)))));
        aSpy.addLikeToPost(users.get(1).getId(), blogPost.getId());
    }
    
    @Test (expected = DomainError.class)
    public void confirmedUserCannotLikeHisOwnPost() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository, times(1)).save(userParam.capture());
        List<User> users = userParam.getAllValues();
        users.get(0).setId((long) 0);
        users.get(0).setAccountStatus(AccountStatus.CONFIRMED);
        Mockito.verify(userRepository, times(1)).save(userParam.capture());
        users = userParam.getAllValues();
        
        blogService.createPost(users.get(0).getId(), new PostRequest());
        ArgumentCaptor<BlogPost> blogPostParam = ArgumentCaptor.forClass(BlogPost.class);
        Mockito.verify(blogPostRepository).save(blogPostParam.capture());
        BlogPost blogPost = blogPostParam.getValue();
        blogPost.setId((long) 3);
        blogPost.setUser(users.get(0));
        Mockito.verify(blogPostRepository).save(blogPostParam.capture());
        blogPost = blogPostParam.getValue();
        
        BlogService aSpy = Mockito.spy(BlogService.class);
        Mockito.when(aSpy.addLikeToPost(users.get(0).getId(), blogPost.getId())).thenThrow(new DomainError("Cannot like your own post!"));
        
        Assert.assertThat(users.get(0).getAccountStatus(), is((equalTo(AccountStatus.CONFIRMED))));
        aSpy.addLikeToPost(users.get(1).getId(), blogPost.getId());
    }
    
    @Test
    public void confirmedUserCanLikeOtherUsersPost() {
    	blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        blogService.createUser(new UserRequest("Wujek", "Sado", "wujek@sado.com"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository, times(2)).save(userParam.capture());
        List<User> users = userParam.getAllValues();
        users.get(0).setId((long) 0);
        users.get(1).setId((long) 1);
        users.get(1).setAccountStatus(AccountStatus.CONFIRMED);
        Mockito.verify(userRepository, times(2)).save(userParam.capture());
        users = userParam.getAllValues();
        
        blogService.createPost(users.get(0).getId(), new PostRequest());
        ArgumentCaptor<BlogPost> blogPostParam = ArgumentCaptor.forClass(BlogPost.class);
        Mockito.verify(blogPostRepository).save(blogPostParam.capture());
        BlogPost blogPost = blogPostParam.getValue();
        blogPost.setId((long) 3);
        blogPost.setUser(users.get(0));
        Mockito.verify(blogPostRepository).save(blogPostParam.capture());
        blogPost = blogPostParam.getValue();
        
        BlogService aSpy = Mockito.spy(BlogService.class);
        Mockito.when(aSpy.addLikeToPost(users.get(1).getId(), blogPost.getId())).thenReturn(true);
        
        Assert.assertThat(users.get(1).getAccountStatus(), is((equalTo(AccountStatus.CONFIRMED))));
        Assert.assertThat(aSpy.addLikeToPost(users.get(1).getId(), blogPost.getId()), is(equalTo(true)));
    }
}
