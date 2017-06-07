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

import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {

    @Autowired
    private LikePostRepository likePostRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BlogPostRepository blogPostRepository;
    
    List<LikePost> likePosts;
    List<BlogPost> blogPosts;
    List<User> users;
    
    @Before
    public void setup() {
    	users = userRepository.findAll();
    	
    	BlogPost blogPost = new BlogPost();
    	blogPost.setEntry("blog pościk");
    	blogPost.setId(1L);
    	blogPost.setUser(users.get(0));
    	blogPostRepository.save(blogPost);
    	
    	blogPosts = blogPostRepository.findAll();
    	
    	LikePost likePost = new LikePost();
    	likePost.setId(2L);
    	likePost.setPost(blogPosts.get(0));
    	likePost.setUser(users.get(0));
    	likePostRepository.save(likePost);
    	
    	likePosts = likePostRepository.findAll();
    }
    
    @Test
    public void postEntityCreatesCorrectly() {
    	Assert.assertThat(likePosts, Matchers.hasSize(1));
    }
    
    @Test
    public void postEntityModifiedCorrectly() {
    	likePosts.get(0).getPost().setEntry("ZMIANA");
    	List<LikePost> likePostsAfter = likePostRepository.findAll();
    	String newEntry = likePostsAfter.get(0).getPost().getEntry();
    	
    	Assert.assertThat(newEntry, Matchers.equalTo("ZMIANA"));
    }
    
    @Test
    public void findByUserAndPostWorksCorrectly() {
    	Optional<LikePost> likePost = likePostRepository.findByUserAndPost(users.get(0), blogPosts.get(0));

    	Assert.assertThat(likePosts.get(0), Matchers.equalTo(likePost.get()));
    }
}
