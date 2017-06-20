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
import java.util.Optional;

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

    @Test
    public void modificatePostTest() {
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
        BlogPost otherPost = createMockBlogPostBy(otherUser);
        userRepository.save(otherUser);
        blogPostRepository.save(otherPost);
        post.setPost(otherPost);
        likePostRepository.save(post);

        // then
        post = likePostRepository.findAll().get(0);
        assertThat(post.getPost(), not(equalTo(post)));
        assertThat(post.getPost(), equalTo(otherPost));
    }

    @Test
    public void findByUserAndPostSearchInEmptyRepository() {
        // given
        userRepository.save(user);
        blogPostRepository.save(post);

        // when
        Optional<LikePost> returned = likePostRepository.findByUserAndPost(user, post);

        // then
        assertThat(returned.orElse(null), equalTo(null));
    }

    @Test
    public void findByUserAndPostSearchExistElementInRepository() {
        // given
        userRepository.save(user);
        blogPostRepository.save(post);

        LikePost likePost = createLikePost(post, user);
        likePostRepository.save(likePost);
        post.getLikes().add(likePost);
        blogPostRepository.save(post);

        User otherUser = createUser("Test", "test", "test@o2.pl");
        userRepository.save(otherUser);
        BlogPost otherPost = createMockBlogPostBy(otherUser);
        blogPostRepository.save(otherPost);
        LikePost otherLikePost = createLikePost(otherPost, otherUser);
        likePostRepository.save(otherLikePost);
        otherPost.getLikes().add(otherLikePost);
        blogPostRepository.save(otherPost);

        // when
        Optional<LikePost> returned = likePostRepository.findByUserAndPost(user, post);

        // then
        assertThat(returned.orElse(null), equalTo(likePost));
    }

    @Test
    public void findByUserAndPostSearchNonExistElementInRepository() {
        // given
        userRepository.save(user);
        blogPostRepository.save(post);

        User otherUser = createUser("Test", "test", "test@o2.pl");
        userRepository.save(otherUser);
        BlogPost otherPost = createMockBlogPostBy(otherUser);
        blogPostRepository.save(otherPost);
        LikePost otherLikePost = createLikePost(otherPost, otherUser);
        likePostRepository.save(otherLikePost);
        otherPost.getLikes().add(otherLikePost);
        blogPostRepository.save(otherPost);

        // when
        Optional<LikePost> returned = likePostRepository.findByUserAndPost(user, post);

        // then
        assertThat(returned.orElse(null), equalTo(null));
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
