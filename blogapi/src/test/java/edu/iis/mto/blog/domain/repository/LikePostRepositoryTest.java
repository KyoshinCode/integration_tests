package edu.iis.mto.blog.domain.repository;

import static org.junit.Assert.*;

import java.util.ArrayList;
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
    private UserRepository userRepository;
    
    @Autowired
    private LikePostRepository likePostRepository;
    
    @Autowired
    private BlogPostRepository blogPostRepository;

    private User user;
    private LikePost likePost;
    private BlogPost blogPost;
    private List<LikePost> likePostList;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
        
        
        blogPost = new BlogPost();
        blogPost.setUser(user);
        blogPost.setEntry("Post");
        
        likePostList = new ArrayList();
        likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(blogPost);
        likePostList.add(likePost);
        
        blogPost.setLikes(likePostList);
    }

	@Test
	public void shouldFindLikePost() {
		userRepository.save(user);
		blogPostRepository.save(blogPost);
		LikePost persistedLikePost = entityManager.persist(likePost);
		likePostList = likePostRepository.findAll();
		
		Assert.assertThat(likePostList, Matchers.hasSize(1));
	}
	
	@Test
	public void shouldAddLikePost() {
		userRepository.save(user);
		blogPostRepository.save(blogPost);
		LikePost persistedLikePost = likePostRepository.save(likePost);
		entityManager.refresh(blogPost);
		
		Assert.assertThat(blogPost.getLikes().size(), Matchers.equalTo(1));
		Assert.assertThat(persistedLikePost.getId(), Matchers.equalTo(blogPost.getId()));
	}

}
