package nl.bosseur.beachvolleybal.model.util;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.bosseur.beachvolleybal.model.match.BeachRound;
import nl.bosseur.beachvolleybal.model.match.BeachRoundList;
import nl.bosseur.beachvolleybal.model.match.Responses;
import nl.bosseur.beachvolleybal.model.match.TournamentMatch;
import nl.bosseur.beachvolleybal.model.match.TournamentMatchList;

/**
 * Created by bosseur on 22/06/15.
 */
public class BeachMatchesXmlParser {


    public static List<List<BeachRound>> unmarschall(String source) throws Exception {
        Serializer serializer = new Persister();
        Responses response    = serializer.read(Responses.class, source);

        List<List<BeachRound>>  roundList       = new ArrayList<>();
        List<List<TournamentMatch>>  matchList  = new ArrayList<>();

        for(BeachRoundList listRounds: response.getRounds()){
            Collections.sort(listRounds.getRounds());
            roundList.add(listRounds.getRounds());
        }

        for(TournamentMatchList listMatches: response.getMatches() ){
            Collections.sort(listMatches.getMatches());
            matchList.add(listMatches.getMatches());
        }

        for (List<BeachRound> listRounds : roundList){
            for(BeachRound round : listRounds) {
                round_list:
                for (List<TournamentMatch> listMatches : matchList) {
                    for (TournamentMatch match : listMatches) {
                        if ( !round.getNumberTournament().equals(match.getNumberTournament()) ){
                            continue round_list;
                        }
                        if(round.getNumber() == match.getRound()) {
                            round.addMatch(match);
                        }
                    }
                }
            }
        }
        return roundList;
    }

    private static List<BeachRound> getMatchesTournament(List<TournamentMatchList> matches) {
        return null;
    }
}
