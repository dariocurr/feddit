package feddit.logger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * this class has the task of creating the
 * logger bean as it represents a configuration
 * of the latter
 *
 * @author Groupe A
 *
 * */
@Configuration
@EnableAspectJAutoProxy
public class LoggerConfiguration {

    @Bean
    public Logger beanLogger() {
        return new Logger();
    }

}
