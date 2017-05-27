package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {

    @Autowired
    private LikePostRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogPostRepository blogPostRepository;
    @Autowired
    private TestEntityManager entityManager;


    private BlogPost post;
    private User user;

    @Before
    public void setUp() throws Exception {
        user = userRepository.findAll().get(0);
        post = new BlogPost();
        post.setUser(user);
        post.setEntry("Test post");
        post = blogPostRepository.save(post);
    }

    @Test
    public void createdLikeShouldBeInBlogPostLikesList() {
        LikePost likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(post);

        likePost = repository.save(likePost);

        entityManager.refresh(post);
        assertThat(post.getLikes()).isNotEmpty();
        assertThat(post.getLikes().get(0).getId()).isEqualTo(likePost.getId());
    }
}