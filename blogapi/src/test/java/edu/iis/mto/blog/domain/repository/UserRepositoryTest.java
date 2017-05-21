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

	private List<User> users;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
    }
    @Ignore
    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {
        List<User> users = repository.findAll();
        
        Assert.assertThat(users, Matchers.hasSize(0));
    }
    @Ignore
    @Test
    public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
        User persistedUser = entityManager.persist(user);
        List<User> users = repository.findAll();

        Assert.assertThat(users, Matchers.hasSize(1));
        Assert.assertThat(users.get(0).getEmail(), Matchers.equalTo(persistedUser.getEmail()));
    }
    @Ignore
    @Test
    public void shouldStoreANewUser() {

        User persistedUser = repository.save(user);

        Assert.assertThat(persistedUser.getId(), Matchers.notNullValue());
    }
    @Test
    public void shouldFindTwoUsersByLastName() {
		users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("*", "Steward", "*");
        final int testValue = 2;
        
		Assert.assertThat(users, Matchers.hasSize(testValue));
    }
    @Test
    public void shouldFindOneUserByFirstName() {
		users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Adam", "*", "*");
		final int testValue = 1;
		
        Assert.assertThat(users, Matchers.hasSize(testValue));
    }
    
    @Test
    public void shouldNotFindAnyUserByEmail() {
		users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("*", "*", "anyemail@anyaddress.com");
        final int testValue = 0;
        
		Assert.assertThat(users, Matchers.hasSize(testValue));
    }
    @Test
    public void shouldFindOneUserByFirstNamesFirstLetter() {
		users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("p", "*", "*");
        final int testValue = 1;
        
		Assert.assertThat(users, Matchers.hasSize(testValue));
    }
    @Test
    public void shouldFindOneUserByLastNamesMiddleLetter() {
    	users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("*", "v", "*");
        final int testValue = 1;
        
		Assert.assertThat(users, Matchers.hasSize(testValue));
    }
    @Test
    public void shouldFindAllUsersByPredicateInEmail() {
		users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("*", "*", "domain");
        final int testValue = 3;
        
		Assert.assertThat(users, Matchers.hasSize(testValue));
    }
}
