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
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
    }

    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {

        List<User> users = repository.findAll();

        Assert.assertThat(users, Matchers.hasSize(1));
    }

    @Test
    public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
        User persistedUser = entityManager.persist(user);
        List<User> users = repository.findAll();

        Assert.assertThat(users, Matchers.hasSize(2));
        Assert.assertThat(users.get(1).getEmail(), Matchers.equalTo(persistedUser.getEmail()));
    }

    @Test
    public void shouldStoreANewUser() {

        User persistedUser = repository.save(user);

        Assert.assertThat(persistedUser.getId(), Matchers.notNullValue());
    }

    @Test
    public void shouldNotFindUser() {
        User persistedUser = entityManager.persist(user);
        List<User> foundUsersList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("fakeUserName","fakeLastName","fake@mail.com");
        Assert.assertThat(foundUsersList.isEmpty(), Matchers.is(true));
    }

    @Test
    public void findByFirstName() {
        User persistedUser = entityManager.persist(user);
        List<User> foundUsersList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Jan","#","#");
        Assert.assertThat(persistedUser.getFirstName(), Matchers.containsString(foundUsersList.get(0).getFirstName()));
    }

    @Test
    public void findByFirstNameThatIsSimilar() {
        User persistedUser = entityManager.persist(user);
        List<User> foundUsersList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("an","#","#");
        Assert.assertThat(persistedUser.getFirstName(), Matchers.containsString(foundUsersList.get(0).getFirstName()));
    }

    @Test
    public void findByLastName() {
        User persistedUser = entityManager.persist(user);
        List<User> foundUsersList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("#","Kowalski","#");
        Assert.assertThat(persistedUser.getFirstName(), Matchers.containsString(foundUsersList.get(0).getFirstName()));
    }

    @Test
    public void findByLastNameThatIsSimilar() {
        User persistedUser = entityManager.persist(user);
        List<User> foundUsersList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("#","Kowa","#");
        Assert.assertThat(persistedUser.getFirstName(), Matchers.containsString(foundUsersList.get(0).getFirstName()));
    }

    @Test
    public void findByEmail() {
        User persistedUser = entityManager.persist(user);
        List<User> foundUsersList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("#","#","john@domain.com");
        Assert.assertThat(persistedUser.getFirstName(), Matchers.containsString(foundUsersList.get(0).getFirstName()));
    }

    @Test
    public void findByEmailThatIsSimilar() {
        User persistedUser = entityManager.persist(user);
        List<User> foundUsersList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("#","#","@domain.com");
        Assert.assertThat(persistedUser.getFirstName(), Matchers.containsString(foundUsersList.get(0).getFirstName()));
    }

    @Test
    public void findByFirstNameLastNameAndEmail() {
        User persistedUser = entityManager.persist(user);
        List<User> foundUsersList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Jan","Kow","john@domain.com");
        Assert.assertThat(persistedUser.getFirstName(), Matchers.containsString(foundUsersList.get(0).getFirstName()));
    }
}
