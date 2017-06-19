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

    private User user;

    @Before
    public void setUp() {
        repository.deleteAll();
        user = new User();
        user.setFirstName("Adam");
        user.setLastName("Rezner");
        user.setEmail("adam@mail.com");
        user.setAccountStatus(AccountStatus.NEW);
    }

    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {

        List<User> users = repository.findAll();

        Assert.assertThat(users, Matchers.hasSize(0));
    }

    @Test
    public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
        User persistedUser = entityManager.persist(user);
        List<User> users = repository.findAll();

        Assert.assertThat(users, Matchers.hasSize(1));
        Assert.assertThat(users.get(0).getEmail(), Matchers.equalTo(persistedUser.getEmail()));
    }


    @Test
    public void shouldStoreANewUser() {

        User persistedUser = repository.save(user);

        Assert.assertThat(persistedUser.getId(), Matchers.notNullValue());
    }

    @Test
    public void shouldFindCorrectUserIfOnlySecondNameIsCorrect() {
        String firstName = "Adam";
        String email = "adam@mail.com";
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(firstName, user.getLastName(), email);
        Assert.assertThat(users, Matchers.hasSize(1));
       }

    @Test
    public void shouldFindCorrectUserIfOnlyFirstNameIsCorrect() {
        String secondName = "Rezner";
        String email = "adam@mail.com";
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(user.getFirstName(), secondName, email);
        Assert.assertThat(users, Matchers.hasSize(1));
    }

    @Test
    public void shouldFindCorrectUserIfOnlyEmailIsCorrect() {
        String firstName = "Adam";
        String secondName = "Rezner";
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(firstName, secondName, user.getEmail());
        Assert.assertThat(users, Matchers.hasSize(1));
    }

    @Test
    public void shouldNotFindAnyCorrectUser() {
        String firstName = "Dominik";
        String secondName = "Cieslak";
        String email = "dominik@mail.com";
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(firstName, secondName, email);
        Assert.assertThat(users, Matchers.hasSize(0));
    }
}
