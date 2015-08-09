package nl.bosseur.beachvolleybal.model.match;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nt21 on 26/06/2015.
 */
@Root(name = "BeachRound", strict = false)
public class BeachRound implements Serializable, Comparable<BeachRound> {

    @Attribute(name = "NoTournament")
    private String numberTournament;

    @Attribute(name = "Code")
    private String code;

    @Attribute(name = "Name")
    private String name;

    @Attribute(name="Bracket")
    private String bracket;

    @Attribute(name = "Phase")
    private int phase;

    @Attribute(name = "StartDate")
    private Date start;

    @Attribute(name = "EndDate")
    private Date end;

    @Attribute(name = "No")
    private int number;

    @Attribute(required = false)
    private List<TournamentMatch> matches = new ArrayList<TournamentMatch>();

    public void addMatch(TournamentMatch match){
        this.matches.add(match);
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getBracket() {
        return bracket;
    }

    public int getPhase() {
        return phase;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public int getNumber() {
        return number;
    }

    public String getNumberTournament() {
        return numberTournament;
    }

    public List<TournamentMatch> getMatches() {
        return matches;
    }

    @Override
    public String toString() {
        return "BeachRound{" +
                "name='" + name + '\'' +
                '}' + this.matches.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BeachRound that = (BeachRound) o;

        return number == that.number;

    }

    @Override
    public int hashCode() {
        return number;
    }

    @Override
    public int compareTo(BeachRound another) {
        if(this.number < another.number){
            return 1;
        }
        if(this.number > another.number){
            return -1;
        }
        return 0;
    }
}
