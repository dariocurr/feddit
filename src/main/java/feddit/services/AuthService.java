package feddit.services;

import feddit.model.User;
import feddit.security.FedditUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * this class represents a service
 * which allows you to load a user thanks
 * to his username
 *
 * @author Groupe A
 * @see org.springframework.security.core.userdetails.UserDetailsService
 *
 * */
public class AuthService implements UserDetailsService {

    @Autowired
    private UserService userService;

    /**
     *
     * @param username
     * @throws UsernameNotFoundException
     * @return a FedditUserDetails
     *
     * */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userService.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("User not found!");
        }
        return new FedditUserDetails(user);
    }

}
