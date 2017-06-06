package edu.iis.mto.blog.domain.repository;

import java.util.List;

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
        user.setFirstName("Jan");
        user.setEmail("john@domain.com");
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
    public void shouldFindExistingUser(){
    	entityManager.persist(user);

    	List<User> foundUsers = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(user.getFirstName(), "", user.getEmail());
    	Assert.assertTrue(foundUsers.contains(user));
    }
    
    @Test
    public void shouldFindExistingUserLowerCase(){
    	entityManager.persist(user);
    	List<User> foundUsers = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("john", "", "john@domain.com");
    	Assert.assertTrue(foundUsers.contains(user));
    }
    
    @Test
    public void shouldFindExistingUserUpperCase(){
    	entityManager.persist(user);
    	List<User> foundUsers = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("JOHN", "", "JOHN@DOMAIN.COM");
    	Assert.assertTrue(foundUsers.contains(user));
    }
    
    @Test
    public void shouldNotFindUser(){
    	entityManager.persist(user);
    	List<User> foundUsers = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("JOHN", "", "JOHNE@DOMAIN.COM");
    	Assert.assertFalse(foundUsers.contains(user));
    }
    
    @Test
    public void shouldFindExistingUserByEmail(){
    	entityManager.persist(user);
    	List<User> foundUsers = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("", "", "john@domain.com");
    	Assert.assertTrue(foundUsers.contains(user));
    }
    
    @Test
    public void shouldFindExistingUserByFirstName(){
    	entityManager.persist(user);
    	List<User> foundUsers = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("jan", "", "");
    	Assert.assertTrue(foundUsers.contains(user));
    }
}
