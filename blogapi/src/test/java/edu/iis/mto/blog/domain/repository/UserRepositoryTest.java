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
import edu.iis.mto.blog.domain.model.User;

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
        user.setFirstName("Matt");
        user.setEmail("matt@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
    }

    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {
    	repository.deleteAll();
        List<User> users = repository.findAll();
        Assert.assertThat(users, Matchers.hasSize(0));
    }

    @Test
    public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
    	repository.deleteAll();
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
    public void findByFullFirstName() {
    	repository.deleteAll();
    	User persistedUser = entityManager.persist(user);
    	List<User> foundUsers = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Matt", "", "");
    	Assert.assertThat(persistedUser.getFirstName(), Matchers.containsString(foundUsers.get(0).getFirstName()));
    }
    
    @Test
    public void findByFullEmail() {
    	repository.deleteAll();
    	User persistedUser = entityManager.persist(user);
    	List<User> foundUsers = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("", "", "matt@domain.com");
    	Assert.assertThat(persistedUser.getEmail(), Matchers.containsString(foundUsers.get(0).getEmail()));
    }
    
    @Test
    public void findByPartialFirstName() {
    	repository.deleteAll();
    	User persistedUser = entityManager.persist(user);
    	List<User> foundUsers = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Ma", "", "");
    	Assert.assertThat(persistedUser.getFirstName(), Matchers.containsString(foundUsers.get(0).getFirstName()));
    }
    
    @Test
    public void findByPartialEmail() {
    	repository.deleteAll();
    	User persistedUser = entityManager.persist(user);
    	List<User> foundUsers = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("", "", "@domain.com");
    	Assert.assertThat(persistedUser.getEmail(), Matchers.containsString(foundUsers.get(0).getEmail()));
    }
    
    @Test
    public void shouldNotFindUser() {
    	repository.deleteAll();
    	List<User> foundUsers = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("", "", "testtest@domain.com");
    	Assert.assertThat(foundUsers, Matchers.hasSize(0));
    }
}
