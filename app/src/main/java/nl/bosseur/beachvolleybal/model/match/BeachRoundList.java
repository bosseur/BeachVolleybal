package nl.bosseur.beachvolleybal.model.match;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by nt21 on 01/07/2015.
 */
@Root(name = "BeachRounds", strict = false)
public class BeachRoundList {

    @ElementList(inline = true)
    private List<BeachRound> rounds;

    public List<BeachRound> getRounds() {
        return rounds;
    }

}
