package feddit.services;

import feddit.model.Vote;
import feddit.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

/**
 * this class has the task of returning
 * the votes repository
 *
 * @author Groupe A
 * @see feddit.services.ForumService
 *
 * */
@Service
public class VoteService implements ForumService<Vote> {

    @Autowired
    private VoteRepository voteRepository;

    /**
     * {@inheritDoc}
     *
     * */
    @Override
    public CrudRepository<Vote, Long> getCrudRepository() {
        return this.voteRepository;
    }

}
