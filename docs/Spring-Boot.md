# Spring Boot

## Configure a Spring Boot Application

#### Set Configuration Properties

A Spring Boot Application will load configuration properties from `application.yml` files in the `/config` subdirectory of the current directory.

You can also supply the configuration properties as command-line parameters:

```
java -jar <application>.jar --panero.tenant=test
```

In addition, you can also use environment variables and Java system properties. Most operating systems disallow period-separated key names, but you can use underscores instead (e.g. `PANERO_TENANT` instead of `panero.tenant`).

#### Using a `dev` Profile

Create a file `application-dev.yml` in the `/config` subdirectory.
Overwrite the configuration values as needed.

Activate the profile on start-up:

```
java -jar <application>.jar --spring.profiles.active=dev
```

## Deploying a Spring Boot Application

* [Linux](http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#deployment-install)
* [Windows](https://github.com/snicoll-scratches/spring-boot-daemon)
