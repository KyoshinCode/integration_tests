package edu.iis.mto.blog.domain.repository;
 
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {
	@Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LikePostRepository repository;
    private LikePost likePost;

    @Before
    public void setUp() {
        likePost = new LikePost();
        likePost.setPost(null);
        likePost.setUser(null);
    }

    @Test
    public void shouldFindNoLikePostsIfRepositoryIsEmpty() {
        List<LikePost> likePosts = repository.findAll();
        Assert.assertThat(likePosts, Matchers.hasSize(0));
    }
}