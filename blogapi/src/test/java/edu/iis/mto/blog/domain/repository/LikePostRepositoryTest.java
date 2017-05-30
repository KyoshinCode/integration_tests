package edu.iis.mto.blog.domain.repository;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LikePostRepository repository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BlogPostRepository blogRepository;
	
	User user;
	BlogPost post;
	LikePost likePost;
	
	@Before
	public void setup() {
		 	user = new User();
	        user.setFirstName("Jan");
	        user.setLastName("Nowak");
	        user.setEmail("john@domain.com");
	        user.setAccountStatus(AccountStatus.NEW);
	        
	        post = new BlogPost();
	        post.setEntry("new");
	        post.setUser(user);
	        
	        likePost = new LikePost();
	        likePost.setUser(user);
	        likePost.setPost(post);
	}
	
	@Test
	public void shouldNotFindLikePostsIfRepositoryIsEmpty() {
		
		userRepository.save(user);
		List<LikePost> likedPosts = repository.findAll();
		
		Assert.assertThat(likedPosts, Matchers.hasSize(0));
	}
	
	@Test 
	public void shouldFindOneLikePostInRepository() {
		
		userRepository.save(user);
		blogRepository.save(post);
		repository.save(likePost);
		List<LikePost> likedPosts = repository.findAll();
		
		Assert.assertThat(likedPosts, Matchers.hasSize(1));
	}
	
	@Test
	public void shouldModifyLikePost() {
	
		userRepository.save(user);
		blogRepository.save(post);
		repository.save(likePost);
		List<LikePost> likedPosts = repository.findAll();
		
		String oldPost = likedPosts.get(0).getPost().getEntry();
		likedPosts.get(0).getPost().setEntry("very new");
		String newPost = likedPosts.get(0).getPost().getEntry();
		
		Assert.assertThat(oldPost, Matchers.equalTo("new"));
		Assert.assertThat(newPost, Matchers.equalTo("very new"));
		
	}
	
	@Test
	public void shouldFindPostByUserAndPost() {
		
		userRepository.save(user);
		blogRepository.save(post);
		repository.save(likePost);
		Optional<LikePost> likedPosts = repository.findByUserAndPost(user, post);
		
		Assert.assertThat(likedPosts.isPresent() , Matchers.is(true));
	}
	
	@Test
	public void shouldNotFindPost_IncorrectUser() {
		
		userRepository.save(user);
		blogRepository.save(post);
		repository.save(likePost);
		
		User altUser = new User();
		altUser.setFirstName("Mike");
		altUser.setLastName("Wazowsky");
		altUser.setEmail("monsterscaryfications@mon.com");
		altUser.setAccountStatus(AccountStatus.CONFIRMED);
		userRepository.save(altUser);
		
		Optional<LikePost> likedPosts = repository.findByUserAndPost(altUser, post);
		
		Assert.assertThat(likedPosts.isPresent() , Matchers.is(false));
	}
	
	@Test
	public void shouldNotFindPost_IncorrectPost() {
		
		userRepository.save(user);
		blogRepository.save(post);
		repository.save(likePost);
		
		BlogPost altPost = new BlogPost();
		altPost.setEntry("OMG!");
		altPost.setUser(user);
		blogRepository.save(altPost);
		
		Optional<LikePost> likedPosts = repository.findByUserAndPost(user, altPost);
		
		Assert.assertThat(likedPosts.isPresent(), Matchers.is(false	));
	}
	
}
