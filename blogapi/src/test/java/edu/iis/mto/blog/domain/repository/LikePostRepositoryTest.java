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
}