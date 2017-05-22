package edu.iis.mto.blog.domain.repository;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Konrad Gos on 22.05.2017.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {
    @Autowired
    private LikePostRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogPostRepository blogPostRepository;

}
