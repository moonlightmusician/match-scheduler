package matchscheduler.exceptions;

/**
 * This exception is thrown when the user has not selected any weekdays.
 *
 */
public class NoWeekdaysSelectedException extends Throwable {

    public NoWeekdaysSelectedException() {
        super("No weekdays are selected.");
    }
}
