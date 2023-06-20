Review the following materials for this project:

- [Commit Messages](https://chris.beams.io/posts/git-commit/)
- [Test method naming](https://enterprisecraftsmanship.com/posts/you-naming-tests-wrong/)

- [Git(hub) Collaboration](#github-collaboration)
- [JIRA, Agile, Scrum](#jira-agile-scrum)
- [Spring Security, JWT](#spring-security-jwt)
- [i18n, l10n](#i18n-l10n)
- [Java 2FA](#java-2fa)
- [Email](#email)
- [Dotenv](#dotenv)
- [ControllerAdvice](#controlleradvice)
- [REST](#rest)
- [Frontend](#frontend)

## Git(hub) Collaboration
- branching & pull requests (short overview) https://www.youtube.com/watch?v=oFYyTZwMyAg
- pull requests (short overview) https://www.youtube.com/watch?v=jhtbhSpV5YA&t=66s
- branching & merging (detailed) https://www.youtube.com/watch?v=BF2OHMM86Ik
- resolving conflicts (details) https://www.youtube.com/watch?v=NXaEImbo-n8
- Git: merge vs rebase: https://www.youtube.com/watch?v=CRlGDDprdOQ
- Git: pull request + merge: https://www.youtube.com/watch?v=oFYyTZwMyAg

## JIRA, Agile, Scrum

- intro: https://www.youtube.com/watch?v=XU0llRltyFM
- agile: https://www.youtube.com/watch?v=GzzkpAOxHXs
- JIRA: https://www.youtube.com/watch?v=GWxMTvRGIpc&t=77s

## Spring Security, JWT

- jwt: https://www.youtube.com/watch?v=7Q17ubqLfaM
- Spring Security with JWT: https://curity.io/resources/learn/spring-boot-api/

## i18n, l10n

- i18n: https://www.baeldung.com/spring-boot-internationalization
- l10n: https://www.baeldung.com/java-8-localization

## Java 2FA

- 2FA with Java and Google Authenticator: https://medium.com/@ihorsokolyk/two-factor-authentication-with-java-and-google-authenticator-9d7ea15ffee6
- gradle package: https://mvnrepository.com/artifact/dev.samstevens.totp/totp

## Email

- javax.mail: https://www.javatpoint.com/java-mail-api-tutorial
- mailtrap: https://mailtrap.io/

## Dotenv

- djava-dotenv: https://mvnrepository.com/artifact/io.github.cdimascio/java-dotenv/5.2.2
- readme: https://github.com/cdimascio/dotenv-java

.env
```bash
DB_URL=jdbc:mysql://localhost/test_db?serverTimezone=UTC
DB_USERNAME=test_db_user
DB_PASSWORD=P4$$w0Rd
```

application.properties
```bash
spring.config.import=optional:file:.env.sample[.properties]
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

## ControllerAdvice

- Error handling for REST with Spring: https://www.baeldung.com/exception-handling-for-rest-with-spring

## REST

Retrofit
- overview: https://www.youtube.com/watch?v=kLZri-UFEN0
- Retrofit + Spring + Intellij: https://www.youtube.com/watch?v=usALVozMLXE

## Frontend

- React: https://www.youtube.com/watch?v=w7ejDZ8SWv8
- CORS:
    - https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS
    - https://stackoverflow.com/questions/49049312/cors-error-when-connecting-local-react-frontend-to-local-spring-boot-middleware
    - https://www.youtube.com/watch?v=PcZGwcgbzE4
