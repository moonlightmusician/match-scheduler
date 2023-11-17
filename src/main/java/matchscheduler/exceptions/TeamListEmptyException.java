package matchscheduler.exceptions;

/**
 * This exception is thrown when the user has not created a list of teams.
 *
 */
public class TeamListEmptyException extends Throwable {
    public TeamListEmptyException() {
        super("Team list is empty.");
    }
}
