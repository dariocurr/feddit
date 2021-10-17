package feddit.services;

import feddit.model.Role;
import feddit.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * this class has the task of finding the roles
 * from the role repository through the user name
 *
 * @author Groupe A
 *
 * */
@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    /**
     * @param name
     * @return a Role
     *
     * */
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

}


