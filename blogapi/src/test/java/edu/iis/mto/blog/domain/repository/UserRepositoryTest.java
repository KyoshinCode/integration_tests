package edu.iis.mto.blog.domain.repository;

import java.util.List;

import antlr.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    public static final String EMPTY = "";
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
        user.setEmail("mike@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
    }

    @Test
    public void shouldFindUserByEmail() throws Exception {
        //given:
        repository.deleteAll();
        User persistedUser = entityManager.persist(user);
        String USER_EMAIL = "mike@domain2.com";

        //when:
        List<User> results = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(EMPTY, EMPTY, USER_EMAIL);

        //then:
        Assert.assertThat(persistedUser.getEmail(), equalTo(results.get(0).getEmail()));
        Assert.assertThat(results, Matchers.hasSize(1));
    }

    @Test
    public void shouldFindUserByLastName() throws Exception {
        //given:
        repository.deleteAll();
        User persistedUser = entityManager.persist(user);
        String USER_SURNAME = "Kowalski";

        //when:
        List<User> results = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(EMPTY, USER_SURNAME, EMPTY);

        //then:
        Assert.assertThat(persistedUser.getLastName(), equalTo(results.get(0).getLastName()));
        Assert.assertThat(results, Matchers.hasSize(1));
    }

    @Test
    public void shouldFindOneUserInRepository() {

        List<User> users = repository.findAll();

        Assert.assertThat(users, Matchers.hasSize(1));
    }

    @Test
    public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
        User persistedUser = entityManager.persist(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(user.getFirstName(),user.getLastName(),user.getEmail());

        Assert.assertThat(users, Matchers.hasSize(1));
        Assert.assertThat(users.get(0).getEmail(), equalTo(persistedUser.getEmail()));
    }

    @Test
    public void shouldStoreANewUser() {

        User persistedUser = repository.save(user);

        Assert.assertThat(persistedUser.getId(), Matchers.notNullValue());
    }

}
