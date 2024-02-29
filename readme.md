# oauth2 client

## spring doc
https://docs.spring.io/spring-security/reference/servlet/oauth2/client/index.html

oauth2 client - good
https://docs.spring.io/spring-security/site/docs/5.0.7.RELEASE/reference/html/oauth2login-advanced.html


intercept logout page
https://docs.spring.io/spring-security/reference/servlet/authentication/logout.html

## oauth2 social login with facebook, google
https://console.cloud.google.com/apis/dashboard?pli=1&project=spring-oauth2-client-414114

login with google & facebook
https://www.baeldung.com/spring-security-5-oauth2-login
github: https://github.com/eugenp/tutorials/tree/master/spring-security-modules/spring-security-oauth2

**notes**
```
default login page
http://localhost:8081/login
after logout can't come back to loginsuccess - remember to logout the securitycontextholder

By default, Spring Boot configures this redirect URI as /login/oauth2/code/{registrationId}.
.redirectUri("{baseUrl}/{action}/oauth2/code/{registrationId}")

security:oauth2:client:registration: => 
OAuth2ClientRegistrationRepositoryConfiguration

click google->
in filter
doauthentication->
org/springframework/security/oauth2/client/oidc/authentication/OidcAuthorizationCodeAuthenticationProvider.java

spring-security-oauth2-client-6.1.3-sources.jar!\org\springframework\security\oauth2\client\web\OAuth2LoginAuthenticationFilter.java
=>
GrantedAuthoritiesMapper
```

## keycloak

https://stackoverflow.com/questions/74571191/use-keycloak-spring-adapter-with-spring-boot-3/74572732#74572732

```
.requestMatchers("/message/**").hasAuthority("SCOPE_message:read")
.anyRequest().authenticated()
```

## doc
spring security 5 how to build resource server
introspect & opaque token
https://www.baeldung.com/spring-security-oauth-resource-server

https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html

ch4mpy
https://github.com/ch4mpy/spring-addons/blob/master/samples/tutorials/reactive-resource-server/src/main/java/com/c4soft/springaddons/tutorials/WebSecurityConfig.java#L123

keycloak + springboot3 + login
https://dzone.com/articles/spring-boot-3-keycloak


keycloak auth code + login
https://www.baeldung.com/spring-boot-keycloak

customized jwtdecode，and configure jwkSetUri??
https://github.com/spring-projects/spring-security-samples/blob/main/servlet/spring-boot/java/oauth2/resource-server/jwe/src/main/java/example/OAuth2ResourceServerSecurityConfiguration.java

nothing new
https://github.com/Pask423/keycloak-springboot/tree/master/base-integration-spring-boot-3






# TODO
划分
private/ public

@PreAuthorize("#oauth2.hasRole('USER1')") not working

logout


There are a couple of ways, but I'm afraid both of them suggest relying on browser action. Original article here: https://groups.google.com/g/google-api-javascript-client/c/PCs8xXV4wxk?pli=1

TL;DR

Expose the link https://accounts.google.com/logout as part of logout action somewhere in your user interface.
Add hidden iframe with src pointing to the same url to the document for a couple of seconds and then remove it hoping there was no internet connection drop at that time.
I've chosen the first one, displayed a logout screen with the button link.