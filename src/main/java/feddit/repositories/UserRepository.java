package feddit.repositories;

import feddit.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.stream.StreamSupport;

/**
 * interface representing a users repository.
 * It extends CrudRepository because it is an interface
 * for general CRUD operations (CREATE, READ, UPDATE and DELETE)
 *
 * @author Groupe A
 * @see org.springframework.data.repository.CrudRepository
 *
 * */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    default User findByUsername(String username) {
        return StreamSupport
                .stream(this.findAll().spliterator(), false)
                .filter(user -> user.getUsername().equals(username))
                .findAny().orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
