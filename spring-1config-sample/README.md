# Sample Application

This application is to demo how the [1Config](https://github.com/BrunoBonacci/1config) library works within a spring boot application.\
To use 1Config locally it is better to use the File System as the default backend, you can do this by setting an environment variable `ONECONFIG_DEFAULT_BACKEND=fs`

In the applications `application.yml` file you have the following property (`foo: default`)

## Default
If you start the application now without changing any of the configuration you should see the following:
```
# curl http://localhost:8195/foo
default
```

## Local
Now create the following file: \
`~/.1config/spring-1config-sample/local/0.0.1/spring-1config-sample.properties`
```
foo=1config
```

If you start the application now with the following environment variable `SPRING_PROFILES_ACTIVE=local` you should see the following:
```
# curl http://localhost:8195/foo
1config
```
*If you started it without specifying the `SPRING_PROFILES_ACTIVE` environment variable you would still get `1config` as the properties value. As the `spring-1config-client` would not detect a profile so it would have defaulted us to `local`*

## Dev
Now create the following file: \
`.1config/spring-1config-sample/dev/0.0.1/spring-1config-sample.properties`
```
foo=1config is awesome!
```

If you start the application now with the following environment variable `SPRING_PROFILES_ACTIVE=dev` you should see the following:
```
# curl http://localhost:8195/foo
1config is awesome!
```