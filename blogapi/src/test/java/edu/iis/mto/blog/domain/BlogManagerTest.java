package edu.iis.mto.blog.domain;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
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

import static org.mockito.Mockito.when;

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

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        assertThat(user.getAccountStatus(), equalTo(AccountStatus.NEW));
    }
    
    @Test
    public void confiremdUserShouldBeAbleToAddLikePost() {
    	User postAuthor = new User();
    	postAuthor.setId(1L);
    	
    	User likeAuthor = new User();
    	likeAuthor.setId(2L);
    	likeAuthor.setAccountStatus(AccountStatus.CONFIRMED);
    	
    	BlogPost blogPost = new BlogPost();
    	blogPost.setUser(postAuthor);
    	
    	when(userRepository.findOne(2L)).thenReturn(likeAuthor);
    	when(blogPostRepository.findOne(1L)).thenReturn(blogPost);
    	when(likePostRepository.findByUserAndPost(likeAuthor, blogPost)).thenReturn(Optional.empty());
    	
    	assertThat(blogService.addLikeToPost(2L, 1L), is(equalTo(true)));    	
    }
    
    @Test (expected = DomainError.class)
    public void newUserCannotAddLikePost() {
    	User postAuthor = new User();
    	postAuthor.setId(1L);
    	
    	User likeAuthor = new User();
    	likeAuthor.setId(2L);
    	likeAuthor.setAccountStatus(AccountStatus.NEW);
    	
    	BlogPost blogPost = new BlogPost();
    	blogPost.setUser(postAuthor);
    	
    	when(userRepository.findOne(2L)).thenReturn(likeAuthor);
    	when(blogPostRepository.findOne(1L)).thenReturn(blogPost);
    	when(likePostRepository.findByUserAndPost(likeAuthor, blogPost)).thenReturn(Optional.empty());
    	
    	blogService.addLikeToPost(2L, 1L);  	
    }

}
