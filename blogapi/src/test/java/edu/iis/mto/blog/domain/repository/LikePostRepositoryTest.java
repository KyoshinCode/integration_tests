package edu.iis.mto.blog.domain.repository;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.domain.model.AccountStatus;
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

    }
	@Test
	public void shouldFindOneLikePostIfRepositoryContainsOneLikePostEntity() {
        likePostRepository.save(likePost);
        List<LikePost> likePosts = likePostRepository.findAll();
        
        Assert.assertThat(likePosts, Matchers.hasSize(1));
        
	}

}