package matchscheduler;

import matchscheduler.exceptions.NoWeekdaysSelectedException;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Contains the days of the week selected by the user on which matches will take place. It is possible to loop through
 * the days of the week. This means that at the end of the week again a day is selected from a new week.
 *
 */
public class LoopableSelectedWeekdays {
    private DayOfWeek cursor;
    private final ArrayList<DayOfWeek> selectedWeekdays;
    LoopableSelectedWeekdays(ArrayList<DayOfWeek> selectedWeekdays) {
        this.selectedWeekdays = selectedWeekdays;
    }
    /**
     * provides the next playable day of the week.
     *
     * @return DayOfWeek dayofweek.
     */
    public DayOfWeek getNext() throws NoWeekdaysSelectedException {
        DayOfWeek current;
        if(selectedWeekdays.size()==0){
            throw new NoWeekdaysSelectedException();
        }
        Iterator<DayOfWeek> iter = selectedWeekdays.iterator();
        while (iter.hasNext()) {
            current = iter.next();
            if (current.getValue() > cursor.getValue()) {
                cursor = current;
                return current;
            }
        }
        iter = selectedWeekdays.iterator();
        current = iter.next();
        cursor = current;
        return current;
    }
    /**
     * sets a starting point for rotating through the days of the week selected by the user.
     *
     * @param day of the week.
     */
    public void setCursor(DayOfWeek day) {
        this.cursor = day;
    }
}
