package nl.bosseur.beachvolleybal.model.match;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by nt21 on 01/07/2015.
 */
@Root(name = "BeachMatches", strict = false)
public class TournamentMatchList {

    @ElementList(inline = true)
    private List<TournamentMatch> matches;

    public List<TournamentMatch> getMatches() {
        return matches;
    }
}
