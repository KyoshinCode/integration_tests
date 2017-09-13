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

import java.util.List;
import java.util.Optional;



@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogPostRepository blogPostRepository;


    @Autowired
    private LikePostRepository likePostRepository;

    private LikePost likePost;

    @Before
    public void setUp() throws Exception {

        likePostRepository.deleteAll();

        List<User> users = userRepository.findAll();
        BlogPost blogPost = new BlogPost();
        blogPost.setEntry("Test for test");

        blogPost.setUser(users.get(0));
        blogPostRepository.save(blogPost);

        List<BlogPost> blogPosts = blogPostRepository.findAll();


        likePost = new LikePost();
        likePost.setPost(blogPosts.get(0));
        likePost.setUser(users.get(0));

    }

    @Test
    public void shouldFindOnePostInRepository() throws Exception {

        likePostRepository.save(likePost);
        List<LikePost> foundLikePosts = likePostRepository.findAll();
        Assert.assertThat(foundLikePosts, Matchers.hasSize(1));

    }

    @Test
    public void shouldFindLikePostUsingFindByUserAndPost() throws Exception {

        LikePost savedLikePost = likePostRepository.save(likePost);
        Optional<LikePost> foundLikePosts = likePostRepository.findByUserAndPost(userRepository.findAll().get(0),blogPostRepository.findAll().get(0));
        Assert.assertThat(foundLikePosts.get(),Matchers.equalTo(savedLikePost));

    }

    @Test
    public void shouldChangeUser() throws Exception {
        User user = new User();
        user.setFirstName("Rafal");
        user.setLastName("Franiewski");
        user.setEmail("194944@edu.p.lodz.pl");
        user.setAccountStatus(AccountStatus.NEW);
        User newUser = userRepository.save(user);
        LikePost savedLikePost = likePostRepository.save(likePost);
        Optional<LikePost> foundLikePosts = likePostRepository.findByUserAndPost(userRepository.findAll().get(0),blogPostRepository.findAll().get(0));
        Assert.assertThat(foundLikePosts.get(),Matchers.equalTo(savedLikePost));
        foundLikePosts.get().setUser(newUser);
        Assert.assertThat(foundLikePosts.get().getUser(),Matchers.equalTo(newUser));


    }
}