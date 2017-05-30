package edu.iis.mto.blog.domain.repository;

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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

/**
 * Created by Konrad Gos on 22.05.2017.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {
    @Autowired
    private LikePostRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Before
    public void setUp() {
        List<User> users = userRepository.findAll();
        BlogPost blogPost = new BlogPost();
        blogPost.setEntry("entry text");
        blogPost.setId(new Long((1)));
        blogPost.setUser(users.get(0));
        blogPostRepository.save(blogPost);

        List<BlogPost> blogPosts = blogPostRepository.findAll();

        LikePost likePost = new LikePost();
        likePost.setId(new Long(1));
        likePost.setPost(blogPosts.get(0));
        likePost.setUser(users.get(0));
        repository.save(likePost);
    }

    @Test
    public void creationLikePostEntity() {
        List<LikePost> likePosts = repository.findAll();
        Assert.assertThat(likePosts, Matchers.hasSize(1));
    }

    @Test
    public void modificationLikePostEntity() {
        List<LikePost> likePosts = repository.findAll();
        String currentEntry = likePosts.get(0).getPost().getEntry();
        likePosts.get(0).getPost().setEntry("new entry");

        repository.save(likePosts);

        likePosts = repository.findAll();
        String newEntry = likePosts.get(0).getPost().getEntry();

        Assert.assertThat(currentEntry, Matchers.equalTo("entry text"));
        Assert.assertThat(newEntry, Matchers.equalTo("new entry"));
    }

    @Test
    public void usageFindByUserAndPost() {
        List<BlogPost> blogPosts = blogPostRepository.findAll();
        List<User> users = userRepository.findAll();
        Optional<LikePost> likePost = repository.findByUserAndPost(users.get(0), blogPosts.get(0));

        Assert.assertThat(likePost.get().getUser(), Matchers.equalTo(users.get(0)));
    }
}
