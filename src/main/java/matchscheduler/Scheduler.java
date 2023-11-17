package matchscheduler;

import matchscheduler.exceptions.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Contains the logic for calculating the match pairings.
 *
 * @author Christian Slawinger
 */
public class Scheduler {

    /**
     * This method determines the necessary match pairings so that each team plays against each other.
     *
     * @param selectedWeekdays:      The days of the week when games take place.
     * @param teams:                 List of all teams participating in the league
     * @param leagueStartDate:       The date when the league should start
     * @param leagueEndDate:         The date when the league should end at the latest
     * @param numberOfMatchesPerDay: Number of games per matchday
     * @return ArrayList of game pairings
     */
    public ArrayList<GamePairing> calculateGamePairings(ArrayList<DayOfWeek> selectedWeekdays, ArrayList<String> teams, LocalDate leagueStartDate, LocalDate leagueEndDate, int numberOfMatchesPerDay) throws LeagueEndExceededException, LeagueStartNotSetException, LeagueEndNotSetException, TeamListEmptyException, NoWeekdaysSelectedException {


        // Determining the match pairings

        if(teams.size()==0){
            throw new TeamListEmptyException();
        }

        ArrayList<GamePairing> gamePairings = new ArrayList<>();

        for (int i = 0; i < teams.size(); i++) {
            for (String team : teams) {
                if (!teams.get(i).equals(team)) {
                    GamePairing gamePairing = new GamePairing();
                    gamePairing.setTeamA(teams.get(i));
                    gamePairing.setTeamB(team);
                    // does pairing exist ?
                    boolean pairingExists = false;
                    for (GamePairing checkPairing : gamePairings) {
                        if (
                                (gamePairing.getTeamA().equals(checkPairing.getTeamA()) && gamePairing.getTeamB().equals(checkPairing.getTeamB()))
                                        || (gamePairing.getTeamA().equals(checkPairing.getTeamB()) && gamePairing.getTeamB().equals(checkPairing.getTeamA()))

                        ) {
                            pairingExists = true;
                            break;
                        }
                    }
                    if (!pairingExists) {
                        gamePairings.add(gamePairing);
                    }
                }
            }
        }
        DayOfWeek dayOfWeek;
        if(leagueStartDate == null){
            throw new LeagueStartNotSetException();
        }else{
            dayOfWeek = leagueStartDate.getDayOfWeek();
        }


        LocalDate firstMatchDay;
        if (selectedWeekdays.contains(dayOfWeek)) {
            firstMatchDay = leagueStartDate;
        } else {
            firstMatchDay = leagueStartDate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
            for (DayOfWeek selectedWeekday : selectedWeekdays) {
                if (selectedWeekday.getValue() > dayOfWeek.getValue()) {
                    firstMatchDay = leagueStartDate.with(TemporalAdjusters.next(selectedWeekday));
                    break;
                }
            }
        }

        // now add the match days

        LocalDate nextMatchDay = null;
        LoopableSelectedWeekdays weekdays = new LoopableSelectedWeekdays(selectedWeekdays);

        for (int x = 0; x < gamePairings.size(); x++) {
            GamePairing gamePairing = gamePairings.get(x);
            if (x == 0) {
                weekdays.setCursor(firstMatchDay.getDayOfWeek());
            }
            if (x < numberOfMatchesPerDay) {
                gamePairing.setMatchDay(firstMatchDay);
                if (((x + 1) % numberOfMatchesPerDay) == 0) {
                    nextMatchDay = firstMatchDay.with(TemporalAdjusters.next(weekdays.getNext()));
                }
            } else {
                gamePairing.setMatchDay(nextMatchDay);
                if (((x + 1) % numberOfMatchesPerDay) == 0) {
                    nextMatchDay = Objects.requireNonNull(nextMatchDay).with(TemporalAdjusters.next(weekdays.getNext()));
                }
            }
            gamePairings.set(x, gamePairing);
        }
        // Now add the match days of the second round

        for (int x = 0; x < gamePairings.size(); x++) {
            GamePairing gamePairing = gamePairings.get(x);
            gamePairing.setMatchDaySecondRound(nextMatchDay);
            if (((x + 1) % numberOfMatchesPerDay) == 0) {
                nextMatchDay = Objects.requireNonNull(nextMatchDay).with(TemporalAdjusters.next(weekdays.getNext()));
            }
            gamePairings.set(x, gamePairing);
        }

        LocalDate dateOfLastMatch = gamePairings.get(gamePairings.size() - 1).getMatchDaySecondRound();

        if(leagueEndDate == null){
            throw new LeagueEndNotSetException();
        }
        if(leagueEndDate.isBefore(dateOfLastMatch)) {
            throw new LeagueEndExceededException("last match day exceeds league end date - please adjust");
        } else {
            return gamePairings;
        }
    }
}
