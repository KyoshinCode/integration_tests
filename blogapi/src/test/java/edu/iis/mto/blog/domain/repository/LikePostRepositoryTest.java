package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by Patryk Wierzyński.
 */

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {

	@Autowired
	protected UserRepository userRepository;

	@Autowired
	protected BlogPostRepository blogPostRepository;

	@Autowired
	protected LikePostRepository likePostRepository;

	private User user;
	private BlogPost blogPost;
	private LikePost likePost;

	@Before
	public void setUp() throws Exception {
		user = new User();
		user.setFirstName("Jan");
		user.setEmail("john2@domain.com");
		user.setAccountStatus(AccountStatus.CONFIRMED);


		blogPost = new BlogPost();
		blogPost.setEntry("post entry");

		likePost = new LikePost();
		likePost.setUser(user);
		likePost.setPost(blogPost);
	}

	// TODO: finish this test case!!!!

	@Test
	public void shouldFindNoUsersIfRepositoryIsEmpty() {
		likePostRepository.deleteAll();
		List<LikePost> posts = likePostRepository.findAll();

		Assert.assertThat(posts, Matchers.hasSize(0));
	}

	@Test
	public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
		likePostRepository.save(likePost);

		List<LikePost> posts = likePostRepository.findAll();

		Assert.assertThat(posts, Matchers.hasSize(1));
//		Assert.assertThat(users.get(0).getEmail(), Matchers.equalTo(persistedUser.getEmail()));
	}

	@Test
	public void shouldStoreANewUser() {
//		User persistedUser = likePostRepository.save(user);

//		Assert.assertThat(persistedUser.getId(), Matchers.notNullValue());
	}

}