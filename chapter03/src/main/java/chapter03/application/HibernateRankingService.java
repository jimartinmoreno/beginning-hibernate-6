package chapter03.application;

import chapter03.hibernate.Person;
import chapter03.hibernate.Ranking;
import chapter03.hibernate.Skill;
import com.autumncode.hibernate.util.SessionUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

public class HibernateRankingService implements RankingService {

    @Override
    public int getRankingFor(String subject, String skill) {
        try (Session session = SessionUtil.getSession()) {
            Query<Ranking> query = session.createQuery("from Ranking r where r.subject.name=:name and r.skill.name=:skill",
                    Ranking.class);
            query.setParameter("name", subject);
            query.setParameter("skill", skill);

            IntSummaryStatistics stats = query.list().stream().collect(summarizingInt(Ranking::getRanking));

            return (int) stats.getAverage();
        }
    }

    @Override
    public void addRanking(String subjectName, String observerName, String skillName, int rank) {
        try (Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();

            addRanking(session, subjectName, observerName, skillName, rank);

            tx.commit();
        }
    }

    private void addRanking(Session session, String subjectName, String observerName, String skillName, int rank) {
        Person subject = savePerson(session, subjectName);
        Person observer = savePerson(session, observerName);
        Skill skill = saveSkill(session, skillName);

        Ranking ranking = new Ranking();
        ranking.setSubject(subject);
        ranking.setObserver(observer);
        ranking.setSkill(skill);
        ranking.setRanking(rank);
        session.save(ranking);
    }

    @Override
    public void updateRanking(String subject, String observer, String skill, int rank) {
        try (Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();

            Ranking ranking = findRanking(session, subject, observer, skill);
            if (ranking == null) {
                addRanking(session, subject, observer, skill, rank);
            } else {
                ranking.setRanking(rank);
            }
            tx.commit();
        }
    }

    @Override
    public void removeRanking(String subject, String observer, String skill) {
        try (Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();

            removeRanking(session, subject, observer, skill);

            tx.commit();
        }
    }

    private void removeRanking(Session session, String subject, String observer, String skill) {
        Ranking ranking = findRanking(session, subject, observer, skill);
        if (ranking != null) {
            session.delete(ranking);
        }
    }

    @Override
    public Map<String, Integer> findRankingsFor(String subject) {
        try (Session session = SessionUtil.getSession()) {
            Map<String, Integer> results = new HashMap<>();

            Query<Ranking> query = session.createQuery("from Ranking r where r.subject.name=:subject order by r.skill.name",
                    Ranking.class);
            query.setParameter("subject", subject);

            List<Ranking> rankings = query.list();

            Map<String, Double> map = rankings.stream()
                    .collect(groupingBy(ranking -> ranking.getSkill().getName(),
                            averagingInt(Ranking::getRanking)));

            System.out.println("map = " + map);

            String lastSkillName = "";
            int sum = 0;
            int count = 0;
            for (Ranking r : rankings) {
                if (!lastSkillName.equals(r.getSkill().getName())) {
                    sum = 0;
                    count = 0;
                    lastSkillName = r.getSkill().getName();
                }
                sum += r.getRanking();
                count++;
                results.put(lastSkillName, sum / count);
            }
            System.out.println("results = " + results);
            return results;
        }
    }


    @Override
    public Person findBestPersonFor(String skill) {
        Person person = null;
        try (Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();

            Query<Object[]> query = session.createQuery("select r.subject.name, avg(r.ranking)" +
                            " from Ranking r where r.skill.name=:skill group by r.subject.name order by avg(r.ranking) desc",
                    Object[].class);
            query.setParameter("skill", skill);
            query.setMaxResults(1);

            List<Object[]> result = query.list();
            if (!result.isEmpty()) {
                // we want the first (and only) row
                Object[] row = result.get(0);
                String personName = row[0].toString();
                person = findPerson(session, personName);
            }
            tx.commit();
        }
        return person;
    }

    private Ranking findRanking(Session session, String subject, String observer, String skill) {
        Query<Ranking> query = session.createQuery("from Ranking r where r.subject.name=:subject and" +
                " r.observer.name=:observer and r.skill.name=:skill", Ranking.class);
        query.setParameter("subject", subject);
        query.setParameter("observer", observer);
        query.setParameter("skill", skill);
        return query.uniqueResult();
    }

    private Person findPerson(Session session, String name) {
        Query<Person> query = session.createQuery("from Person p where p.name=:name", Person.class);
        query.setParameter("name", name);
        return query.uniqueResult();
    }

    private Skill findSkill(Session session, String name) {
        Query<Skill> query = session.createQuery("from Skill s where s.name=:name", Skill.class);
        query.setParameter("name", name);
        return query.uniqueResult();
    }

    private Skill saveSkill(Session session, String skillName) {
        Skill skill = findSkill(session, skillName);
        if (skill == null) {
            skill = new Skill();
            skill.setName(skillName);
            session.save(skill);
        }
        return skill;
    }

    private Person savePerson(Session session, String name) {
        Person person = findPerson(session, name);
        if (person == null) {
            person = new Person();
            person.setName(name);
            session.save(person);
        }
        return person;
    }
}
