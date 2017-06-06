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

    private User user;
    private User userConfirmed;
    private User userNew;
    private BlogPost blogPost;

    @Before
	public void setUp() {

    	user = new User();
    	user.setFirstName("Jan");
    	user.setLastName("Nowak");
    	user.setEmail("john@domain.com");
    	user.setAccountStatus(AccountStatus.CONFIRMED);
    	user.setId(1L);

    	userConfirmed = new User();
        userConfirmed.setFirstName("");
        userConfirmed.setLastName("");
        userConfirmed.setEmail("");
        userConfirmed.setAccountStatus(AccountStatus.CONFIRMED);
        userConfirmed.setId(2L);

        userNew = new User();
        userNew.setFirstName("");
        userNew.setLastName("");
        userNew.setEmail("");
        userNew.setAccountStatus(AccountStatus.NEW);
        userNew.setId(3L);

        blogPost = new BlogPost();
        blogPost.setUser(user);
        blogPost.setEntry("");
        blogPost.setId(4L);

        Mockito.when(userRepository.findOne(user.getId())).thenReturn(user);
        Mockito.when(postRepository.findOne(blogPost.getId())).thenReturn(blogPost);
    }

    @Test
    public void creatinguserNewShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        Assert.assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test
    public void userConfirmedShouldAddLike() {
        Optional<LikePost> emptyLikesList = Optional.empty();

        Mockito.when(userRepository.findOne(userConfirmed.getId())).thenReturn(userConfirmed);
        Mockito.when(likeRepository.findByUserAndPost(userConfirmed, blogPost)).thenReturn(emptyLikesList);

        Assert.assertThat(blogService.addLikeToPost(userConfirmed.getId(), blogPost.getId()), Matchers.equalTo(true));
    }

    @Test(expected = DomainError.class)
    public void userNewShouldNotAddLikePost() {
    	Optional<LikePost> emptyLikesList = Optional.empty();

        Mockito.when(userRepository.findOne(userNew.getId())).thenReturn(userNew);
        Mockito.when(likeRepository.findByUserAndPost(userNew, blogPost)).thenReturn(emptyLikesList);

        Assert.assertThat(blogService.addLikeToPost(userNew.getId(), blogPost.getId()), Matchers.equalTo(true));
    }

}
