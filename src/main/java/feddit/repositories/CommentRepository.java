package feddit.repositories;

import feddit.model.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * interface representing a comments repository.
 * It extends CrudRepository because it is an interface
 * for general CRUD operations (CREATE, READ, UPDATE and DELETE)
 *
 * @author Groupe A
 * @see org.springframework.data.repository.CrudRepository
 *
 * */
@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {

}

