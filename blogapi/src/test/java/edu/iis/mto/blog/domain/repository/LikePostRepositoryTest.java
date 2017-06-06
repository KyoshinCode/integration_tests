package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

/**
 * Created by Patryk Wierzyński.
 */

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected BlogPostRepository blogPostRepository;

    @Autowired
    protected LikePostRepository likePostRepository;

    private User user;
    private BlogPost blogPost;
    private LikePost likePost;

    @Before
    public void setUp() {
        List<User> users = userRepository.findAll();
        BlogPost blogPost = new BlogPost();
        blogPost.setEntry("old entry");
        blogPost.setUser(users.get(0));
        blogPost.setId((long) 3);
        blogPostRepository.save(blogPost);

        List<BlogPost> blogPosts = blogPostRepository.findAll();

        LikePost likePost = new LikePost();
        likePost.setPost(blogPosts.get(0));
        likePost.setUser(users.get(0));
        likePost.setId(null);
        likePostRepository.save(likePost);
    }

    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {
        likePostRepository.deleteAll();
        List<LikePost> posts = likePostRepository.findAll();

        Assert.assertThat(posts, Matchers.hasSize(0));
    }

    @Test
    public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
        List<LikePost> posts = likePostRepository.findAll();

        Assert.assertThat(posts, Matchers.hasSize(1));
    }

    @Test
    public void shouldModifyPostEntry() {
        List<LikePost> likePosts = likePostRepository.findAll();
        String newEntry = "new blog post entry";
        likePosts.get(0).getPost().setEntry(newEntry);

        Assert.assertThat(likePosts.get(0).getPost().getEntry(), Matchers.equalTo(newEntry));
    }

    @Test
    public void shouldFindLikePostByUserAndPost() {
        List<BlogPost> blogPosts = blogPostRepository.findAll();
        List<User> users = userRepository.findAll();
        Optional<LikePost> likePost = likePostRepository.findByUserAndPost(users.get(0), blogPosts.get(0));
        List<LikePost> likePosts = likePostRepository.findAll();

        Assert.assertThat(likePost.get(), Matchers.equalTo(likePosts.get(0)));
    }

}