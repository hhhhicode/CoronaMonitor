package hwangjihun.coronamonitor.repository;


import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Optional;
/*

@Repository
@Transactional
public class SessionRepository {

    private final EntityManager entityManager;

    public SessionRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(UserSession userSession) {

        entityManager.persist(userSession);
    }

    public Optional<UserSession> findById(String sessionId) {
        UserSession findUserSession = entityManager.find(UserSession.class, sessionId);
        return Optional.ofNullable(findUserSession);
    }

    public void remove(String sessionId) {
        entityManager.remove(findById(sessionId));
    }
}
*/
