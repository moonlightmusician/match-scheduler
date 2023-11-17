package matchscheduler;

import java.time.LocalDate;

/**
 * Represents a match up of two teams.
 *
 */
public class GamePairing {
    private String teamA;
    private String teamB;
    private LocalDate matchDay;
    private LocalDate matchDaySecondRound;

    public String getTeamA() {
        return teamA;
    }

    public void setTeamA(String teamA) {
        this.teamA = teamA;
    }

    public String getTeamB() {
        return teamB;
    }

    public void setTeamB(String teamB) {
        this.teamB = teamB;
    }

    public void setMatchDay(LocalDate matchDay) {
        this.matchDay = matchDay;
    }

    public LocalDate getMatchDaySecondRound() {
        return matchDaySecondRound;
    }

    public void setMatchDaySecondRound(LocalDate matchDaySecondRound) {
        this.matchDaySecondRound = matchDaySecondRound;
    }

    public GamePairing() {
    }

    /**
     * This method returns a string containing all the necessary information about the match between two teams.
     *
     * @return a string containing information about the match (name of the two teams, day of week, date)
     */
    public String asString() {
        if (teamA != null && teamB != null && matchDay != null && matchDaySecondRound != null) {
            return teamA + " vs " + teamB + " -  first round: " + matchDay.getDayOfWeek() + " " + matchDay + ", second round: " + matchDaySecondRound.getDayOfWeek() + " " + matchDaySecondRound;
        } else {
            return null;
        }
    }
}






