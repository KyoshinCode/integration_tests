package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    private User user1;
    private User user2;

    @Before
    public void setUp() {
        user1 = new User();
        user1.setFirstName("Jan");
        user1.setLastName("Kowalski");
        user1.setEmail("john@domain.com");
        user1.setAccountStatus(AccountStatus.NEW);

        user2 = new User();
        user2.setFirstName("Wojciech");
        user2.setLastName("Szczepaniak");
        user2.setEmail("wojtek@o2.pl");
        user2.setAccountStatus(AccountStatus.NEW);
    }

    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {

        List<User> users = repository.findAll();

        Assert.assertThat(users, Matchers.hasSize(0));
    }

    @Test
    public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
        User persistedUser = entityManager.persist(user1);
        List<User> users = repository.findAll();

        Assert.assertThat(users, Matchers.hasSize(1));
        Assert.assertThat(users.get(0).getEmail(), Matchers.equalTo(persistedUser.getEmail()));
    }

    @Test
    public void shouldStoreANewUser() {

        User persistedUser = repository.save(user1);

        Assert.assertThat(persistedUser.getId(), Matchers.notNullValue());
    }

    @Test
    public void findExistedUserByLastName() {
        User persistedUser = entityManager.persist(user1);
        entityManager.persist(user2);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("*", "Kowalski", "*");

        Assert.assertThat(users, Matchers.hasSize(1));
        Assert.assertThat(users.get(0), Matchers.equalTo(persistedUser));
    }

    @Test
    public void findExistedUserByFirstName() {
        User persistedUser = entityManager.persist(user1);
        entityManager.persist(user2);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Jan", "*", "*");

        Assert.assertThat(users, Matchers.hasSize(1));
        Assert.assertThat(users.get(0), Matchers.equalTo(persistedUser));
    }

    @Test
    public void findExistedUserByEmail() {
        User persistedUser = entityManager.persist(user2);
        entityManager.persist(user1);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("*", "*", "wojtek@o2.pl");

        Assert.assertThat(users, Matchers.hasSize(1));
        Assert.assertThat(users.get(0), Matchers.equalTo(persistedUser));
    }
}
