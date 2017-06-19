package edu.iis.mto.blog.domain.repository;

import java.util.List;

import org.hamcrest.Matchers;
import org.hamcrest.core.IsNot;
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
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
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
    public void shouldFindUserByName() {
    	repository.save(user);
    	List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(user.getFirstName(), "", "");
    	
    	Assert.assertThat(users, Matchers.hasSize(1));
    }
    
    @Test
    public void shouldFindUserBySurname() {
    	repository.save(user);
    	List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("", user.getLastName(), "");
    	
    	Assert.assertThat(users, Matchers.hasSize(1));
    }
    
    @Test
    public void shouldFindUserByEmail() {
    	repository.save(user);
    	List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("", "", user.getEmail());
    	
    	Assert.assertThat(users, Matchers.hasSize(1));
    }
    
    @Test
    public void shouldFindUserByOneRightParameter() {
    	repository.save(user);
    	List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Jan", "Kapibara", "Kapibara@awesome.com");
    	
    	Assert.assertThat(users, Matchers.hasSize(1));
    }
    
    @Test
    public void shouldNotFindUserByWrongInfo() {
    	repository.save(user);
    	List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("John", "Kapibara", "Kapibara@awesome.com");
    	
    	Assert.assertThat(users, Matchers.hasSize(0));
    }

}
