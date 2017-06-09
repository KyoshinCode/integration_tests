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
    BlogPostRepository blogRepository;
    
    @MockBean
    LikePostRepository likeRepository;
	
    @MockBean
    UserRepository userRepository;

    @Autowired
    DataMapper dataMapper;

    @Autowired
    BlogService blogService;
    
    private User first;
    private User second;
    private BlogPost post;
    
    @Before
	public void setUp() {
    	
    	first = new User();
    	first.setFirstName("Jan");
    	first.setEmail("jan@domain.com");
    	first.setAccountStatus(AccountStatus.CONFIRMED);
    	first.setId(12L);
    	
        post = new BlogPost();
        post.setUser(first);
        post.setEntry("Jan's first post!!!");
        post.setId(23L);
    	
    	second = new User();
    	second.setFirstName("Jakub");
    	second.setEmail("jakub@domain.com");
    	second.setAccountStatus(AccountStatus.CONFIRMED);
    	second.setId(13L);
        
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
    public void onlyUserWithStatusCONFIRMEDCanLikePost() {
        
        Mockito.when(userRepository.findOne(second.getId())).thenReturn(second);
        Mockito.when(blogRepository.findOne(post.getId())).thenReturn(post);
        Optional<LikePost> likes = Optional.empty();
        Mockito.when(likeRepository.findByUserAndPost(second, post)).thenReturn(likes);

        Assert.assertThat(blogService.addLikeToPost(second.getId(), post.getId()), Matchers.equalTo(true));
    }

}
