package com.zhourui.oauth2client.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.client.RestTemplate

@Service
class UserInfoService {
    @Autowired
    private lateinit var authorizedClientService: OAuth2AuthorizedClientService

    fun getUserName(): String {
        val authentication = SecurityContextHolder.getContext().authentication as OAuth2AuthenticationToken

        val client = authorizedClientService.loadAuthorizedClient<OAuth2AuthorizedClient>(
            authentication.authorizedClientRegistrationId,
            authentication.name
        )

        // userinfo url
        val userInfoEndpointUri = client.clientRegistration
            .providerDetails
            .userInfoEndpoint
            .uri

        val nameAttribute = client.clientRegistration
            .providerDetails.userInfoEndpoint.userNameAttributeName


        if (StringUtils.hasLength(userInfoEndpointUri)) {
            val restTemplate = RestTemplate()
            val headers = HttpHeaders()
            headers.add(
                HttpHeaders.AUTHORIZATION, "Bearer " + client.accessToken
                    .tokenValue
            )

            val entity = HttpEntity("", headers)
            val response = restTemplate.exchange(
                userInfoEndpointUri, HttpMethod.GET, entity,
                MutableMap::class.java
            )
            val userAttributes = response.body

            if (userAttributes.containsKey("name")) {
                return userAttributes["name"] as String
            } else {
                return userAttributes[nameAttribute] as String
            }
        }

        return authentication.name
    }
}