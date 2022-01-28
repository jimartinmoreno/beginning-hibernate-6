//tag::preamble[]
package chapter03.application;

import chapter03.hibernate.Person;
import chapter03.hibernate.Ranking;
import org.hibernate.Session;

import java.util.Map;

public interface RankingService {
    int getRankingFor(String subject, String skill);

    void addRanking(String subject, String observer, String skill, int ranking);

    void updateRanking(String subject, String observer, String skill, int ranking);

    void removeRanking(String subject, String observer, String skill);

    Map<String, Integer> findRankingsFor(String subject);

    Person findBestPersonFor(String skill);

    //Ranking findRanking(Session session, String subject, String observer, String skill);
}
