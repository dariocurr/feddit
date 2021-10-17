package feddit.controllers;

import feddit.model.*;
import feddit.model.hierarchy.ForumObject;
import feddit.security.FedditUserDetails;
import feddit.services.CommentService;
import feddit.services.PostService;
import feddit.services.UserService;
import feddit.services.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional;

/**
 * this class has the task of implementing the system of likes (upvotes, downvotes)
 * that users leave to comments or posts and also
 * manages the related errors of this context
 *
 * @author Groupe A
 *
 * */
@Controller
public class VoteController {

    @Autowired
    private UserService userService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    /**
     * this method has the task of managing the count of users'
     * posts and comments and of managing the related errors
     *
     * @param userDetails
     * @param redirectAttributes
     * @param id
     * @param parentType
     * @param postId
     * @param page
     * @param voteType
     * @param model
     * @return a String (an URL of the WEB page to be displayed)
     *
     * */
    @PostMapping("/vote/{id}")
    public String processVote(@AuthenticationPrincipal FedditUserDetails userDetails,
                                        RedirectAttributes redirectAttributes,
                                        @PathVariable long id,
                                        @RequestParam("parent_type") String parentType,
                                        @RequestParam("post") long postId,
                                        @RequestParam("page") String page,
                                        @RequestParam("vote_type") String voteType,
                                        Model model) {

        User user = userService.findByUsername(userDetails.getUsername());
        ForumObject voted;
        Vote newVote = new Vote();
        newVote.setUser(user);
        newVote.setType(voteType);

        boolean isUpVote = voteType.equalsIgnoreCase(Vote.UP_VOTE);
        ResultObject result = null;

        if (parentType.equalsIgnoreCase("post")) {
            voted = postService.findById(id);
            newVote.setPost((Post) voted);
        } else {
            voted = commentService.findById(id);
            newVote.setComment((Comment) voted);
        }

        if (isUpVote) {
            voted.setUpVotes(voted.getUpVotes() + 1);
        } else {
            voted.setDownVotes(voted.getDownVotes() + 1);
        }

        Optional<Vote> previousVote = voted.getVotes().stream().filter(v -> v.getUser().equals(user)).findAny();

        if (previousVote.isPresent()) {
            if (!previousVote.get().getType().equalsIgnoreCase(newVote.getType())) {
                if (isUpVote) {
                    voted.setDownVotes(voted.getDownVotes() - 1);
                } else {
                    voted.setUpVotes(voted.getUpVotes() - 1);
                }
                if (!voteService.save(newVote)) {
                    result = new ResultObject("E10", "error", "An error occured.");
                }
            } else {
                if (isUpVote) {
                    voted.setUpVotes(voted.getUpVotes() - 2);
                } else {
                    voted.setDownVotes(voted.getDownVotes() - 2);
                }
            }
            if (!voteService.deleteById(previousVote.get().getId())) {
                result = new ResultObject("E11", "error", "An error occured.");
            }
        } else {
            if (!voteService.save(newVote)) {
                result = new ResultObject("E12", "error", "An error occured.");
            }
        }

        if (result != null) {
            redirectAttributes.addFlashAttribute("postResult", result);
        }

        if (parentType.equalsIgnoreCase("post")) {
            if (page.equalsIgnoreCase("index")) {
                return "redirect:/";
            } else {
                return "redirect:/view_post?id=" + postId;
            }
        } else {
            model.addAttribute("post", postService.findById(postId));
            return "redirect:/view_post?id=" + postId;
        }

    }

}
