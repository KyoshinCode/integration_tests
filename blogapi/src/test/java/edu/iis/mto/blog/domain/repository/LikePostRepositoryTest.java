package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.BlogManager;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Created by Piotrek on 22.05.2017.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {
    private BlogManager blogManager;

    @Autowired
    private LikePostRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogPostRepository blogPostRepository;

    private LikePost likePost;
    private User user;
    private BlogPost blogPost;

    @Test
    public void postEntityCorrectCreate(){
        List<LikePost> foundLikePosts = repository.findAll();
        Assert.assertThat(foundLikePosts, Matchers.hasSize(1));
    }

    @Test
    public void postEntityCorrectModify(){
        List<LikePost> foundLikePosts = repository.findAll();
        String foundEntry = foundLikePosts.get(0).getPost().getEntry();
        foundLikePosts.get(0).getPost().setEntry("testing");
        String newEntry = foundLikePosts.get(0).getPost().getEntry();
        Assert.assertThat(foundEntry, Matchers.equalTo("test"));
        Assert.assertThat(newEntry, Matchers.equalTo("testing"));
    }

    @Test
    public void findByUserAndPostCorrectWorking(){
        List<BlogPost> foundBlogPost = blogPostRepository.findAll();
        List<User> foundUser = userRepository.findAll();
        List<LikePost> foundLikePost = repository.findAll();
        Optional<LikePost> likePost = repository.findByUserAndPost(foundUser.get(0), foundBlogPost.get(0));
        Assert.assertThat(likePost.get().getUser(), Matchers.equalTo(foundUser.get(0)));
    }
}