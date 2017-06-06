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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wojciech Szczepaniak on 23.05.2017.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {
    @Autowired private LikePostRepository likePostRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private BlogPostRepository blogPostRepository;

    private User user;
    private BlogPost post;

    @Before
    public void setUp() {
        createMockUser();
        createMockBlogPostBy(user);
    }

    @Test
    public void afterSaveElementRepositoryHaveThisElement() {
        // given
        userRepository.save(user);
        blogPostRepository.save(post);

        // when
        LikePost likePost = prepareLikePost(post, user);
        likePostRepository.save(likePost);
        post.getLikes().add(likePost);
        blogPostRepository.save(post);

        // than
        List<LikePost> likePosts = likePostRepository.findAll();
        Assert.assertThat(likePosts, Matchers.hasSize(1));
        Assert.assertThat(likePosts.get(0), Matchers.equalTo(likePost));
    }

    private LikePost prepareLikePost(BlogPost post, User user) {
        LikePost like = new LikePost();
        like.setId(1L);
        like.setPost(post);
        like.setUser(user);

        return like;
    }

    private void createMockBlogPostBy(User user) {
        post = new BlogPost();
        post.setEntry("test");
        post.setId(1L);
        post.setUser(user);
        post.setLikes(new ArrayList<>());
    }

    private void createMockUser() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
    }
}
