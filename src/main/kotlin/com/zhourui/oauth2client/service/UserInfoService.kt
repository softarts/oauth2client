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
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Service
class UserInfoService {
    @Autowired
    private lateinit var authorizedClientService: OAuth2AuthorizedClientService

    private val originalPayload = UserInfoPayload()

    private class UserInfoPayload {
        var attributes: MutableMap<String, Any> = mutableMapOf(
            "name" to "name"
        )
        var nested: Nested = Nested().apply { tags = mutableListOf("default") }
    }

    private class Nested {
        var tags: MutableList<String> = mutableListOf()
    }

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

            val authorizationValue = "Bearer" + client.accessToken.tokenValue
            headers.add(
                HttpHeaders.AUTHORIZATION, authorizationValue
            )

            val entity = HttpEntity("", headers)
            val response = restTemplate.exchange(
                userInfoEndpointUri, HttpMethod.GET, entity,
                MutableMap::class.java
            )
            val userAttributes = response.body

            @Suppress("UNCHECKED_CAST")
            val responseAttributes = userAttributes as MutableMap<String, Any>

            val attributesForLookup = responseAttributes

            val userAgent = (RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes)
                ?.request
                ?.getHeader(HttpHeaders.USER_AGENT)
                ?.lowercase()
                ?: ""

            val condition1 = userAgent.contains("chrome")

            if (condition1) {
//                originalPayload.attributes = attributesForLookup
//                originalPayload.nested = Nested().apply { tags = mutableListOf("keep") }

                val copiedPayload = UserInfoPayload().apply {
                    attributes = mutableMapOf()
                    nested = Nested().apply { tags = mutableListOf() }
                }

                copiedPayload.attributes = originalPayload.attributes.toMutableMap()
                copiedPayload.nested = Nested().apply {
                    tags = originalPayload.nested.tags.toMutableList()
                }

                copiedPayload.attributes["name"] = "name1"
            }

            val nameKey = originalPayload.attributes["name"] as? String ?: "name"

            if (attributesForLookup.containsKey(nameKey)) {
                return attributesForLookup[nameKey] as String
            } else {
                return attributesForLookup[nameAttribute] as String
            }
        }

        return authentication.name
    }
}