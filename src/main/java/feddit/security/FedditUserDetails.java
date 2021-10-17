package feddit.security;

import feddit.model.Comment;
import feddit.model.Post;
import feddit.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Groupe A
 * @see org.springframework.security.core.userdetails.UserDetails
 *
 * */
public class FedditUserDetails implements UserDetails {

    private User user;

    public FedditUserDetails(User user) {
        this.user = user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getDescription()))
                .collect(Collectors.toList());
    }

    public long getId() {return user.getId();}

    public String getFullName() {
        return user.getFirstName() + " " + user.getLastName();
    }

    public String getFirstName() {
        return this.user.getFirstName();
    }

    public String getLastName() {
        return this.user.getLastName();
    }

    public String getEmail() {
        return this.user.getEmail();
    }

    public Date getBirthDate() {
        return this.user.getBirthDate();
    }

    public List<Post> getPosts() {
        return this.user.getPosts();
    }

    public List<Comment> getComments() {
        return this.user.getComments();
    }

}
