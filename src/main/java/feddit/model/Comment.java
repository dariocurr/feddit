package feddit.model;

import feddit.model.hierarchy.ForumObject;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;

/**
 * this class represents the comments that a user can make under each post or in
 * response to another comment. Of each comment we are interested in its id to
 * identify it, the post it belongs to and the votes obtained
 *
 * @author Groupe A
 * @see feddit.model.hierarchy.ForumObject
 *
 */
@Entity
@Table(name = "comments")
public class Comment extends ForumObject {

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @OneToMany(mappedBy = "comment", orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "comment", orphanRemoval = true)
    private List<Vote> votes;

    public Comment() {
    }

    @Override
    public String getClazz() {
        return "Comment";
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
