package feddit.model;

import feddit.model.hierarchy.DatabaseObject;

import javax.persistence.*;

/**
 * represents a concretization of a DatabaseObject.
 * This class represents the votes that a user
 * can give to a comment or post
 *
 * @author Groupe A
 * @see feddit.model.hierarchy.DatabaseObject
 *
 * */
@Entity
@Table(name = "votes")
public class Vote extends DatabaseObject {

    public static final String UP_VOTE = "UP_VOTE";
    public static final String DOWN_VOTE = "DOWN_VOTE";

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "type")
    private String type;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public Vote() {}

    @Override
    public String getClazz() {
        return "Vote";
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

}
