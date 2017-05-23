package edu.iis.mto.blog.domain;

import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
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
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.DataMapper;
import edu.iis.mto.blog.services.BlogService;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

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
        assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test(expected = DomainError.class)
    public void userWithStatusNewShouldNotLikePost() {
        User userJohn = new User();
        userJohn.setFirstName("Jan");
        userJohn.setEmail("john@domain.com");
        userJohn.setAccountStatus(AccountStatus.NEW);
        userJohn.setId(1L);

        User userJacob = new User();
        userJacob.setFirstName("Jacob");
        userJacob.setEmail("jacob@domain.com");
        userJacob.setAccountStatus(AccountStatus.NEW);
        userJacob.setId(2L);

        BlogPost blogPost = new BlogPost();
        blogPost.setUser(userJohn);
        blogPost.setEntry("My first post");
        blogPost.setId(1L);

        Mockito.when(userRepository.findOne(userJohn.getId())).thenReturn(userJohn);
        Mockito.when(userRepository.findOne(userJacob.getId())).thenReturn(userJacob);
        Mockito.when(blogPostRepository.findOne(blogPost.getId())).thenReturn(blogPost);
        Optional<LikePost> emptyLikesList = Optional.empty();
        Mockito.when(likePostRepository.findByUserAndPost(userJacob, blogPost)).thenReturn(emptyLikesList);

        assertFalse(blogService.addLikeToPost(userJacob.getId(), blogPost.getId()));
    }

}
