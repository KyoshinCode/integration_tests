package edu.iis.mto.blog.domain;

import java.util.Optional;

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
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.DataMapper;
import edu.iis.mto.blog.services.BlogService;

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

    User postAuthor;
    User user;
    BlogPost post;
    
    @Before
    public void setup() {
        postAuthor = new User();
        postAuthor.setId(1L);
    	
        user = new User();
        user.setId(2L);
        user.setAccountStatus(AccountStatus.NEW);
        
        post = new BlogPost();
        post.setUser(postAuthor);
        post.setId(3L);
    	
        Mockito.when(userRepository.findOne(user.getId())).thenReturn(user);
        Mockito.when(blogPostRepository.findOne(post.getId())).thenReturn(post);
        Mockito.when(likePostRepository.findByUserAndPost(user, post)).thenReturn(Optional.empty());
    }
    
    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        Assert.assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }
    
    @Test(expected = DomainError.class)
    public void likingPostByUserWithStatusNEWThrowsDomainError() {
        user.setAccountStatus(AccountStatus.NEW);
        blogService.addLikeToPost(user.getId(), post.getId());
    }

    @Test(expected = DomainError.class)
    public void likingPostByUserWithStatusREMOVEDThrowsDomainError() {
        user.setAccountStatus(AccountStatus.REMOVED);
        blogService.addLikeToPost(user.getId(), post.getId());
    }
    
    @Test
    public void likingPostByUserWithStatusCONFIRMEDIsOk() {
        user.setAccountStatus(AccountStatus.CONFIRMED);
        blogService.addLikeToPost(user.getId(), post.getId());
    }
}
