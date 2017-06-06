package edu.iis.mto.blog.domain.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class LikePost extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "postId", nullable = false)
    private BlogPost post;

    public User getUser() {
        return user;
    }

    public LikePost setUser(User user) {
        this.user = user;
        return this;
    }

    public BlogPost getPost() {
        return post;
    }

    public LikePost setPost(BlogPost post) {
        this.post = post;
        return this;
    }

}
