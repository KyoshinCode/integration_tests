package edu.iis.mto.blog.domain.repository;

import java.util.List;
import java.util.Optional;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {
	@Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LikePostRepository repository;
    private LikePost likePost;
    private User user;
    private BlogPost blogPost;
    @Before
    public void setUp() {
        likePost = new LikePost();
        blogPost = new BlogPost();
        user = new User();

        user.setEmail("john@domain.com");
        user.setFirstName("John");
        user.setLastName("Steward");
        user.setAccountStatus(AccountStatus.NEW);
        blogPost.setUser(user);
        blogPost.setEntry("Testowy wpis");
        entityManager.persist(user);
        entityManager.persist(blogPost);

        likePost.setPost(blogPost);
        likePost.setUser(user);
    }

    @Test
    public void shouldFindNoLikePostsIfRepositoryIsEmpty() {
        List<LikePost> likePosts = repository.findAll();
        assertThat(likePosts, Matchers.hasSize(0));
    }
    @Test
    public void shouldFindOneLikePostIfRepositoryContainsOneLikePostEntity() {
        LikePost persistedLikePost = entityManager.persist(likePost);
        List<LikePost> likePosts = repository.findAll();

        assertThat(likePosts, Matchers.hasSize(1));
        assertThat(likePosts.get(0).getUser(), Matchers.equalTo(persistedLikePost.getUser()));
    }

    @Test
    public void shouldStoreANewLikePost() {
        LikePost persistedLikePost = repository.save(likePost);
        assertThat(persistedLikePost.getId(), Matchers.notNullValue());
    }

    @Test
    public void shouldModifyLikePostsBlogPost() {
        LikePost persistedLikePost = repository.save(likePost);
        BlogPost blogPost2 = new BlogPost();

        blogPost2.setUser(user);
        blogPost2.setEntry("drugi wpis");
        persistedLikePost.setPost(blogPost2);
        entityManager.persist(blogPost2);
        repository.save(persistedLikePost);

        List<LikePost> likePosts = repository.findAll();
        assertThat(likePosts.get(0).getPost(), Matchers.equalTo(blogPost2));
        assertThat(likePosts.get(0).getId(), Matchers.equalTo(persistedLikePost.getId()));
    }
    @Test
    public void shouldModifyLikePostsUser() {
        LikePost persistedLikePost = repository.save(likePost);
        User user = new User();

        user.setEmail("white@domain.com");
        user.setFirstName("Soul");
        user.setLastName("White");
        user.setAccountStatus(AccountStatus.NEW);
        entityManager.persist(user);

        persistedLikePost.setUser(user);
        repository.save(persistedLikePost);

        List<LikePost> likePosts = repository.findAll();
        assertThat(likePosts.get(0).getUser(), Matchers.equalTo(user));
        assertThat(likePosts.get(0).getId(), Matchers.equalTo(persistedLikePost.getId()));
    }

    @Test
    public void shouldFindExistingLikePostByUserAndPost(){
        entityManager.persist(likePost);

        Optional<LikePost> foundLikePosts = repository.findByUserAndPost(user,blogPost);
        assertTrue(foundLikePosts.isPresent());
        assertThat(foundLikePosts.get(), Matchers.equalTo(likePost));
    }
    @Test
    public void shouldNotFindLikePostByUserAndPost(){
        entityManager.persist(likePost);

        BlogPost blogPost2 = new BlogPost();
        blogPost2.setUser(user);
        blogPost2.setEntry("drugi wpis");
        entityManager.persist(blogPost2);

        Optional<LikePost> foundLikePosts = repository.findByUserAndPost(user,blogPost2);
        assertFalse(foundLikePosts.isPresent());
    }
}
