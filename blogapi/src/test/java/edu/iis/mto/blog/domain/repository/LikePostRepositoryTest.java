package edu.iis.mto.blog.domain.repository;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

import org.hamcrest.Matchers;
import org.junit.Assert;
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
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.model.LikePost;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {
	
	@Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;
    
    @Autowired
    private LikePostRepository likeRepository;
    
    @Autowired
    private BlogPostRepository blogRepository;

    private User user;
    private LikePost likePost;
    private BlogPost post;

    @Before
    public void setUp() {
    	repository.deleteAll();
        user = new User();
        user.setFirstName("Jan");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
        
        post = new BlogPost();
        post.setEntry("Tutaj nowy post");
        post.setUser(user);
        likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(post);
        
        entityManager.persist(user);
        entityManager.persist(post);
    }

    @Test
	public void shouldFindNoLikedPostsIfRepositoryIsEmpty() {
		
		repository.save(user);
		List<LikePost> likedPosts = likeRepository.findAll();
		
		Assert.assertThat(likedPosts, Matchers.hasSize(0));
	}
    
    @Test
    public void shouldFindOneLikeIfRepositoryContainsOneLikedPost() {
    	LikePost persistedLike = entityManager.persist(likePost);
        List<LikePost> likes = likeRepository.findAll();

        Assert.assertThat(likes, Matchers.hasSize(1));
        Assert.assertThat(likes.get(0).getUser(), Matchers.equalTo(persistedLike.getUser()));
    }
    
    @Test
    public void shouldStoreANewUser() {

        LikePost persistedLike = likeRepository.save(likePost);

        Assert.assertThat(persistedLike.getId(), Matchers.notNullValue());
    }
    
    
    @Test
	public void shouldCheckIfModifyOfLikePostWorks() {
	
		repository.save(user);
		blogRepository.save(post);
		likeRepository.save(likePost);
		List<LikePost> likes = likeRepository.findAll();
		
		String beforeModifyPost = likes.get(0).getPost().getEntry();
		likes.get(0).getPost().setEntry("To mój pierwszy post!");
		String afterModifyPost = likes.get(0).getPost().getEntry();
		
		Assert.assertThat(beforeModifyPost, Matchers.equalTo("Tutaj nowy post"));
		Assert.assertThat(afterModifyPost, Matchers.equalTo("To mój pierwszy post!"));
		
	}
    
    @Test
    public void shouldFindLikeByUserAndPost() throws Exception {
    	likeRepository.save(likePost);
        LikePost newLike = likeRepository.findByUserAndPost(user, post).get();
        Assert.assertThat(likePost.getId(), Matchers.equalTo(newLike.getId()));
    }

    @Test
    public void shouldNotFindLikesIfWrongPost() throws Exception {
        BlogPost blogPost2 = new BlogPost();
        blogPost2.setUser(user);
        blogPost2.setEntry("To mój drugi wpis!");
        entityManager.persist(blogPost2);

        Optional<LikePost> likes = likeRepository.findByUserAndPost(user,blogPost2);
        Assert.assertThat(likes.isPresent(), Matchers.is(false));
    }
    
    @Test
    public void shouldNotFindLikesIfWrongUser() throws Exception {
    	User exampleUser = new User();
    	exampleUser.setFirstName("Janek");
    	exampleUser.setEmail("johnny@domain.com");
    	exampleUser.setAccountStatus(AccountStatus.NEW);
    	repository.save(exampleUser);

        Optional<LikePost> likes = likeRepository.findByUserAndPost(exampleUser,post);
        Assert.assertThat(likes.isPresent(), Matchers.is(false));
    }

}
