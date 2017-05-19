package edu.iis.mto.blog.domain.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
public class LikeRepositoryTest {

	@Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LikePostRepository repository;

    @Autowired
    private BlogPostRepository blogPostRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    private User user;
    private BlogPost blogPost;
    private LikePost likePost;
	
	@Before
	public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Nowak");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
        
        blogPost = new BlogPost();
        List<LikePost> likes = new ArrayList<>();
        likes.add(likePost);
        blogPost.setUser(user);
        blogPost.setEntry("Post");
        blogPost.setLikes(likes);
        
        likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(blogPost);
	}

	@Test
    public void shouldFindNoLikesIfRepositoryIsEmpty() {

        List<LikePost> likes = repository.findAll();

        Assert.assertThat(likes, Matchers.hasSize(0));
    }
	
	@Test
    public void shouldFindOneLikePostIfRepositoryContainsOneLikePostEntity() {
		userRepository.save(user);
		blogPostRepository.save(blogPost);
		LikePost persistedLikePost = entityManager.persist(likePost);
        List<LikePost> likes = repository.findAll();

        Assert.assertThat(likes, Matchers.hasSize(1));
        Assert.assertThat(likes.get(0).getPost(), Matchers.equalTo(persistedLikePost.getPost()));
    }

	@Test
    public void shouldStoreANewLikePost() {

		userRepository.save(user);
		blogPostRepository.save(blogPost);
		LikePost persistedLikePost = repository.save(likePost);

        Assert.assertThat(persistedLikePost.getId(), Matchers.notNullValue());
    }
    
	@Test
	public void shouldFindLikesIfBothUserAndPostAreCorrect() {
		
		userRepository.save(user);
		blogPostRepository.save(blogPost);
		repository.save(likePost);
		
		Optional<LikePost> likes = repository.findByUserAndPost(user, blogPost);
		
		Assert.assertThat(likes.isPresent(), Matchers.equalTo(true));
	}
	
	@Test
	public void shouldNotFindLikesIfUserIsDifferent() {
		
		userRepository.save(user);
		blogPostRepository.save(blogPost);
		repository.save(likePost);
		
		User differentUser = new User();
		differentUser.setFirstName("Janusz");
		differentUser.setLastName("Kowalski");
		differentUser.setEmail("kowalski@domain.com");
		differentUser.setAccountStatus(AccountStatus.NEW);
		userRepository.save(differentUser);
		
		Optional<LikePost> likes = repository.findByUserAndPost(differentUser, blogPost);
		
		Assert.assertThat(likes.isPresent(), Matchers.equalTo(false));
	}
	
	@Test
	public void shouldNotFindLikesIfBlogPostIsDifferent() {
		
		userRepository.save(user);
		blogPostRepository.save(blogPost);
		repository.save(likePost);
		
		BlogPost differentBlogPost = new BlogPost();
        List<LikePost> list = new ArrayList<>();
        differentBlogPost.setUser(user);
        differentBlogPost.setEntry("Different post");
        differentBlogPost.setLikes(list);
        blogPostRepository.save(differentBlogPost);
		
		Optional<LikePost> likes = repository.findByUserAndPost(user, differentBlogPost);
		
		Assert.assertThat(likes.isPresent(), Matchers.equalTo(false));
	}

}
