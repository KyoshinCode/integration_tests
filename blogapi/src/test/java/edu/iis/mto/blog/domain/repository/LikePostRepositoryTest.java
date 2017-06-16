package edu.iis.mto.blog.domain.repository;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
/**
 * Created by Piotr on 12.06.2017.
 */
public class LikePostRepositoryTest {

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private UserRepository repository;

    private User user;
    private LikePost likePost;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Nowak");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.CONFIRMED);
        repository.save(user);

        likePost = new LikePost();
        likePost.setUser(repository.findAll().get(0));
        likePost.setPost(blogPostRepository.findAll().get(0));

    }

}
