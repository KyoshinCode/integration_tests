package edu.iis.mto.blog.domain;

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

    private User author;
        private User confirmedUser;
        private User newUser;
        private BlogPost blogPost;
       
        
        @Before
    	public void setUp() {
         	
        author = new User();
        author.setFirstName("Adam");
        author.setLastName("Rezner");
        author.setEmail("adam@mail.com");
        author.setAccountStatus(AccountStatus.CONFIRMED);
        author.setId(123L);

        newUser = new User();
        newUser.setFirstName("Marcin");
        newUser.setLastName("Marcinkowski");
        newUser.setEmail("marcin@mail.com");
        newUser.setAccountStatus(AccountStatus.NEW);
        newUser.setId(124L);
        
        confirmedUser = new User();
        confirmedUser.setFirstName("Janusz");
        confirmedUser.setLastName("Januszowski");
        confirmedUser.setEmail("janusz@mail.com");
        confirmedUser.setAccountStatus(AccountStatus.CONFIRMED);
        confirmedUser.setId(124L);

        blogPost = new BlogPost();
        blogPost.setUser(author);
        blogPost.setEntry("Post");
        blogPost.setId(234L);

    }
        

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        Assert.assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }
    
    @Test
    public void addingLikeToPostShouldRequireStatus_CONFIRMED() {
	
    	Mockito.when(userRepository.findOne(author.getId())).thenReturn(author); 
        Mockito.when(userRepository.findOne(confirmedUser.getId())).thenReturn(confirmedUser);
        Mockito.when(blogPostRepository.findOne(blogPost.getId())).thenReturn(blogPost);
        Optional<LikePost> emptyLikesList = Optional.empty();
        Mockito.when(likePostRepository.findByUserAndPost(confirmedUser, blogPost)).thenReturn(emptyLikesList);
        Assert.assertThat(blogService.addLikeToPost(confirmedUser.getId(), blogPost.getId()), Matchers.equalTo(true));
	}
    
    
    @Test(expected = DomainError.class)
     public void tryAddLikeToPostAsNewUser_ThrowsDomainError() {
    	Mockito.when(userRepository.findOne(author.getId())).thenReturn(author);
    	Mockito.when(userRepository.findOne(newUser.getId())).thenReturn(newUser);
        Mockito.when(blogPostRepository.findOne(blogPost.getId())).thenReturn(blogPost);
        Optional<LikePost> emptyLikesList = Optional.empty();
    	Mockito.when(likePostRepository.findByUserAndPost(newUser, blogPost)).thenReturn(emptyLikesList);
    	Assert.assertThat(blogService.addLikeToPost(newUser.getId(), blogPost.getId()), Matchers.equalTo(true));
    }
}
