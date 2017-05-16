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
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Nowak");
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
    public void shouldFindUserIfFirstNameIsCorrect() {
    	
    	String differentLastName = "Kowalski";
    	String differentEmail = "kowalski@domain.com";
    	repository.save(user);
    	List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(user.getFirstName(), differentLastName, differentEmail);

        Assert.assertThat(users, Matchers.hasSize(1));
    }
    
    @Test
    public void shouldFindUserIfSecondNameIsCorrect() {
    	
    	String differentFirstName = "Janusz";
    	String differentEmail = "kowalski@domain.com";
    	repository.save(user);
    	List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(differentFirstName, user.getLastName(), differentEmail);

        Assert.assertThat(users, Matchers.hasSize(1));
    }
    
    @Test
    public void shouldFindUserIfEmailIsCorrect() {
    	
    	String differentFirstName = "Janusz";
    	String differentLastName = "Kowalski";
    	repository.save(user);
    	List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(differentFirstName, differentLastName, user.getEmail());

        Assert.assertThat(users, Matchers.hasSize(1));
    }

}
