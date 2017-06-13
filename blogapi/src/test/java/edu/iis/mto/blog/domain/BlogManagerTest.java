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
	BlogPostRepository blogRepository;

	@MockBean
	LikePostRepository likeRepository;

	@Autowired
	DataMapper dataMapper;

	@Autowired
	BlogService blogService;

	User postUser;
	User user;
	BlogPost post;

	@Before
	public void setUp() {
		postUser = new User();
		postUser.setFirstName("Usr1");
		postUser.setLastName("Asd");
		postUser.setAccountStatus(AccountStatus.CONFIRMED);
		postUser.setId(5L);

		post = new BlogPost();
		post.setId(1L);
		post.setUser(postUser);
		post.setEntry("post entry");

		user = new User();
		user.setFirstName("Usr2");
		user.setLastName("Def");
		user.setId(6L);

		Mockito.when(userRepository.findOne(postUser.getId())).thenReturn(postUser);
		Mockito.when(userRepository.findOne(user.getId())).thenReturn(user);
		Mockito.when(blogRepository.findOne(post.getId())).thenReturn(post);
		Optional<LikePost> list = Optional.empty();
		Mockito.when(likeRepository.findByUserAndPost(user, post)).thenReturn(list);
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
		user.setAccountStatus(AccountStatus.CONFIRMED);
		boolean result = blogService.addLikeToPost(user.getId(), post.getId());

		Assert.assertThat(result, Matchers.is(true));
	}

	@Test(expected = DomainError.class)
	public void newUserCannotLikePost() {
		user.setAccountStatus(AccountStatus.NEW);
		boolean result = blogService.addLikeToPost(user.getId(), post.getId());

		Assert.assertThat(result, Matchers.is(false));
	}

	@Test(expected = DomainError.class)
	public void removedUserCannotLikePost() {
		user.setAccountStatus(AccountStatus.REMOVED);
		boolean result = blogService.addLikeToPost(user.getId(), post.getId());

		Assert.assertThat(result, Matchers.is(false));
	}

}
