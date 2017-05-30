package edu.iis.mto.blog.domain;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.dto.PostData;
import edu.iis.mto.blog.dto.UserData;
import edu.iis.mto.blog.services.DataFinder;

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
@Service
public class BlogDataFinder extends DomainService implements DataFinder {

    @Override
    public UserData getUserData(Long userId) {
        User user = userRepository.findOne(userId);
        if (user == null) {
            throw new EntityNotFoundException(String.format("user with id %d does not exists", userId));
        }
        return mapper.mapToDto(user);
    }

    @Override
    public List<UserData> findUsers(String searchString) {
        List<User> users = userRepository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                searchString, searchString, searchString);
        
        Iterator<User> i = users.iterator();
        while (i.hasNext()) {
           User u = i.next();
           if (u.getAccountStatus().equals(AccountStatus.REMOVED)) {
        	   i.remove();
           }
        }

        return users.stream().map(user -> mapper.mapToDto(user)).collect(Collectors.toList());
    }

    @Override
    public PostData getPost(Long userId) {
        BlogPost blogPost = blogPostRepository.findOne(userId);
        return mapper.mapToDto(blogPost);
    }

    @Override
    public List<PostData> getUserPosts(Long userId) {
        User user = userRepository.findOne(userId);
    	if (user.getAccountStatus().equals(AccountStatus.REMOVED)) {
            throw new DomainError("user has been removed");
        }
        List<BlogPost> posts = blogPostRepository.findByUser(user);
        return posts.stream().map(post -> mapper.mapToDto(post)).collect(Collectors.toList());
    }

}
