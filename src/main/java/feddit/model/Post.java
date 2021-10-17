package feddit.model;

import feddit.model.hierarchy.ForumObject;

import javax.persistence.*;

import java.util.Comparator;
import java.util.List;

/**
 * this class represents posts that users can create in the forum. Each post has
 * a title, a list of comments released by various users and votes
 *
 * @author Groupe A
 * @see feddit.model.hierarchy.ForumObject
 *
 */
@Entity
@Table(name = "posts")
public class Post extends ForumObject {

    @Column(name = "title")
    private String title;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<Vote> votes;

    public Post() {
    }

    @Override
    public String getClazz() {
        return "Post";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public List<Comment> getComments() {
        this.comments.sort(Comparator.comparingInt(c -> c.getDownVotes() - c.getUpVotes()));
        return this.comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

}
