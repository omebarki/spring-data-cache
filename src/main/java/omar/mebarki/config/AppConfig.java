package omar.mebarki.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@ComponentScan(basePackages = {"omar.mebarki.controller", "omar.mebarki.repository"})
@EntityScan(basePackages = {"omar.mebarki.domain"})
@EnableJpaRepositories({"omar.mebarki.repository"})
public class AppConfig {
}
