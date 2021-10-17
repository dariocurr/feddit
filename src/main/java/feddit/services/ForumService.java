package feddit.services;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;

/**
 * an interface for services with methods
 * used to carry out CRUD operations
 *
 * @author Groupe A
 *
 * */
public interface ForumService<T> {

    /**
     * method that is used by the various services
     * to get the relative repository
     *
     * @return a CrudRepository
     *
     * */
    CrudRepository<T, Long> getCrudRepository();


    /**
     * this method has the task of searching for a particular
     * object in its particular repository through an id
     *
     * @param id
     * @return a T object
     *
     * */
    default T findById(long id) {
        return this.getCrudRepository().findById(id).get();
    }

    /**
     * this method has the task of deleting a particular
     * object in its particular repository through an id
     *
     * @param id
     * @throws DataAccessException
     * @return a boolean
     *
     * */
    default boolean deleteById(long id) {
        try{
            this.getCrudRepository().deleteById(id);
            return true;
        } catch (DataAccessException dataAccessException) {
            return false;
        }
    }

    /**
     * this method has the task of saving a particular
     * object in its particular repository through an id
     *
     * @param t
     * @throws DataAccessException
     * @return a boolean
     *
     * */
    default boolean save(T t) {
        try {
            this.getCrudRepository().save(t);
            return true;
        } catch (DataAccessException dataAccessException) {
            return false;
        }
    }

}
