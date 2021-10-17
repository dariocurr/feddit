package feddit.repositories;

import feddit.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.StreamSupport;

/**
 * interface representing a roles repository.
 * It extends CrudRepository because it is an interface
 * for general CRUD operations (CREATE, READ, UPDATE and DELETE)
 *
 * @author Groupe A
 * @see org.springframework.data.repository.CrudRepository
 *
 * */
@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

    default Role findByName(String description) {
        return StreamSupport
                .stream(this.findAll().spliterator(), false)
                .filter(role -> role.getDescription().equalsIgnoreCase(description))
                .findAny()
                .get();
    }

}
