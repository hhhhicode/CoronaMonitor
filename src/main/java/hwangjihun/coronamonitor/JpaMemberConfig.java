package hwangjihun.coronamonitor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class JpaMemberConfig {

    private final EntityManager entityManager;

    public JpaMemberConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
