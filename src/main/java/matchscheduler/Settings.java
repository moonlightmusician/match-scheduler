package matchscheduler;

import java.io.Serial;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

public class Settings implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final ArrayList<String> teams;
    private final LocalDate leagueStartDate;
    private final LocalDate leagueEndDate;
    private final ArrayList<DayOfWeek> selectedWeekdays;
    private final String leagueName;
    private final int numberOfMatchesPerDay;

    public LocalDate getLeagueStartDate() {
        return leagueStartDate;
    }

    public LocalDate getLeagueEndDate() {
        return leagueEndDate;
    }

    public ArrayList<DayOfWeek> getSelectedWeekdays() {
        return selectedWeekdays;
    }

    Settings(ArrayList<String> teams, String leagueStartDate, String leagueEndDate, ArrayList<String> selectedWeekdays, String leagueName, int numberOfMatchesPerDay) {
        this.teams = teams;
        this.leagueName = leagueName;
        this.numberOfMatchesPerDay = numberOfMatchesPerDay;
        this.leagueStartDate = LocalDate.parse(leagueStartDate);
        this.leagueEndDate = LocalDate.parse(leagueEndDate);
        this.selectedWeekdays = new ArrayList<>();
        for (String selectedWeekday : selectedWeekdays) {
            this.selectedWeekdays.add(DayOfWeek.valueOf(selectedWeekday));
        }
    }

    public String getLeagueName() {
        return leagueName;
    }

    public int getNumberOfMatchesPerDay() {
        return numberOfMatchesPerDay;
    }

    @Override
    public String toString() {
        String output = "";
        for (String team : teams) {
            output = team + "\n";
        }
        for (DayOfWeek selectedWeekday : selectedWeekdays) {
            output = selectedWeekday.toString() + "\n";
        }
        output = output.concat(leagueStartDate.toString()).concat("\n").concat(leagueEndDate.toString())
                .concat("\n").concat(leagueName).concat("\n").concat(Integer.toString(numberOfMatchesPerDay)).concat("\n");

        return output;
    }

    public ArrayList<String> getTeams() {
        return teams;
    }

}
