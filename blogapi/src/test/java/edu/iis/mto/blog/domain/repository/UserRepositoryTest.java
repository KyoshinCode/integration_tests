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

//    @Ignore
    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {
    	
        List<User> users = repository.findAll();

        Assert.assertThat(users, Matchers.hasSize(0));
    }

//    @Ignore
    @Test
    public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
    	repository.deleteAll();
        User persistedUser = entityManager.persist(user);
        List<User> users = repository.findAll();

        Assert.assertThat(users, Matchers.hasSize(1));
        Assert.assertThat(users.get(0).getEmail(), Matchers.equalTo(persistedUser.getEmail()));
    }

//    @Ignore
    @Test
    public void shouldStoreANewUser() {

        User persistedUser = repository.save(user);

        Assert.assertThat(persistedUser.getId(), Matchers.notNullValue());
    }

    @Test
    public void shouldFindOneUserByFirstName() {
    	
    	User persistedUser = repository.save(user);
    	
    	List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Jan", "", "");
    	
    	Assert.assertThat(users, Matchers.hasSize(1));
    	Assert.assertThat(users.get(0).getFirstName(), Matchers.equalTo("Jan"));
    }
    
    @Test 
    public void shouldFindOneUserByLastName() {
    	
    	User persistedUser = repository.save(user);
    	
    	List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("", "Nowak", "");
    	
    	Assert.assertThat(users, Matchers.hasSize(1));
    	Assert.assertThat(users.get(0).getLastName(), Matchers.equalTo("Nowak"));
    }
    
    @Test
    public void shouldFindOneUserByFirstNameUpercases() {
    	
    	User persistedUser = repository.save(user);
    	
    	List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("JAN", "", "");
    	
    	Assert.assertThat(users, Matchers.hasSize(1));
    	Assert.assertThat(users.get(0).getFirstName(), Matchers.equalTo("Jan"));
    }
    
    @Test
    public void shouldNotFindUser() {
    	
    	repository.save(user);
    	
    	List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Piotr", "Piotr", "Piotr");
    	Assert.assertThat(users, Matchers.hasSize(0));
    	
    }
    
    
}
