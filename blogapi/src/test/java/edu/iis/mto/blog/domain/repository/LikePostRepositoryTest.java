package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.User;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Wojciech Szczepaniak on 23.05.2017.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {
    @Autowired private LikePostRepository repository;
    @Autowired private UserRepository userRepository;
    @Autowired private BlogPostRepository blogPostRepository;

    private User user;
    private BlogPost post;

    @Before
    public void setUp() {
        createMockUser();
        createMockBlogPostBy(user);
    }

    private void createMockBlogPostBy(User user) {
        post = new BlogPost();
        post.setEntry("test");
        post.setId(1L);
        post.setUser(user);
    }

    private void createMockUser() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
    }
}
