package edu.iis.mto.blog.domain.repository;

import java.util.List;

import org.hamcrest.Matcher;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
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
        user.setEmail("jan@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
    }

    
    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {

        List<User> users = repository.findAll();

        assertThat(users, hasSize(0));
    }

    
    @Test
    public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
        User persistedUser = entityManager.persist(user);
        List<User> users = repository.findAll();
     
        for (User u : users) {
        	System.out.println(u.getFirstName() + " " + u.getLastName());
        }

        assertThat(users, hasSize(1));
        assertThat(users.get(0).getEmail(), equalTo(persistedUser.getEmail()));
    }

   
    @Test
    public void shouldStoreANewUser() {

        User persistedUser = repository.save(user);
        
        assertThat(persistedUser.getId(), notNullValue());
    }
    
    @Test
    public void shouldFindUserByName() {
    	repository.save(user);
    	
    	List<User> foundUsers = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase
    			("Jan", "", "");
    	
    	assertThat(foundUsers, hasSize(1));
    	
    	User foundUser =foundUsers.get(0);
    	
    	assertThat(user.getFirstName(), is(equalTo(foundUser.getFirstName())));
    	assertThat(user.getLastName(), is(equalTo(foundUser.getLastName())));
    	assertThat(user.getEmail(), is(equalTo(foundUser.getEmail())));
    	assertThat(user.getAccountStatus(), is(equalTo(foundUser.getAccountStatus())));
    }

}
