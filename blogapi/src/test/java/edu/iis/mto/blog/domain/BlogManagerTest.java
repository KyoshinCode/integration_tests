package edu.iis.mto.blog.domain;

import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
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
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.DataMapper;
import edu.iis.mto.blog.services.BlogService;

import java.util.Optional;

import static edu.iis.mto.blog.domain.model.AccountStatus.CONFIRMED;
import static edu.iis.mto.blog.domain.model.AccountStatus.NEW;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    DataMapper dataMapper;

    @Autowired
    BlogService blogService;

    @MockBean
    BlogPostRepository blogRepository;

    @MockBean
    LikePostRepository likeRepository;

    private User firstUser;
    private User secondUser;
    private BlogPost blogPost;

    @Before
    public void setUp(){

        firstUser = new User();
        firstUser.setFirstName("Adam");
        firstUser.setLastName("Nowak");
        firstUser.setEmail("adamnowak@email.pl");
        firstUser.setAccountStatus(NEW);
        firstUser.setId(1L);

        secondUser = new User();
        secondUser.setFirstName("Jakub");
        secondUser.setLastName("Adamiak");
        secondUser.setEmail("jakubadamiak@email.pl");
        secondUser.setAccountStatus(CONFIRMED);
        secondUser.setId(2L);

        blogPost = new BlogPost();
        blogPost.setId(1L);
        blogPost.setUser(secondUser);
        blogPost.setEntry("Post");
    }


    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        assertThat(user.getAccountStatus(), Matchers.equalTo(NEW));
    }


    @Test(expected = DomainError.class)
    public void newUserCanNotLikePosts() {
        firstUser.setAccountStatus(NEW);
        Mockito.when(userRepository.findOne(firstUser.getId())).thenReturn(firstUser);
        Mockito.when(userRepository.findOne(secondUser.getId())).thenReturn(secondUser);
        Mockito.when(blogRepository.findOne(blogPost.getId())).thenReturn(blogPost);

        assertThat(blogService.addLikeToPost(firstUser.getId(), blogPost.getId()), is(true));
    }

    @Test
    public void confirmedUserCanLikePosts() {
        firstUser.setAccountStatus(CONFIRMED);
        Mockito.when(userRepository.findOne(firstUser.getId())).thenReturn(firstUser);
        Mockito.when(userRepository.findOne(secondUser.getId())).thenReturn(secondUser);
        Mockito.when(blogRepository.findOne(blogPost.getId())).thenReturn(blogPost);
        Optional<LikePost> list = Optional.empty();
        Mockito.when(likeRepository.findByUserAndPost(firstUser, blogPost)).thenReturn(list);

        assertThat(blogService.addLikeToPost(firstUser.getId(), blogPost.getId()), is(true));
    }
}
