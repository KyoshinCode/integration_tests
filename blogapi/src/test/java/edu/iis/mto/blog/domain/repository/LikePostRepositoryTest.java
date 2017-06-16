package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.BlogPost;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

/**
 * Created by Piotr on 12.06.2017.
 */
@RunWith(SpringRunner.class)
@DataJpaTest

public class LikePostRepositoryTest {

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private UserRepository repository;

    @Autowired
    private LikePostRepository likePostRepository;

    private User user;
    private BlogPost blogPost;
    private LikePost likePost;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Nowak");
        user.setEmail("jan@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
        repository.save(user);

        blogPost = new BlogPost();
        blogPost.setEntry("new");
        blogPost.setUser(user);

        likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(blogPost);

    }

    @Test
    public void shouldNotFindLikePosts_repositoryEmpty() {
        repository.save(user);
        List<LikePost> likePosts = likePostRepository.findAll();

        assertThat(likePosts, hasSize(0));
    }



}
