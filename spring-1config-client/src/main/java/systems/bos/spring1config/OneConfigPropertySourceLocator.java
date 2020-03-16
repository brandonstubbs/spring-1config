package systems.bos.spring1config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.cloud.bootstrap.support.OriginTrackedCompositePropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;

import com.brunobonacci.oneconfig.client.OneConfigClient;
import com.brunobonacci.oneconfig.client.OneConfigClient.ConfigEntry;

@Order(0)
public class OneConfigPropertySourceLocator implements PropertySourceLocator {
    private static final Log log = LogFactory.getLog(OneConfigPropertySourceLocator.class);

    private static final String PROPERTY_SOURCE = "OneConfigService";
    private static final String APPLICATION_NAME_PROPERTY = "spring.application.name";
    private static final String APPLICATION_VERSION_PROPERTY = "spring.application.version";
    private static final String LOCAL_PROFILE = "local";

    @Override
    public PropertySource<?> locate(org.springframework.core.env.Environment environment) {
        final String env;
        if (environment.getActiveProfiles() != null && environment.getActiveProfiles().length > 0) {
            if (environment.getActiveProfiles().length > 1) {
                throw new IllegalArgumentException("1Config client can not be used with more than 1 active profile. Profiles detected: " + Arrays.toString(environment.getActiveProfiles()));
            }
            env = environment.getActiveProfiles()[0];
        } else {
            env = LOCAL_PROFILE;
        }

        final String applicationName = environment.getProperty(APPLICATION_NAME_PROPERTY);
        if (applicationName == null) {
            throw new IllegalStateException("Could not get application name. Property " + APPLICATION_NAME_PROPERTY + " not set");
        }
        final String version = environment.getProperty(APPLICATION_VERSION_PROPERTY);
        if (version == null) {
            throw new IllegalStateException("Could not get application version. Property " + APPLICATION_VERSION_PROPERTY + " not set");
        }

        log.info(String.format("Attempting to get configuration for => applicationName: %s, environment: %s, version: %s", applicationName, env, version));
        final ConfigEntry config = OneConfigClient.configure(applicationName, env, version);

        if (config == null) {
            log.info("No configuration found");
            return null;
        }

        switch (config.getContentType().toLowerCase()) {
            case "yaml":
            case "json": {
                final CompositePropertySource composite = new OriginTrackedCompositePropertySource(PROPERTY_SOURCE);
                composite.addFirstPropertySource(new MapPropertySource("oneConfig-json", jsonToProperties(config.getValueAsJsonMap())));
                return composite;
            }
            case "properties":
            case "txt": {
                final CompositePropertySource composite = new OriginTrackedCompositePropertySource(PROPERTY_SOURCE);
                composite.addFirstPropertySource(new PropertiesPropertySource("oneConfig-properties", config.getValueAsProperties()));
                return composite;
            }
            default:
                throw new IllegalStateException(String.format("Received a content type %s that is not supported", config.getContentType().toLowerCase()));
        }
    }

    private static Map<String, Object> jsonToProperties(Map<String, ? extends Object> json) {
        final Map<String, Object> propertyMap = new HashMap<>();
        appendJsonParentGroup(propertyMap, json, null);
        return propertyMap;
    }

    private static void appendJsonParentGroup(Map<String, Object> propertyMap, Map<?, ?> json, StringJoiner key) {
        for (Entry<?, ?> group : json.entrySet()) {
            StringJoiner newKey = new StringJoiner(".");
            if (key != null) {
                newKey.merge(key);
            }
            newKey.add((String) group.getKey());
            appendJsonChildProperty(propertyMap, newKey, group);
        }
    }

    private static void appendJsonChildProperty(Map<String, Object> propertyMap, StringJoiner key, Entry<?, ?> entry) {
        if (entry.getValue() instanceof Map) {
            appendJsonParentGroup(propertyMap, (Map<?, ?>) entry.getValue(), key);
        } else {
            propertyMap.put(key.toString(), entry.getValue());
        }
    }
}
