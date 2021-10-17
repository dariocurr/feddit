package feddit.services;

import feddit.model.Post;
import feddit.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Groupe A
 * @see feddit.services.ForumService
 *
 * */
@Service
public class PostService implements ForumService<Post> {

    @Autowired
    private PostRepository postRepository;

    /**
     * this method has the task of returning a
     * list of posts from the post repository
     *
     * @return a List<Post>
     *
     * */
    public List<Post> findAll() {
        return (List<Post>) this.postRepository.findAll();
    }

    /**
     * {@inheritDoc}
     *
     * */
    @Override
    public CrudRepository<Post, Long> getCrudRepository() {
        return this.postRepository;
    }

}
