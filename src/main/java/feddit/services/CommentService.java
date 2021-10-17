package feddit.services;

import feddit.model.Comment;
import feddit.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

/**
 * this class represents a service
 * related to comments
 *
 * @author Groupe A
 * @see feddit.services.ForumService
 *
 * */
@Service
public class CommentService implements ForumService<Comment> {

    @Autowired
    private CommentRepository commentRepository;

    /**
     * {@inheritDoc}
     *
     * */
    @Override
    public CrudRepository<Comment, Long> getCrudRepository() {
        return this.commentRepository;
    }
}
