# spring-1config

***spring-1config-client*** is a spring boot client implementation for [1Config](https://github.com/BrunoBonacci/1config) *(A tool and a library to manage application secrets and configuration safely and effectively.)*.

***spring-1config-sample*** is an application to demo this *spring-1config-client*

# Usage
Before getting started please read how the [1Config library works](https://cljdoc.org/d/com.brunobonacci/oneconfig/0.16.0/doc/readme)

For an example project please see [spring-1config-sample](spring-1config-sample)

Add the library to your pom.xml
```
<dependency>
    <groupId>systems.bos</groupId>
    <artifactId>spring-1config-client</artifactId>
    <version>0.0.3</version>
</dependency>
```

Make sure your application has the following properties available
```
spring.application.name=your-awesome-app
spring.application.version=0.0.3
```

[1Config](https://github.com/BrunoBonacci/1config) stores configuration using a `:key`, `:version` and `:env`. This library maps your configuration as below to match those fields.
|1Config |spring-1config            |
|--------|--------------------------|
|:key    |spring.application.name   |
|:version|spring.application.version|
|:env    |*Active profile           |

**Active profile* - spring-1config-client only supports one active [profile](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-profiles).\
It is recommended to set this via the environment variable `SPRING_PROFILES_ACTIVE`, this will ensure that your application is only packaged with default configuration and environment specific configuration is retrieved from [1Config](https://github.com/BrunoBonacci/1config). \
If no profile is found it will try and get configuration using the `local` env.

## Automatic property expansion
If you want to automatically populate your properties file with your project version from your `pom.xml` or `build.gradle`. Please follow this article: https://www.baeldung.com/spring-boot-auto-property-expansion

# Configuration type support
The *spring-1config-client* supports the following configuration types:
- properties
- json
- yaml

## TODO
- [ ] Tests
- [ ] Local DynamoDB + KMS
- [ ] Improve Documentation