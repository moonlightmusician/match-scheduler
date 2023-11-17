package matchscheduler.exceptions;

/**
 * This exception is thrown if the determined match pairings do not fit into the frame of league start and league end specified by the user.
 *
 */
public class LeagueEndExceededException extends Exception {
    public LeagueEndExceededException(String s) {
        super(s);
    }

    public LeagueEndExceededException() {
        super("LeagueEndDate exceeded.");
    }
}
