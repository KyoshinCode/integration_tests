package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.BlogPost;
import org.hamcrest.Matchers;
import org.junit.Assert;
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
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
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

        repository.save(user);
        blogPostRepository.save(blogPost);
        likePostRepository.save(likePost);
    }

    @Test
    public void shouldNotFindLikePosts_repositoryEmpty() {
        likePostRepository.deleteAll();
        List<LikePost> likePosts = likePostRepository.findAll();

        assertThat(likePosts, hasSize(0));
    }

    @Test
    public void shouldFindOneLikePost() {

        List<LikePost> likedPosts = likePostRepository.findAll();

        assertThat(likedPosts, hasSize(1));
    }

    @Test
    public void shouldModifyLikePost() {

        List<LikePost> likedPosts = likePostRepository.findAll();

        String oldPost = likedPosts.get(0).getPost().getEntry();
        likedPosts.get(0).getPost().setEntry("new new");
        String newPost = likedPosts.get(0).getPost().getEntry();

        assertThat(oldPost, equalTo("new"));
        assertThat(newPost, equalTo("new new"));
    }

    @Test
    public void shouldFindByUserAndPost() {

        Optional<LikePost> likedPosts = likePostRepository.findByUserAndPost(user, blogPost);
        assertThat(likedPosts.isPresent() , is(true));
    }
}
