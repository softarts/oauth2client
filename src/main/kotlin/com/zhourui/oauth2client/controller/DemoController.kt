package com.zhourui.oauth2client.controller

import com.zhourui.oauth2client.service.UserInfoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DemoController {
    @Autowired
    private lateinit var userInfoService: UserInfoService

    @GetMapping("/")
    fun index(): String? {
        return "home page - welcome " + userInfoService.getUserName()
    }

}