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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

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
        user = createUser("Jan", "Kozioł", "john@gmail.com");
        post = createMockBlogPostBy(user);
    }

    @Test
    public void afterSaveElementRepositoryHaveThisElement() {
        // given
        userRepository.save(user);
        blogPostRepository.save(post);

        // when
        LikePost likePost = createLikePost(post, user);
        likePostRepository.save(likePost);
        post.getLikes().add(likePost);
        blogPostRepository.save(post);

        // then
        List<LikePost> likePosts = likePostRepository.findAll();
        assertThat(likePosts, hasSize(1));
        assertThat(likePosts.get(0), equalTo(likePost));
    }

    @Test
    public void modificateUserTest() {
        // given
        // Prepare repositories.
        userRepository.save(user);
        blogPostRepository.save(post);
        LikePost likePost = createLikePost(post, user);
        likePostRepository.save(likePost);
        post.getLikes().add(likePost);
        blogPostRepository.save(post);

        LikePost post = likePostRepository.findAll().get(0);

        // when
        User otherUser = createUser("Wojtek", "XYZ", "a@w.pl");
        userRepository.save(otherUser);
        post.setUser(otherUser);
        likePostRepository.save(post);

        // then
        post = likePostRepository.findAll().get(0);
        assertThat(post.getUser(), not(equalTo(user)));
        assertThat(post.getUser(), equalTo(otherUser));
    }

    private LikePost createLikePost(BlogPost post, User user) {
        LikePost like = new LikePost();
        like.setPost(post);
        like.setUser(user);

        return like;
    }

    private BlogPost createMockBlogPostBy(User user) {
        BlogPost post = new BlogPost();

        post.setEntry("test");
        post.setUser(user);
        post.setLikes(new ArrayList<>());

        return post;
    }

    private User createUser(String firstName, String lastName, String eMail) {
        User user = new User();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(eMail);
        user.setAccountStatus(AccountStatus.NEW);

        return user;
    }
}
