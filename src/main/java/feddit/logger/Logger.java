package feddit.logger;

import feddit.model.Post;
import feddit.model.User;
import feddit.security.FedditUserDetails;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * this class has the task of keeping track of which user is performing one of
 * the possible actions at its disposal. It also keeps track of any possible
 * errors a user may encounter
 *
 * @author Groupe A
 *
 */
@Aspect
public class Logger {

    private static final Path path = Path.of("src/main/resources/log.txt");
    private final Set<Long> times = new HashSet<>();

    public Logger() {
    }

    @Pointcut("execution(public * feddit.services.AuthService.loadUserByUsername*(..))")
    private void loginPointcut() {
    }

    @Pointcut("execution(public * feddit.controllers.UserController.processSignUp*(..))")
    private void addUserPointcut() {
    }

    @Pointcut("execution(public * feddit.controllers.PostController.addPost*(..))")
    private void addPostPointcut() {
    }

    @Pointcut("execution(public * feddit.controllers.PostController.deletePost*(..))")
    private void deletePostPointcut() {
    }

    @Pointcut("execution(public * feddit.controllers.PostController.addComment*(..))")
    private void addCommentPointcut() {
    }

    @Pointcut("execution(public * feddit.controllers.PostController.deleteComment*(..))")
    private void deleteCommentPointcut() {
    }

    @Pointcut("execution(public * feddit.controllers.VoteController.processVote*(..))")
    private void addOrDeleteVotePointcut() {
    }

    @Pointcut("execution(public * org.springframework.data.repository.CrudRepository.save*(..))")
    private void databasePointcut() {
    }

    /**
     * this method has the task of saving in a file, what is the username of the
     * user who is trying to log in
     *
     * @param joinPoint
     * @return void
     * @see JoinPoint
     *
     */
    @Before("loginPointcut()")
    private void beforeLogin(JoinPoint joinPoint) {
        this.writeToFile("User with username '" + joinPoint.getArgs()[0] + "' is trying to login");
    }

    /**
     * this method has the task of saving in a file the username of the user who
     * managed to log in
     *
     * @param joinPoint
     * @return void
     *
     */
    @AfterReturning("loginPointcut()")
    private void afterLogin(JoinPoint joinPoint) {
        this.writeToFile("User with username '" + joinPoint.getArgs()[0] + "' is logged in");
    }

    /**
     * this method has the task of saving in a file the username of the user who was
     * unable to log in because his username does not exist
     *
     * @param joinPoint
     * @return void
     *
     */
    @AfterThrowing("loginPointcut()")
    private void afterLoginError(JoinPoint joinPoint) {
        this.writeToFile("User with username '" + joinPoint.getArgs()[0] + "' doesnt't exist");
    }

    /**
     * this method has the task of saving in a file the username of the user who
     * managed to join the web site
     *
     * @param joinPoint
     * @return void
     *
     */
    @After("addUserPointcut()")
    private void afterAddUser(JoinPoint joinPoint) {
        this.writeToFile("User with username '" + ((User) joinPoint.getArgs()[0]).getUsername() + "' joined Feddit");
    }

    /**
     * this method has the task of saving in a file the username of the user and the
     * title of a post he has created
     *
     * @param joinPoint
     * @return void
     *
     */
    @After("addPostPointcut()")
    private void afterAddPost(JoinPoint joinPoint) {
        this.writeToFile("User with username '" + ((FedditUserDetails) joinPoint.getArgs()[0]).getUsername()
                + "' creates post with title '" + ((Post) joinPoint.getArgs()[3]).getTitle() + "'");
    }

    /**
     * this method has the task of saving in a file the id of a post deleted by an
     * admin
     *
     * @param joinPoint
     * @return void
     *
     */
    @After("deletePostPointcut()")
    private void afterDeletePost(JoinPoint joinPoint) {
        this.writeToFile("Delete post with id " + joinPoint.getArgs()[2] + ". Removed by admin");
    }

    /**
     * this method has the task of saving in a file the username of a user, the
     * content of his comment and the ids of the objects where he left the comment
     *
     * @param joinPoint
     * @return void
     *
     */
    @After("addCommentPointcut()")
    private void afterAddComment(JoinPoint joinPoint) {
        this.writeToFile("User with username '" + ((FedditUserDetails) joinPoint.getArgs()[5]).getUsername()
                + "' creates comment with content '" + joinPoint.getArgs()[1] + "' related to "
                + joinPoint.getArgs()[2].toString().toLowerCase() + " with id " + joinPoint.getArgs()[3]
                + " in post with id " + joinPoint.getArgs()[4]);
    }

    /**
     * this method has the task of saving in a file the id of the comment deleted by
     * an admin or by the user who created the comment and all the comments related
     * to the first, if any, and the id of the post taken into consideration
     *
     * @param joinPoint
     * @return void
     *
     */
    @After("deleteCommentPointcut()")
    private void afterDeleteComment(JoinPoint joinPoint) {
        this.writeToFile("Delete comment with id " + joinPoint.getArgs()[1]
                + " and all related comments, if any, in Post with id " + joinPoint.getArgs()[2]
                + ". Removed by admin or user who created the comment");
    }

    /**
     * this method has the task of writing in a file the username of the user who
     * voted a post or a comment, then the method saves accordingly either the id of
     * a post or the id of a comment
     *
     * @param joinPoint
     * @return void
     *
     */
    @After("addOrDeleteVotePointcut()")
    private void afterPostVote(JoinPoint joinPoint) {
        String log = "User with username '" + ((FedditUserDetails) joinPoint.getArgs()[0]).getUsername() + "' "
                + (joinPoint.getArgs()[6].toString().toLowerCase());
        if ("post".equalsIgnoreCase(joinPoint.getArgs()[3].toString())) {
            log += " post with id " + joinPoint.getArgs()[2];
        } else {
            log += " comment with id " + joinPoint.getArgs()[2] + " related to post with id " + joinPoint.getArgs()[4];
        }
        this.writeToFile(log);
    }

    /**
     * this method has the task of keeping track of the execution time
     *
     * @param proceedingJoinPoint
     * @throws Throwable
     * @return void
     *
     */
    @Around("databasePointcut()")
    private void saveExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        proceedingJoinPoint.proceed();
        this.times.add(System.currentTimeMillis() - startTime);
        printTimeReport();
    }

    /**
     * this method has the task of writing in a file the average execution time of a
     * database operation
     *
     * @return void
     *
     */
    private void printTimeReport() {
        this.writeToFile("Average milliseconds required for database operations: "
                + (int) this.times.stream().mapToInt(x -> x.intValue()).average().getAsDouble());
    }

    /**
     * this method allows the other methods to be able to write to a file
     *
     * @param string
     * @throws IOException
     * @return void
     *
     */
    private void writeToFile(String string) {
        try {
            Files.writeString(Logger.path, new Date() + "\t" + string + "\n", StandardOpenOption.APPEND,
                    StandardOpenOption.CREATE);
        } catch (IOException e) {
            new IOException("Error: can't write to " + Logger.path);
        }
    }

}
