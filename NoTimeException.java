/**
 * @author Damian Ugalde
 * @date 2020-05-03
 * @version 1.0
 *
 * Project 4 - Isolation
 * CS 4200 - Artificial Intelligence
 * California State Polytechnic University, Pomona
 * Computer Science Department
 *
 * Instructor: Dominick A. Atanasio
 *
 */
public class NoTimeException extends Exception {

    private static final long serialVersionUID = 88L;

    /**
     * Creates a NoTimeException with the default message.
     * This is used to notify the Agent that it has run out of time to search
     * and must return a result now.
     */
    public NoTimeException(){
        super("The timer has expired. The computer must make a move now.");
    }

}