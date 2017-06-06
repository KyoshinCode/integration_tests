package edu.iis.mto.blog.domain.repository;


import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Assert;
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
    
    @Test
    public void postEntityCreatesCorrectly() {
    	List<LikePost> likePosts = likePostRepository.findAll();
    	Assert.assertThat(likePosts, Matchers.hasSize(1));
    }
    
    @Test
    public void postEntityModifiedCorrectly() {
    	List<LikePost> likePosts = likePostRepository.findAll();
    	likePosts.get(0).getPost().setEntry("ZMIANA");
	List<LikePost> likePostsAfter = likePostRepository.findAll();
    	String newEntry = likePostsAfter.get(0).getPost().getEntry();
    	Assert.assertThat(newEntry, Matchers.equalTo("ZMIANA"));
    }
    
    @Test
    public void findByUserAndPostWorksCorrectly() {
    	List<BlogPost> blogPosts = blogPostRepository.findAll();
    	List<User> users = userRepository.findAll();
	List<LikePost> likePosts = likePostRepository.findAll();

    	Optional<LikePost> likePost = likePostRepository.findByUserAndPost(users.get(0), blogPosts.get(0));

    	Assert.assertThat(likePosts.get(0), Matchers.equalTo(likePost.get()));
    }
}
