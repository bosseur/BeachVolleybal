package nl.bosseur.beachvolleybal.model.tournament;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by bosseur on 22/06/15.
 */
@Root(name = "BeachTournament")
public class BeachTournament implements Comparable<BeachTournament>, Serializable{

    @Attribute(name = "Name")
    private String name;

    @Attribute(name = "Title")
    private String tile;

    @Attribute(name = "CountryCode")
    private String country;

    @Attribute(name = "StartDateMainDraw", required = false, empty = "")
    private Date startDate;

    @Attribute(name = "EndDateMainDraw")
    private Date endDate;

    @Attribute(name="Gender")
    private int gender;

    @Attribute(name = "Type")
    private Integer type;

    @Attribute(name = "Code")
    private String tournamentCode;

    private String otherGenderTournamentCode;

    @Attribute(name = "No")
    private Integer number;

    @Attribute(name = "Version")
    private String version;

    @Attribute(name = "NoEvent")
    private int eventNumber;

    @Attribute(name = "Status")
    private int status;

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public Date getStartDate() {
        return startDate;
    }

    public int getEventNumber() {
        return eventNumber;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getTile() {
        return tile;
    }

    public Integer getNumber() {
        return number;
    }

    public Integer getType() {
        return type;
    }

    public int getStatus() {
        return status;
    }

    public String getMaleTournamentCode() {
        if(this.gender == 0 ) {return number.toString();}
        return otherGenderTournamentCode;
    }

    public String getFemaleTournamentCode() {
        if(this.gender == 1) {return number.toString();}
        return otherGenderTournamentCode;
    }

    public String getOtherGenderTournamentCode() {
        return otherGenderTournamentCode;
    }

    public void setOtherGenderTournamentCode(String otherGenderTournamentCode) {
        this.otherGenderTournamentCode = otherGenderTournamentCode;
    }

    public void setTournamentCode(String tournamentCode) {
        this.tournamentCode = tournamentCode;
    }

    public boolean isWorldTourEvent(){
        return this.getStatus() != 0 &&  (this.getType() == 0 ||
                this.getType() == 1 ||
                this.getType() == 4 ||
                this.getType() == 5 ||
                this.getType() == 32 ||
                this.getType() == 33) && this.getEventNumber() != 0;
    }

    @Override
    public String toString() {
        return "BeachTournament{" +
                "name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                "}\n";
    }

    @Override
    public int compareTo(BeachTournament another) {
        int compare = this.getStartDate().compareTo(another.getStartDate());
        return compare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BeachTournament that = (BeachTournament) o;

        if (eventNumber != that.eventNumber) return false;
        if (country != null ? !country.equals(that.country) : that.country != null) return false;
        return !(type != null ? !type.equals(that.type) : that.type != null);

    }

    @Override
    public int hashCode() {
        int result = country != null ? country.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + eventNumber;
        return result;
    }
}
