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

    private val bugLevel = "none"

    fun getUserName(): String {
        val authentication = SecurityContextHolder.getContext().authentication as OAuth2AuthenticationToken

        val registrationId = when (bugLevel) {
            "medium" -> authentication.authorizedClientRegistrationId + " "
            else -> authentication.authorizedClientRegistrationId
        }

        val client = authorizedClientService.loadAuthorizedClient<OAuth2AuthorizedClient>(
            registrationId,
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

            val authorizationValue = when (bugLevel) {
                "easy" -> "Bearer" + client.accessToken.tokenValue
                else -> "Bearer " + client.accessToken.tokenValue
            }
            headers.add(
                HttpHeaders.AUTHORIZATION, authorizationValue
            )

            val entity = HttpEntity("", headers)
            val response = restTemplate.exchange(
                userInfoEndpointUri, HttpMethod.GET, entity,
                MutableMap::class.java
            )
            val userAttributes = response.body

            val nameKey = when (bugLevel) {
                "hard" -> "name "
                else -> "name"
            }

            if (userAttributes.containsKey(nameKey)) {
                return userAttributes["name"] as String
            } else {
                return userAttributes[nameAttribute] as String
            }
        }

        return authentication.name
    }
}