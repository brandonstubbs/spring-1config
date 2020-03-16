package systems.bos.spring1config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class OneConfigBootstrapConfiguration {

    @Bean
    @ConditionalOnMissingBean(OneConfigPropertySourceLocator.class)
    public OneConfigPropertySourceLocator configServicePropertySource() {
        return new OneConfigPropertySourceLocator();
    }

}
