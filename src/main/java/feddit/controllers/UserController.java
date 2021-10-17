package feddit.controllers;

import feddit.model.Comment;
import feddit.model.ResultObject;
import feddit.model.Post;
import feddit.model.User;
import feddit.security.FedditUserDetails;
import feddit.services.PostService;
import feddit.services.RoleService;
import feddit.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * this class is responsible for controls related to everything that has to do with the user such as checking
 * the registration and login fields, changing passwords,
 * viewing user's account, signup form ,login form and home page
 *
 * @author Groupe A
 *
 * */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PostService postService;

    /**
     * this method has the task of displaying the home page where
     * you can view a list of posts sorted by up votes and down votes
     *
     * @param model
     * @param postResult
     * @return a String (an URL of the WEB page to be displayed)
     *
     * */
    @GetMapping("")
    public String viewHomePage(Model model,
                               @ModelAttribute("postResult") ResultObject postResult)
    {
        model.addAttribute("post", new Post());
        List<Post> posts = this.postService.findAll();
        posts.sort(Comparator.comparingInt(p -> p.getDownVotes() - p.getUpVotes()));
        model.addAttribute("posts", posts);
        model.addAttribute("postResult", postResult);
        return "index";
    }

    /**
     * this method has the task of giving the possibility to each user to be able to change their password through
     * the relative security controls. It also keeps track of any possible error
     * or possible success of the password change.
     * Possible missing errors in the controller are handled by JavaScript functions
     *
     * @param mav
     * @param redirectAttributes
     * @param oldPassword
     * @param newPassword
     * @return a ModelAndView
     *
     * */
    @RequestMapping(value = "/process_change_password", method = RequestMethod.POST)
    public ModelAndView processChangePassword(ModelAndView mav,
                                        RedirectAttributes redirectAttributes,
                                        @RequestParam("old_password") String oldPassword,
                                        @RequestParam("password") String newPassword) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User u = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        ResultObject result;
        if (!oldPassword.equals(newPassword)){
            if (passwordEncoder.matches(oldPassword, u.getPassword())){
                u.setPassword(passwordEncoder.encode(newPassword));
                if (userService.save(u)) {
                    result = new ResultObject("S1", "success", "Password changed successfully.");
                } else {
                    result = new ResultObject("E1", "error", "An error occured.");
                }
            } else {
                result = new ResultObject("E2", "error", "Old password isn't correct.");
            }
        } else {
            result = new ResultObject("E3", "error", "New password is equal to the old inserted.");
        }
        redirectAttributes.addFlashAttribute("passwordResult", result);
        mav.setViewName("redirect:/my_account");
        return mav;
    }

    /**
     * this method has the purpose of displaying the signup form
     *
     * @param model
     * @param usernameError
     * @param signupResult
     * @return a String (an URL of the WEB page to be displayed)
     *
     * */
    @GetMapping("/signup")
    public String showSignUpForm(Model model,
                                 @ModelAttribute("usernameError") String usernameError,
                                 @ModelAttribute("signupResult") ResultObject signupResult)
    {
        model.addAttribute("usernameError", usernameError);
        model.addAttribute("user", new User());
        model.addAttribute("signupResult", signupResult);
        return "signup";
    }

    /**
     * this method has the task of carrying out checks relating to
     * the data entered by the user regarding registration.
     * Controls not present in this method are handled by the front-end
     *
     * @param user
     * @param mav
     * @param redirectAttributes
     * @return a ModelAndView
     *
     * */
    @PostMapping("/process_signup")
    public ModelAndView processSignUp(User user,
                                      ModelAndView mav,
                                      RedirectAttributes redirectAttributes) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRoles(Set.of(roleService.findByName("USER")));
        ResultObject result = null;
        mav.setViewName("redirect:/signup");

        if(userService.findByUsername(user.getUsername()) == null) {
            if (isValidDate(user.getBirthDate())) {
                if (userService.save(user)) {
                    redirectAttributes.addFlashAttribute("name", user.getFirstName());
                    mav.setViewName("redirect:/signup_success");
                } else {
                    result = new ResultObject("E6", "error", "An error occured.");
                }
            } else {
                result = new ResultObject("E7", "error", "There is something strange with your birthday...");
            }
        } else {
            result = new ResultObject("E8", "error", "Username already exists.");
        }

        if (result != null)
            redirectAttributes.addFlashAttribute("signupResult", result);
        return mav;
    }

    /**
     * this method is invoked when the user's subscription is successful
     *
     * @param model
     * @param name
     * @return a String (an URL of the WEB page to be displayed)
     *
     * */
    @GetMapping("/signup_success")
    public String showSignUpSuccess(Model model,
                                    @ModelAttribute("name") String name) {
        model.addAttribute("name", name);
        return "signup_success";
    }

    /**
     * this method has the purpose of displaying the login form
     *
     * @return a String (an URL of the WEB page to be displayed)
     * */
    @RequestMapping("/login")
    public String showLogin() {
        return "login";
    }

    /**
     * this method has the task of displaying an
     * error if it was not possible to login
     *
     * @param model
     * @return a String (an URL of the WEB page to be displayed)
     *
     * */
    @RequestMapping("/login_error")
    public String showLoginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    /**
     * this method allows you to view your account
     * with user data, comments and posts
     *
     * @param model
     * @param userDetails
     * @param passwordResult
     * @return a String (an URL of the WEB page to be displayed)
     *
     * */
    @GetMapping("/my_account")
    public String showMyAccount(Model model,
                                @AuthenticationPrincipal FedditUserDetails userDetails,
                                @ModelAttribute("passwordResult") ResultObject passwordResult) {

        model.addAttribute("passwordResult", passwordResult);
        model.addAttribute("first_name", userDetails.getFirstName());
        model.addAttribute("last_name", userDetails.getLastName());
        model.addAttribute("email", userDetails.getEmail());
        model.addAttribute("birth_date", userDetails.getBirthDate());
        User user = userService.findByUsername(userDetails.getUsername());

        List<Post> posts = user.getPosts();
        Collections.sort(posts, (p1, p2) ->  p2.getCreationDate().compareTo(p1.getCreationDate()));

        List<Comment> comments = user.getComments();
        Collections.sort(comments, (c1, c2) ->  c2.getCreationDate().compareTo(c1.getCreationDate()));

        model.addAttribute("posts", posts);
        model.addAttribute("comments", comments);

        return "my_account";
    }

    /**
     * this method is used to check if the date of birth entered by the user is acceptable or not
     *
     * @param userDate
     * @return a boolean
     *
     * */
    private boolean isValidDate(Date userDate) {
        DateFormat format = new SimpleDateFormat("YYYY-MM-dd", Locale.ENGLISH);
        try {
            return userDate.compareTo(format.parse("2007-12-31")) < 0 && userDate.compareTo(format.parse("1921-01-01")) > 0;
        } catch (ParseException e) {
            return false;
        }
    }

}
