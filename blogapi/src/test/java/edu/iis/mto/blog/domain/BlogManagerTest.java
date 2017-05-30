package edu.iis.mto.blog.domain;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.AssertTrue;

import org.aspectj.util.GenericSignature.FieldTypeSignature;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
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
    UserRepository userRepository;
    
    @MockBean
    BlogPostRepository blogRepository;
    
    @MockBean
    LikePostRepository likeRepository;

    @Autowired
    DataMapper dataMapper;

    @Autowired
    BlogService blogService;

    User firstUser, secondUser;
    BlogPost post;
    
    @Before
    public void setUp(){
    	
    	firstUser = new User();
    	firstUser.setFirstName("Donald");
    	firstUser.setLastName("Trump");
    	firstUser.setEmail("whitehouse@mexico.gov");
    	firstUser.setAccountStatus(AccountStatus.NEW);
    	firstUser.setId(59L);
    	
    	post = new BlogPost();
    	post.setId(1L);
    	post.setUser(firstUser);
    	post.setEntry("My name is post");
    	
    	secondUser = new User();
    	secondUser.setFirstName("Donald");
    	secondUser.setLastName("Tusk");
    	secondUser.setEmail("tusk@tower.com");
    	secondUser.setAccountStatus(AccountStatus.CONFIRMED);
    	secondUser.setId(23L);
    	
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
    public void confirmedUserCanLikePost() {
    	
    	firstUser.setAccountStatus(AccountStatus.CONFIRMED);
    	Mockito.when(userRepository.findOne(firstUser.getId())).thenReturn(firstUser);
    	Mockito.when(userRepository.findOne(secondUser.getId())).thenReturn(secondUser);
    	Mockito.when(blogRepository.findOne(post.getId())).thenReturn(post);
    	Optional<LikePost> list = Optional.empty();
    	Mockito.when(likeRepository.findByUserAndPost(secondUser, post)).thenReturn(list);
    	
    	Assert.assertThat(blogService.addLikeToPost(secondUser.getId(), post.getId()), Matchers.is(true));
//    	assertTrue(blogService.addLikeToPost(secondUser.getId(), post.getId()));
    }
    

}
