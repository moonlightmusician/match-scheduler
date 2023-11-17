package matchscheduler;
import matchscheduler.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UnitTests for Scheduler.java
 *
 */
class SchedulerTest {

    ArrayList<GamePairing> referenceA;
    ArrayList<GamePairing> referenceB;

    @BeforeEach
    void setUp() {
    }

    @Test
    void calculateGamePairings_with_4_teams_1_game_per_week() {
        referenceA = initReferenceA();
        Scheduler scheduler = new Scheduler();
        ArrayList<DayOfWeek> selectedWeekdays = new ArrayList<>();
        selectedWeekdays.add(DayOfWeek.SUNDAY);
        ArrayList<String> teams = new ArrayList<>(List.of("Team A", "Team B", "Team C", "Team D"));
        LocalDate leagueStartDate = LocalDate.parse("2020-01-01");
        LocalDate leagueEndDate = LocalDate.parse("2020-07-01");
        int numberOfMatchesPerDay = 1;

        ArrayList<GamePairing> gamePairings = null;
        try {
            gamePairings = scheduler.calculateGamePairings(selectedWeekdays, teams, leagueStartDate, leagueEndDate, numberOfMatchesPerDay);
        } catch (LeagueEndExceededException | LeagueStartNotSetException | LeagueEndNotSetException | TeamListEmptyException | NoWeekdaysSelectedException e) {
            e.printStackTrace();
        }
        assert gamePairings != null;
        if(gamePairings.size()>0){
            assertEquals(referenceA.size(), gamePairings.size());
            for (int i = 0; i < referenceA.size(); i++) {
                assertEquals(referenceA.get(i).getTeamA(), gamePairings.get(i).getTeamA());
                assertEquals(referenceA.get(i).getTeamB(), gamePairings.get(i).getTeamB());
                assertEquals(referenceA.get(i).getMatchDay(), gamePairings.get(i).getMatchDay());
                assertEquals(referenceA.get(i).getMatchDaySecondRound(), gamePairings.get(i).getMatchDaySecondRound());
            }
        }
    }

    @Test
    void calculateGamePairings_with_5_teams_2_games_a_day_3_games_per_week() {
        referenceB = initReferenceB();
        Scheduler scheduler = new Scheduler();
        ArrayList<DayOfWeek> selectedWeekdays = new ArrayList<>();
        selectedWeekdays.add(DayOfWeek.WEDNESDAY);
        selectedWeekdays.add(DayOfWeek.FRIDAY);
        selectedWeekdays.add(DayOfWeek.SUNDAY);
        ArrayList<String> teams = new ArrayList<>(List.of("Team A", "Team B", "Team C", "Team D", "Team E"));
        LocalDate leagueStartDate = LocalDate.parse("2020-01-01");
        LocalDate leagueEndDate = LocalDate.parse("2020-07-01");
        int numberOfMatchesPerDay = 2;

        ArrayList<GamePairing> gamePairings = null;
        try {
            gamePairings = scheduler.calculateGamePairings(selectedWeekdays, teams, leagueStartDate, leagueEndDate, numberOfMatchesPerDay);
        } catch (LeagueEndExceededException | LeagueStartNotSetException | LeagueEndNotSetException | TeamListEmptyException | NoWeekdaysSelectedException e) {
            e.printStackTrace();
        }
        assert gamePairings != null;
        if(gamePairings.size()>0){
            assertEquals(referenceB.size(), gamePairings.size());
            for (int i = 0; i < referenceB.size(); i++) {
                assertEquals(referenceB.get(i).getTeamA(), gamePairings.get(i).getTeamA());
                assertEquals(referenceB.get(i).getTeamB(), gamePairings.get(i).getTeamB());
                assertEquals(referenceB.get(i).getMatchDay(), gamePairings.get(i).getMatchDay());
                assertEquals(referenceB.get(i).getMatchDaySecondRound(), gamePairings.get(i).getMatchDaySecondRound());
            }
        }
    }

    private ArrayList<GamePairing> initReferenceA() {
        ArrayList<GamePairing> pairings = new ArrayList<>();
        pairings.add(new GamePairing("Team A", "Team B",  LocalDate.parse("2020-01-05"), LocalDate.parse("2020-02-16")));
        pairings.add(new GamePairing("Team A", "Team C",  LocalDate.parse("2020-01-12"), LocalDate.parse("2020-02-23")));
        pairings.add(new GamePairing("Team A", "Team D",  LocalDate.parse("2020-01-19"), LocalDate.parse("2020-03-01")));
        pairings.add(new GamePairing("Team B", "Team C",  LocalDate.parse("2020-01-26"), LocalDate.parse("2020-03-08")));
        pairings.add(new GamePairing("Team B", "Team D",  LocalDate.parse("2020-02-02"), LocalDate.parse("2020-03-15")));
        pairings.add(new GamePairing("Team C", "Team D",  LocalDate.parse("2020-02-09"), LocalDate.parse("2020-03-22")));
        return pairings;
    }

    private ArrayList<GamePairing> initReferenceB(){
        ArrayList<GamePairing> pairings = new ArrayList<>();
        pairings.add(new GamePairing("Team A", "Team B",  LocalDate.parse("2020-01-01"), LocalDate.parse("2020-01-12")));
        pairings.add(new GamePairing("Team A", "Team C",  LocalDate.parse("2020-01-01"), LocalDate.parse("2020-01-12")));
        pairings.add(new GamePairing("Team A", "Team D",  LocalDate.parse("2020-01-03"), LocalDate.parse("2020-01-15")));
        pairings.add(new GamePairing("Team A", "Team E",  LocalDate.parse("2020-01-03"), LocalDate.parse("2020-01-15")));
        pairings.add(new GamePairing("Team B", "Team C",  LocalDate.parse("2020-01-05"), LocalDate.parse("2020-01-17")));
        pairings.add(new GamePairing("Team B", "Team D",  LocalDate.parse("2020-01-05"), LocalDate.parse("2020-01-17")));
        pairings.add(new GamePairing("Team B", "Team E",  LocalDate.parse("2020-01-08"), LocalDate.parse("2020-01-19")));
        pairings.add(new GamePairing("Team C", "Team D",  LocalDate.parse("2020-01-08"), LocalDate.parse("2020-01-19")));
        pairings.add(new GamePairing("Team C", "Team E",  LocalDate.parse("2020-01-10"), LocalDate.parse("2020-01-22")));
        pairings.add(new GamePairing("Team D", "Team E",  LocalDate.parse("2020-01-10"), LocalDate.parse("2020-01-22")));
        return pairings;
    }
}