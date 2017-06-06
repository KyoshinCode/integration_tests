package edu.iis.mto.blog.domain;

import static org.mockito.Mockito.times;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.is;

import java.util.List;
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

import edu.iis.mto.blog.api.request.PostRequest;
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
    
    private User postingUser;
    
    private User likingUser;
    
    private BlogPost blogPost;
    
    private Optional<LikePost> existingLikeForPostFake;

	@Before
	public void setUp() {
		postingUser = new User();
		postingUser.setId(1L);
		postingUser.setFirstName("Milosz");
		postingUser.setAccountStatus(AccountStatus.CONFIRMED);
		
		likingUser = new User();
		likingUser.setId(2L);
		likingUser.setFirstName("Adam");
		likingUser.setAccountStatus(AccountStatus.NEW);
		
		blogPost = new BlogPost();
		blogPost.setId(3L);
		blogPost.setUser(postingUser);
		
		Mockito.when(userRepository.findOne(postingUser.getId())).thenReturn(postingUser);
        Mockito.when(userRepository.findOne(likingUser.getId())).thenReturn(likingUser);
    	Mockito.when(blogPostRepository.findOne(blogPost.getId())).thenReturn(blogPost);
    	existingLikeForPostFake = Optional.empty();
	}

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
    	blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        user.setId(1L);
        Assert.assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
   }
   @Test(expected = DomainError.class)
   public void shouldThrowDomainErrorExceptionIfNewUserTryToLikePost() {
        
    	Mockito.when(likePostRepository.findByUserAndPost(likingUser, blogPost)).thenReturn(existingLikeForPostFake);
    	blogService.addLikeToPost(likingUser.getId(), blogPost.getId());
   }
   @Test(expected = DomainError.class)
   public void shouldThrowDomainErrorExceptionIfUserTryToLikeHisOwnPost() {
    	Mockito.when(likePostRepository.findByUserAndPost(postingUser, blogPost)).thenReturn(existingLikeForPostFake);
    	blogService.addLikeToPost(postingUser.getId(), blogPost.getId());
   }
}
