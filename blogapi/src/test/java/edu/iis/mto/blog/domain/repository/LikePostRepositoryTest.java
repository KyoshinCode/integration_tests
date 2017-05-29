package edu.iis.mto.blog.domain.repository;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
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
	
}
