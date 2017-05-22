package edu.iis.mto.blog.domain;

import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Assert;
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
    BlogPostRepository postRepository;
    
    @MockBean
    LikePostRepository likeRepository;

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
    
    @Test
    public void addingLikeToPostShouldRequireAccountStatusSetToCONFIRMED() {
    	
    	User author = new User();
    	author.setFirstName("Jan");
    	author.setLastName("Nowak");
    	author.setEmail("john@domain.com");
    	author.setAccountStatus(AccountStatus.CONFIRMED);
    	author.setId(123L);
        
        User user = new User();
        user.setFirstName("Janusz");
        user.setLastName("Nowakowski");
        user.setEmail("janusz@domain.com");
        user.setAccountStatus(AccountStatus.CONFIRMED);
        user.setId(124L);
        
        BlogPost blogPost = new BlogPost();
        blogPost.setUser(author);
        blogPost.setEntry("Post");
        blogPost.setId(234L);

        Mockito.when(userRepository.findOne(author.getId())).thenReturn(author);
        Mockito.when(userRepository.findOne(user.getId())).thenReturn(user);
        Mockito.when(postRepository.findOne(blogPost.getId())).thenReturn(blogPost);
        Optional<LikePost> emptyLikesList = Optional.empty();
        Mockito.when(likeRepository.findByUserAndPost(user, blogPost)).thenReturn(emptyLikesList);

        Assert.assertThat(blogService.addLikeToPost(user.getId(), blogPost.getId()), Matchers.equalTo(true));
    }
    
    @Test(expected = DomainError.class)
    public void addingLikeToPostAsNEWUserThrowsDomainError() {
    	
    	User author = new User();
    	author.setFirstName("Jan");
    	author.setLastName("Nowak");
    	author.setEmail("john@domain.com");
    	author.setAccountStatus(AccountStatus.CONFIRMED);
    	author.setId(123L);
        
        User user = new User();
        user.setFirstName("Janusz");
        user.setLastName("Nowakowski");
        user.setEmail("janusz@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
        user.setId(124L);
        
        BlogPost blogPost = new BlogPost();
        blogPost.setUser(author);
        blogPost.setEntry("Post");
        blogPost.setId(234L);

        Mockito.when(userRepository.findOne(author.getId())).thenReturn(author);
        Mockito.when(userRepository.findOne(user.getId())).thenReturn(user);
        Mockito.when(postRepository.findOne(blogPost.getId())).thenReturn(blogPost);
        Optional<LikePost> emptyLikesList = Optional.empty();
        Mockito.when(likeRepository.findByUserAndPost(user, blogPost)).thenReturn(emptyLikesList);

        Assert.assertThat(blogService.addLikeToPost(user.getId(), blogPost.getId()), Matchers.equalTo(true));
    }

}
