package nl.bosseur.beachvolleybal.model.match;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.math.BigDecimal;
import java.util.List;

/**
l * Created by bosseur on 22/06/15.
 */
@Root(strict = false)
public class Responses {

    @ElementList(inline = true)
    private List<BeachRoundList> rounds;

    @ElementList(inline = true)
    private List<TournamentMatchList> matches;

    @Attribute(name = "ServerTime", required = false)
    private BigDecimal servertime;

    public List<BeachRoundList> getRounds() {
        return rounds;
    }

    public List<TournamentMatchList> getMatches() {
        return matches;
    }
}
