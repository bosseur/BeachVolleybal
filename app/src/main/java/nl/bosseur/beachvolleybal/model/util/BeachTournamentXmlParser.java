package nl.bosseur.beachvolleybal.model.util;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.util.Collections;
import java.util.List;

import nl.bosseur.beachvolleybal.model.tournament.BeachTournament;
import nl.bosseur.beachvolleybal.model.tournament.Responses;

/**
 * Created by bosseur on 22/06/15.
 */
public class BeachTournamentXmlParser {


    public static List<BeachTournament> unmarschall(String source) throws Exception {
        Serializer serializer = new Persister();
        Responses response    = serializer.read(Responses.class, source);

        List<BeachTournament> eventList = response.getTournaments();
        Collections.sort(eventList);
        return eventList;
    }
}
