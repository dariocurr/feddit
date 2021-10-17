package feddit.repositories;

import feddit.model.Vote;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * interface representing a votes repository.
 * It extends CrudRepository because it is an interface
 * for general CRUD operations (CREATE, READ, UPDATE and DELETE)
 *
 * @author Groupe A
 * @see org.springframework.data.repository.CrudRepository
 *
 * */
@Repository
public interface VoteRepository extends CrudRepository<Vote, Long> {

}
