package nl.bosseur.beachvolleybal.model.match;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by nt21 on 26/06/2015.
 */
@Root(name = "BeachMatch", strict = false)
public class TournamentMatch implements Serializable, Comparable<TournamentMatch>{

    @Attribute(name = "NoTournament")
    private String numberTournament;

    @Attribute(name="NoInTournament")
    private int matchNumber;

    @Attribute(name = "NoTeamA" , empty = "0")
    private String teamA;

    @Attribute(name = "NoTeamB" , empty = "0")
    private String teamB;

    @Attribute(name = "NoRound")
    private int round;

    @Attribute(name = "LocalDate")
    private Date dateMatch;

    @Attribute(name = "LocalTime")
    private String time;

    @Attribute(name = "TeamAName")
    private String teamAName;

    @Attribute(name = "TeamBName")
    private String teamBName;

    @Attribute(name = "Court")
    private String court;

    @Attribute(name = "TeamAFederationCode", empty = "", required = false)
    private String teamAFederactionCode;

    @Attribute(name = "TeamBFederationCode", required = false, empty = "")
    private String teamBFederactionCode;

    @Attribute(name = "PointsTeamASet1", empty = "0", required = false)
    private String pointsTeamASet1;

    @Attribute(name = "PointsTeamASet2" , empty = "0", required = false)
    private String pointsTeamASet2;

    @Attribute(name = "PointsTeamASet3" , empty = "0", required = false)
    private String pointsTeamASet3;

    @Attribute(name = "PointsTeamBSet1" , empty = "0", required = false)
    private String pointsTeamBSet1;

    @Attribute(name = "PointsTeamBSet2" , empty = "0", required = false)
    private String pointsTeamBSet2;

    @Attribute(name = "PointsTeamBSet3" , empty = "0", required = false)
    private String pointsTeamBSet3;

    @Attribute(name = "DurationSet1" , empty = "0", required = false)
    private String durationSet1;

    @Attribute(name = "DurationSet2" , empty = "0", required = false)
    private String durationSet2;

    @Attribute(name = "DurationSet3" , empty = "0", required = false)
    private String durationSet3;

    @Attribute(name = "ResultType" , empty = "0", required = false)
    private int resultType;

    @Attribute(name = "TeamAPositionInMainDraw")
    private String teamAPositionInMainDraw;

    @Attribute(name = "TeamBPositionInMainDraw")
    private String teamBPositionInMainDraw;

    public String getTeamAFederactionCode() {
        return teamAFederactionCode;
    }

    public String getTeamBFederactionCode() {
        return teamBFederactionCode;
    }

    public String getTeamAPositionInMainDraw() {
        return teamAPositionInMainDraw;
    }

    public String getTeamBPositionInMainDraw() {
        return teamBPositionInMainDraw;
    }

    public int getMatchNumber() {
        return matchNumber;
    }

    public String getTeamA() {
        return teamA;
    }

    public String getTeamB() {
        return teamB;
    }

    public int getRound() {
        return round;
    }

    public Date getDateMatch() {
        return dateMatch;
    }

    public String getTime() {
        return time;
    }

    public String getTeamAName() {
        return teamAName;
    }

    public String getTeamBName() {
        return teamBName;
    }

    public String getCourt() {
        return court;
    }

    public String getPointsTeamASet1() {
        return pointsTeamASet1;
    }

    public String getPointsTeamASet2() {
        return pointsTeamASet2;
    }

    public String getPointsTeamASet3() {
        return pointsTeamASet3;
    }

    public String getPointsTeamBSet1() {
        return pointsTeamBSet1;
    }

    public String getPointsTeamBSet2() {
        return pointsTeamBSet2;
    }

    public String getPointsTeamBSet3() {
        return pointsTeamBSet3;
    }

    public String getDurationSet1() {
        return durationSet1;
    }

    public String getDurationSet2() {
        return durationSet2;
    }

    public String getDurationSet3() {
        return durationSet3;
    }

    public int getResultType() {
        return resultType;
    }

    public String getNumberTournament() {
        return numberTournament;
    }

    @Override
    public String toString() {
        return "TournamentMatch{" +
                "teamAName='" + teamAName + "\' vs " +
                " teamBName='" + teamBName + "\'" +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TournamentMatch that = (TournamentMatch) o;

        return matchNumber == that.matchNumber;

    }

    @Override
    public int hashCode() {
        return matchNumber;
    }

    @Override
    public int compareTo(TournamentMatch another) {
        if(this.matchNumber < another.matchNumber){
            return 1;
        }
        if(this.matchNumber > another.matchNumber){
            return -1;
        }
        return 0;
    }

    public String getTeamACode() {
        return this.teamAName + " " + teamAFederactionCode + " (" + teamAPositionInMainDraw + ")";
    }

    public String getTeamBCode() {
        return this.teamBName + " " + teamBFederactionCode + " (" + teamBPositionInMainDraw + ")";
    }
}
