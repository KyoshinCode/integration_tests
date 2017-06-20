package edu.iis.mto.blog.domain;

import com.sun.org.apache.xpath.internal.operations.Bool;
import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
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
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.DataMapper;
import edu.iis.mto.blog.services.BlogService;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

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

    @Test(expected = DomainError.class)
    public void shouldNewUserCanNotLikePost() throws Exception {
        //given:
        User newUser = new User();
        newUser.setFirstName("Jan");
        newUser.setLastName("Kowalski");
        newUser.setEmail("jankowalski@mail.com");
        newUser.setAccountStatus(AccountStatus.NEW);
        newUser.setId(1L);

        BlogPost newBlogPost = new BlogPost();
        newBlogPost.setUser(newUser);
        newBlogPost.setEntry("entry");
        newBlogPost.setId(1L);

        Mockito.when(userRepository.findOne(newUser.getId())).thenReturn(newUser);
        Mockito.when(blogPostRepository.findOne(newBlogPost.getId())).thenReturn(newBlogPost);


        //when:
        blogService.addLikeToPost(newUser.getId(), newBlogPost.getId());
    }

    @Test
    public void shouldConfirmedUserCanLikePost() throws Exception {
        //given:
        User confirmedUser = new User();
        confirmedUser.setFirstName("Jan");
        confirmedUser.setLastName("Kowalski");
        confirmedUser.setEmail("jankowalski@mail.com");
        confirmedUser.setAccountStatus(AccountStatus.CONFIRMED);
        confirmedUser.setId(1L);

        User anyUser = new User();
        anyUser.setFirstName("Jan");
        anyUser.setLastName("Kowalski");
        anyUser.setEmail("jankowalski@mail.com");
        anyUser.setAccountStatus(AccountStatus.CONFIRMED);
        anyUser.setId(2L);

        BlogPost newBlogPost = new BlogPost();
        newBlogPost.setUser(anyUser);
        newBlogPost.setEntry("entry");
        newBlogPost.setId(1L);

        Mockito.when(userRepository.findOne(confirmedUser.getId())).thenReturn(confirmedUser);
        Mockito.when(userRepository.findOne(anyUser.getId())).thenReturn(anyUser);
        Mockito.when(blogPostRepository.findOne(newBlogPost.getId())).thenReturn(newBlogPost);
        Mockito.when(likePostRepository.findByUserAndPost(confirmedUser, newBlogPost)).thenReturn(Optional.empty());

        //when:
        Boolean result = blogService.addLikeToPost(confirmedUser.getId(), newBlogPost.getId());

        //then
        assertThat(result, equalTo(true));
    }

}
