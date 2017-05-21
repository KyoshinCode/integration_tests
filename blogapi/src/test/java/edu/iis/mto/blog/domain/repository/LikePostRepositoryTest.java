package edu.iis.mto.blog.domain.repository;

import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest 
{
    @Autowired
    private LikePostRepository likePostRepository;
	
    @Autowired
    private BlogPostRepository blogPostRepository;
    
    @Autowired
    private UserRepository repository;
    
	private User user;
	
	private LikePost likePost;

	private BlogPost post;

	private List<LikePost> likePosts;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("John");
        user.setEmail("johnas@domain.com");
        user.setAccountStatus(AccountStatus.CONFIRMED);
        repository.save(user);
        
		likePost = new LikePost();
        likePost.setUser(repository.findAll().get(0));
        likePost.setPost(blogPostRepository.findAll().get(0));
        likePostRepository.save(likePost);
        
        post = new BlogPost();
        post.setEntry("Second post");
        post.setUser(user);
        blogPostRepository.save(post);

    }
	@Test
	public void shouldFindOneLikePostIfRepositoryContainsOneLikePostEntity() {
        likePosts = likePostRepository.findAll();
        
        Assert.assertThat(likePosts, Matchers.hasSize(1));
	}
	
	@Test
	public void shouldFindLikePostWithChangedBlogPost() {
        likePosts = likePostRepository.findAll();
        likePosts.get(0).setPost(post);
        likePostRepository.save(likePosts.get(0));
        likePosts = likePostRepository.findAll();
        
        Assert.assertThat(likePosts.get(0).getPost().getEntry(), Matchers.equalTo(post.getEntry()));
	}
	
	@Test
	public void shouldFindLikePostWithChangedUser() {
		likePosts = likePostRepository.findAll();
        likePosts.get(0).setUser(user);
        likePostRepository.save(likePosts.get(0));
        likePosts = likePostRepository.findAll();
        
        Assert.assertThat(likePosts.get(0).getUser().getId(), Matchers.equalTo(user.getId()));
	}
	@Test
	public void shouldFindOneLikePostByUserAndPost() {
		likePosts = likePostRepository.findAll();
        likePosts.get(0).setPost(post);
        likePosts.get(0).setUser(user);
		Optional<LikePost> result = likePostRepository.findByUserAndPost(user, post);
		
		Assert.assertThat(result.isPresent(), Matchers.is(true));
	}
}