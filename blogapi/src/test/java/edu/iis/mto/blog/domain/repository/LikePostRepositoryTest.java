package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

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


    @Before
    public void setUp() {
        setupAndSaveUserForTests();

        setupAndSaveSamplePost();
    }

    private void setupAndSaveSamplePost() {
        LikePost likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(blogPostRepository.findAll().get(0));
    }

    private void setupAndSaveUserForTests() {
        user = new User();

        user.setAccountStatus(AccountStatus.CONFIRMED);

        user.setFirstName("Jan");
        user.setLastName("Kowalski");

        user.setEmail("jankowalski@mail.com");

        userRepository.save(user);
    }
}
