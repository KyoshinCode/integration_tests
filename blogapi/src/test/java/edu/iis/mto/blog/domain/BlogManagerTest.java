package edu.iis.mto.blog.domain;

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
	BlogPostRepository blogRepository;

	@MockBean
	LikePostRepository likeRepository;

	@Autowired
	DataMapper dataMapper;

	@Autowired
	BlogService blogService;

	User newUser;
	User confUser;
	BlogPost post;

	@Before
	public void setUp() {
		newUser = new User();
		newUser.setFirstName("Usr1");
		newUser.setLastName("Asd");
		newUser.setAccountStatus(AccountStatus.NEW);
		newUser.setId(5L);

		post = new BlogPost();
		post.setId(1L);
		post.setUser(newUser);
		post.setEntry("post entry");

		confUser = new User();
		confUser.setFirstName("Usr2");
		confUser.setLastName("Def");
		confUser.setAccountStatus(AccountStatus.CONFIRMED);
		confUser.setId(6L);

		Mockito.when(userRepository.findOne(newUser.getId())).thenReturn(newUser);
		Mockito.when(userRepository.findOne(confUser.getId())).thenReturn(confUser);
		Mockito.when(blogRepository.findOne(post.getId())).thenReturn(post);
		Optional<LikePost> list = Optional.empty();
		Mockito.when(likeRepository.findByUserAndPost(confUser, post)).thenReturn(list);
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
		boolean result = blogService.addLikeToPost(confUser.getId(), post.getId());

		Assert.assertThat(result, Matchers.is(true));
	}

}
