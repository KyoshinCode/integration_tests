package edu.iis.mto.blog.domain;

import org.hamcrest.Matchers;
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

import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.DataMapper;
import edu.iis.mto.blog.services.BlogService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

    @MockBean
    UserRepository userRepository;
    
    @MockBean
    BlogPostRepository blogPostRepository;
    
    @MockBean
    LikePostRepository likePostRepository;

    @Autowired
    DataMapper dataMapper;

    @Autowired
    BlogService blogService;
    
    private List<User> users;
    BlogPost post;
    
    @Before
    public void setUp() {
    	users = new ArrayList<User>();
        users.add(new User());
        users.add(new User());
        users.get(0).setFirstName("A");
        users.get(1).setFirstName("B");
        users.get(0).setLastName("A");
        users.get(1).setLastName("B");
        users.get(0).setEmail("A@a.com");
        users.get(1).setEmail("B@b.com");
        users.get(0).setId((long) 1);
        users.get(1).setId((long) 2);
        post = new BlogPost();
        post.setEntry("tralala");
        post.setId((long)123);
    }
    
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
        users.get(0).setAccountStatus(AccountStatus.NEW);
        users.get(1).setAccountStatus(AccountStatus.CONFIRMED);
        Mockito.when(userRepository.findOne(users.get(0).getId())).thenReturn(users.get(0));
        Mockito.when(userRepository.findOne(users.get(1).getId())).thenReturn(users.get(1));
        
        post.setUser(users.get(1));
        Mockito.when(blogPostRepository.findOne(post.getId())).thenReturn(post);
        Mockito.when(likePostRepository.findByUserAndPost(users.get(0), post)).thenReturn(Optional.empty());
        blogService.addLikeToPost(users.get(1).getId(), post.getId());
    }
    
    @Test (expected = DomainError.class)
    public void confirmedUserCannotLikeHisOwnPost() {
        users.get(0).setAccountStatus(AccountStatus.CONFIRMED);
        Mockito.when(userRepository.findOne(users.get(0).getId())).thenReturn(users.get(0));
        
        post.setUser(users.get(0));
        Mockito.when(blogPostRepository.findOne(post.getId())).thenReturn(post);
        Mockito.when(likePostRepository.findByUserAndPost(users.get(0), post)).thenReturn(Optional.empty());
        blogService.addLikeToPost(users.get(0).getId(), post.getId());
    }
    
    @Test
    public void confirmedUserCanLikeOtherUsersPost() {
        users.get(0).setAccountStatus(AccountStatus.CONFIRMED);
        users.get(1).setAccountStatus(AccountStatus.CONFIRMED);
        Mockito.when(userRepository.findOne(users.get(0).getId())).thenReturn(users.get(0));
        Mockito.when(userRepository.findOne(users.get(1).getId())).thenReturn(users.get(1));
        
        post.setUser(users.get(0));
        Mockito.when(blogPostRepository.findOne(post.getId())).thenReturn(post);
        Mockito.when(likePostRepository.findByUserAndPost(users.get(1), post)).thenReturn(Optional.empty());
        blogService.addLikeToPost(users.get(1).getId(), post.getId());
    }
}
