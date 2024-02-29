package com.zhourui.oauth2client

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.PropertySource


@SpringBootApplication
@PropertySource("classpath:application.yaml")
class Application


fun main(args:Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
