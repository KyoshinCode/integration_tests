package edu.iis.mto.blog.domain.repository;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest

public class LikeRepositoryTest {
	
	@Autowired
    private TestEntityManager entityManager;
    
	@Autowired
    private UserRepository userRepository;
    
	@Autowired
    private LikePostRepository likePostRepository;
	
	@Autowired
    private BlogPostRepository blogPostRepository;
	
	private User firstUser;
	private User secondUser;
	private BlogPost post;
	private LikePost like;
	
	@Before
	
	public void setUp() {
		firstUser = new User();
		firstUser.setFirstName("Jan");
		firstUser.setEmail("jan@domain.com");
		firstUser.setAccountStatus(AccountStatus.CONFIRMED);
		firstUser = userRepository.save(firstUser);
		
		secondUser = new User();
		secondUser.setFirstName("Michal");
		secondUser.setEmail("michal@domain.com");
		secondUser.setAccountStatus(AccountStatus.CONFIRMED);
		secondUser = userRepository.save(secondUser);
		
		post = new BlogPost();
		post.setUser(firstUser);
		post.setEntry("Test");
		post = blogPostRepository.save(post);
	}
	
	@Test
	public void shouldFindOnePost() {

		LikePost likePost = new LikePost();
		likePost.setPost(post);
		likePost.setUser(secondUser);
		
		likePost = likePostRepository.save(likePost);
		
		entityManager.refresh(post);
		
		assertThat(post.getLikes(), hasSize(1));
		assertThat(post.getLikes().get(0).getId(), is(equalTo(likePost.getId())));
	}
	
	@Test
	public void shouldFindLikePostByUser() {
		LikePost likePost = new LikePost();
		likePost.setPost(post);
		likePost.setUser(secondUser);
		likePost = likePostRepository.save(likePost);
		
		entityManager.refresh(post);
		LikePost foundLikePost = likePostRepository.findByUserAndPost(secondUser, post).get();
		
		assertThat(foundLikePost, notNullValue());
		assertThat(foundLikePost.getId(), is(equalTo(likePost.getId())));
	}
	
}
