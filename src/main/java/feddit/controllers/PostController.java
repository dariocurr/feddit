package feddit.controllers;

import feddit.model.Comment;
import feddit.model.Post;
import feddit.model.ResultObject;
import feddit.security.FedditUserDetails;
import feddit.services.CommentService;
import feddit.services.PostService;
import feddit.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * this class is responsible for controls related to
 * adding and deleting posts and comments from the platform
 * @author Groupe A
 *
 * */
@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    /**
     * this method has the task of making it possible for a user to add a post. Furthermore,
     * the possibility that the post cannot be added due to some error will be considered,
     * managing everything through an instance of ResultObject
     *
     * @param userDetails
     * @param mav
     * @param redirectAttributes
     * @param post
     * @return a ModelAndView
     * @see ModelAndView
     *
     * */
    @PostMapping("/add_post")
    public ModelAndView addPost(@AuthenticationPrincipal FedditUserDetails userDetails,
                                       ModelAndView mav,
                                       RedirectAttributes redirectAttributes,
                                       Post post) {

        post.setUser(userService.findByUsername(userDetails.getUsername()));
        ResultObject result;
        if (postService.save(post)) {
            result = new ResultObject("S2", "success", "Post added successfully");
        } else {
            result = new ResultObject("E4", "error", "An error occured.");
        }
        redirectAttributes.addFlashAttribute("postResult", result);
        mav.setViewName("redirect:/");
        return mav;
    }

    /**
     * this method has the task of making it possible for a user to delete a post. Furthermore,
     * the possibility that the post cannot be deleted due to some error will be taken into consideration,
     * managing everything through an instance of ResultObject
     *
     * @param mav
     * @param redirectAttributes
     * @param id
     * @return a ModelAndView
     *
     * */
    @RequestMapping(value="/removePost/{id}", method = RequestMethod.DELETE)
    public ModelAndView deletePost(ModelAndView mav,
                                   RedirectAttributes redirectAttributes,
                                   @PathVariable long id) {

        ResultObject result;
        if (postService.deleteById(id)) {
            result = new ResultObject("S3", "success", "Post removed successfully");
        } else {
            result = new ResultObject("E9", "error", "An error occured.");
        }
        redirectAttributes.addFlashAttribute("postResult", result);
        mav.setViewName("redirect:/");
        return mav;
    }

    /**
     * this method has the task of instantiating a Post class object through the findById method of the postService
     * class attribute. Once this is done through the model instance,
     * in which it is possible to add attributes such as post and commentResult,
     * it will be possible to view a post
     *
     * @param model
     * @param id
     * @param commentResult
     * @return a String
     * @see Model
     *
     * */
    @GetMapping("/view_post")
    public String showPost(Model model,
                           @RequestParam("id") long id,
                           @ModelAttribute("commentResult") ResultObject commentResult) {

        Post post = this.postService.findById(id);
        model.addAttribute("post", post);
        model.addAttribute("commentResult", commentResult);
        return "post";
    }

    /**
     * this method has the task of adding a comment from a user. Thanks to an instance of the Comment class,
     * it is possible to set a content of this instance which is none other than the comment released by the user.
     * It also takes into account who is the parent object of this comment,
     * if it is another comment or directly the post.
     * To finish through the instance result, we check if the comment could be added or not.
     *
     * @param model
     * @param content
     * @param parentType
     * @param parentId
     * @param postId
     * @param userDetails
     * @param redirectAttributes
     * @return a String (an URL of the WEB page to be displayed)
     *
     * */
    @PostMapping("/add_comment")
    public String addComment(Model model,
                             @RequestParam("content") String content,
                             @RequestParam("parent_type") String parentType,
                             @RequestParam("parent_id") long parentId,
                             @RequestParam("post") long postId,
                             @AuthenticationPrincipal FedditUserDetails userDetails,
                             RedirectAttributes redirectAttributes) {

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(this.userService.findByUsername(userDetails.getUsername()));
        ResultObject result;
        if (parentType.equals("Comment")) {
            Comment parent = this.commentService.findById(parentId);
            comment.setComment(parent);
        } else if (parentType.equals("Post")) {
            Post parent = this.postService.findById(parentId);
            comment.setPost(parent);
        }
        if (commentService.save(comment)) {
            result = new ResultObject("S4", "success", "Comment added successfully");
        } else {
            result = new ResultObject("E5", "error", "An error occured.");
        }

        Post post = postService.findById(postId);
        model.addAttribute("post", post);
        redirectAttributes.addFlashAttribute("commentResult", result);
        return "redirect:/view_post?id=" + postId;
    }

    /**
     * this method has the task of deleting a comment of a user and through the instance result,
     * it is checked whether it was possible to delete the comment or not.
     *
     * @param model
     * @param commentId
     * @param postId
     * @param redirectAttributes
     * @return a String (an URL of the WEB page to be displayed)
     *
     * */
    @PostMapping("/delete_comment")
    public String deleteComment(Model model,
                                @RequestParam("id") long commentId,
                                @RequestParam("post") long postId,
                                RedirectAttributes redirectAttributes) {

        ResultObject result;
        if (this.commentService.deleteById(commentId)) {
            result = new ResultObject("S7", "success", "Comment removed successfully");
        } else {
            result = new ResultObject("E10", "error", "An error occured.");
        }
        model.addAttribute("post", postService.findById(postId));
        redirectAttributes.addFlashAttribute("commentResult", result);
        return "redirect:/view_post?id="+postId;
    }

}
