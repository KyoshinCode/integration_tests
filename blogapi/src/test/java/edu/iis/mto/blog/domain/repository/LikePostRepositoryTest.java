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
    private LikePostRepository repository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BlogPostRepository blogPostRepository;
    
    @Test
    public void postEntityCreatesCorrectly() {
    	List<LikePost> foundLikePosts = repository.findAll();
    	Assert.assertThat(foundLikePosts, Matchers.hasSize(1));
    }
    
    @Test
    public void postEntityModifiedCorrectly() {
    	List<LikePost> foundLikePosts = repository.findAll();
    	String foundEntry = foundLikePosts.get(0).getPost().getEntry();
    	foundLikePosts.get(0).getPost().setEntry("Mateusz");
    	String newEntry = foundLikePosts.get(0).getPost().getEntry();
    	Assert.assertThat(foundEntry, Matchers.equalTo("something"));
    	Assert.assertThat(newEntry, Matchers.equalTo("Mateusz"));
    }
    
    @Test
    public void findByUserAndPostWorksCorrectly() {
    	List<BlogPost> foundBlogPosts = blogPostRepository.findAll();
    	List<User> foundUsers = userRepository.findAll();
    	Optional<LikePost> likePost = repository.findByUserAndPost(foundUsers.get(0), foundBlogPosts.get(0));
    	Assert.assertThat(likePost.get().getUser(), Matchers.equalTo(foundUsers.get(0)));
    }
}
