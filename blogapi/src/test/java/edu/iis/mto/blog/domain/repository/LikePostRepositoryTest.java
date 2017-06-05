package edu.iis.mto.blog.domain.repository;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {

	@Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LikePostRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogPostRepository blogPostRepository;

    private User user;
    private BlogPost blogPost;
    private LikePost likePost;

	@Before
	public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Nowak");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);

        blogPost = new BlogPost();
        blogPost.setUser(user);
        blogPost.setEntry("Post");

        likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(blogPost);

        userRepository.save(user);
        blogPostRepository.save(blogPost);
	}

    @Test
    public void shouldFindOneLikePostIfRepositoryContainsOneLikePostEntity() {
        LikePost persistedLikePost = entityManager.persist(likePost);
        List<LikePost> likePosts = repository.findAll();

        Assert.assertThat(likePosts, Matchers.hasSize(1));
        Assert.assertThat(likePosts.get(0).getUser(), Matchers.equalTo(persistedLikePost.getUser()));
    }

    @Test
    public void shouldFindNoLikePostIfRepositoryIsEmpty() {
    	List<LikePost> likePosts = repository.findAll();

        Assert.assertThat(likePosts, Matchers.hasSize(0));
    }

    @Test
    public void shouldStoreANewLikePost() {
        LikePost persistedLikePost = repository.save(likePost);

        Assert.assertThat(persistedLikePost.getId(), Matchers.notNullValue());
    }
}