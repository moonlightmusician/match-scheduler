package matchscheduler.exceptions;

/**
 * This exception is thrown if the user does not specify a league start date.
 *
 */
public class LeagueStartNotSetException extends Exception {
    public LeagueStartNotSetException() {
        super("League start date is missing.");
    }
}
