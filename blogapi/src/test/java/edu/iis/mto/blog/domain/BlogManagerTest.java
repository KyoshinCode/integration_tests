package edu.iis.mto.blog.domain;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.times;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.api.request.PostRequest;
import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.DataMapper;
import edu.iis.mto.blog.services.BlogService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

	@MockBean
	UserRepository userRepository;

	@MockBean
	BlogPostRepository blogPostRepository;

	@Autowired
	DataMapper dataMapper;

	@Autowired
	BlogService blogService;

	@Test
	public void creatingNewUserShouldSetAccountStatusToNEW() {
		blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
		ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
		Mockito.verify(userRepository).save(userParam.capture());
		User user = userParam.getValue();
		Assert.assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
	}


	@Test  //(expected = DomainError.class)
	public void shouldThrowDomainErrorIfNotConfirmedUserTriesToLikePost() {
		// Nie mam pojecia jak to zrobic, gdyz addLikeToPost siega do autowired repozytoriow, ktore sa puste i nie mam do nich referencji
	}
	
	@Test
	public void shouldAddLikeToPostIfUserConfirmed() {
		// Analogicznie nie wiem jak to zrobic
	}

}
