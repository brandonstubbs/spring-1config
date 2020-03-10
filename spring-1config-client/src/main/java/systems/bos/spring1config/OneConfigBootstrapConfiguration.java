package systems.bos.spring1config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class OneConfigBootstrapConfiguration {

    @Bean
    @ConditionalOnMissingBean(OneConfigPropertySourceLocator.class)
    @ConditionalOnProperty(value = "1Config.enabled", matchIfMissing = true)
    public OneConfigPropertySourceLocator configServicePropertySource() {
        return new OneConfigPropertySourceLocator();
    }
}
