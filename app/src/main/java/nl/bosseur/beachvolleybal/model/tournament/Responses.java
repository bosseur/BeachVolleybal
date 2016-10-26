package nl.bosseur.beachvolleybal.model.tournament;

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

    @ElementList(name = "BeachTournaments")
    private List<BeachTournament> eventList;

    @Attribute(name = "ServerTime", required = false)
    private BigDecimal servertime;

    public List<BeachTournament> getTournaments() {
        return eventList;
    }
}
