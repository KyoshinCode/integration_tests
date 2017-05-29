package edu.iis.mto.blog.domain.repository;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;

public class LikePostRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    
    @Autowired
    private LikePostRepository repository;
	
	User user;
	
	@Before
	public void setup() {
		 	user = new User();
	        user.setFirstName("Jan");
	        user.setLastName("Nowak");
	        user.setEmail("john@domain.com");
	        user.setAccountStatus(AccountStatus.NEW);
	}
	
}
