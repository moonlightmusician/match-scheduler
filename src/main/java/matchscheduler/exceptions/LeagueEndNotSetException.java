package matchscheduler.exceptions;

/**
 * This exception is thrown if the user does not specify a league end date.
 *
 */
public class LeagueEndNotSetException extends Throwable {

    public LeagueEndNotSetException() {
        super("League end date is missing.");
    }
}
