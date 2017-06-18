package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {

    @Autowired
    private LikePostRepository likePostRepository;

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private BlogPost blogPost;
    private LikePost likePost;

    @Before
    public void setUp() {
        setupAndSaveUserForTests();

        setupSampleBlogPostAndLikePost();
    }

    private void setupSampleBlogPostAndLikePost() {
        blogPost = new BlogPost();
        blogPost.setUser(user);
        blogPost.setEntry("entry");

        likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(blogPost);
    }

    private void setupAndSaveUserForTests() {
        user = new User();

        user.setAccountStatus(AccountStatus.CONFIRMED);

        user.setFirstName("Jan");
        user.setLastName("Kowalski");

        user.setEmail("jankowalski@mail.com");

        userRepository.save(user);
    }

    @Test
    public void shouldNotFindAnyLikedPost() throws Exception {

        //when:
        List<LikePost> result = likePostRepository.findAll();

        //then:
        assertThat(result, hasSize(0));
    }

    @Test
    public void shouldFindOneLikedPost() throws Exception {
        //given:
        blogPostRepository.save(blogPost);
        likePostRepository.save(likePost);

        //when:
        List<LikePost> result = likePostRepository.findAll();

        //then:
        assertThat(result,hasSize(1));
    }
}
