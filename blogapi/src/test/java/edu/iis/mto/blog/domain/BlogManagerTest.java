package edu.iis.mto.blog.domain;

import edu.iis.mto.blog.api.request.PostRequest;
import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
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

import edu.iis.mto.blog.api.request.PostRequest;
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


import static org.mockito.Mockito.times;
import static org.hamcrest.Matchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

    @MockBean
    UserRepository userRepository;
    
    @MockBean
    LikePostRepository likePostRepository;

    @MockBean
    BlogPostRepository blogPostRepository;

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
    
    @Test
    public void verifyIfConfirmUserCanLike() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        blogService.createUser(new UserRequest("Doe", "John", "doe@john.com"));
        
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository, times(2)).save(userParam.capture());
        
        userParam.getAllValues().get(1).setAccountStatus(AccountStatus.CONFIRMED);
        Mockito.verify(userRepository, times(2)).save(userParam.capture());
        
        blogService.createPost(userParam.getAllValues().get(1).getId(), new PostRequest());

        ArgumentCaptor<BlogPost> blogPostParam = ArgumentCaptor.forClass(BlogPost.class);
        Mockito.verify(blogPostRepository, times(1)).save(blogPostParam.capture());
        
        BlogService service = Mockito.spy(BlogService.class);
        Mockito.when(service.addLikeToPost(userParam.getAllValues().get(1).getId(), blogPostParam.getValue().getId())).thenReturn(true);
        
        Assert.assertThat(userParam.getAllValues().get(1).getAccountStatus(), is(equalTo(AccountStatus.CONFIRMED)));
        Assert.assertThat(service.addLikeToPost(userParam.getAllValues().get(1).getId(), blogPostParam.getValue().getId()), is(equalTo(true)));
	}
    
    @Test(expected = DomainError.class)
    public void verifyIfUnconfirmUserCanLike() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        blogService.createUser(new UserRequest("Doe", "John", "doe@john.com"));
        
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository, times(2)).save(userParam.capture());
        userParam.getAllValues().get(1).setAccountStatus(AccountStatus.NEW);
        Mockito.verify(userRepository, times(2)).save(userParam.capture());
        blogService.createPost(userParam.getAllValues().get(1).getId(), new PostRequest());


        ArgumentCaptor<BlogPost> blogPostParam = ArgumentCaptor.forClass(BlogPost.class);
        Mockito.verify(blogPostRepository, times(1)).save(blogPostParam.capture());
        
        BlogService service = Mockito.spy(BlogService.class);
        Mockito.when(service.addLikeToPost(userParam.getAllValues().get(1).getId(), blogPostParam.getValue().getId())).thenThrow(new DomainError("Domain error!"));
        
        Assert.assertThat(userParam.getAllValues().get(1).getAccountStatus(), is(equalTo(AccountStatus.NEW)));
        Assert.assertThat(service.addLikeToPost(userParam.getAllValues().get(1).getId(), blogPostParam.getValue().getId()), is(equalTo(true)));
	}  
}
