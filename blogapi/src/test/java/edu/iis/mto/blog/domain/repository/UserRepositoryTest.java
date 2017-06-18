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
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

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
        assertThat(persistedUser.getEmail(), equalTo(results.get(0).getEmail()));
        assertThat(results, Matchers.hasSize(1));
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
        assertThat(persistedUser.getLastName(), equalTo(results.get(0).getLastName()));
        assertThat(results, Matchers.hasSize(1));
    }

    @Test
    public void shouldFindUserByFirstLetterOfName() throws Exception {
        //given:
        repository.deleteAll();
        User persistedUser = entityManager.persist(user);
        String FIRST_LETTER = "J";

        //when:
        List<User> results = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(FIRST_LETTER, EMPTY, EMPTY);

        //then:
        assertThat(persistedUser.getLastName(), equalTo(results.get(0).getLastName()));
        assertThat(results, Matchers.hasSize(1));
    }

    @Test
    public void shouldNotFindAnyUser() throws Exception {
        //given:
        repository.deleteAll();

        //when:
        List<User> results = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(EMPTY, EMPTY, EMPTY);

        //then:
        assertThat(results,hasSize(0));
    }

    @Test
    public void shouldFindOneUserInRepository() {
        repository.deleteAll();
        entityManager.persist(user);


        List<User> users = repository.findAll();

        assertThat(users, Matchers.hasSize(1));
    }

    @Test
    public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
        User persistedUser = entityManager.persist(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(user.getFirstName(),user.getLastName(),user.getEmail());

        assertThat(users, Matchers.hasSize(1));
        assertThat(users.get(0).getEmail(), equalTo(persistedUser.getEmail()));
    }

    @Test
    public void shouldStoreANewUser() {

        User persistedUser = repository.save(user);

        assertThat(persistedUser.getId(), Matchers.notNullValue());
    }

}
