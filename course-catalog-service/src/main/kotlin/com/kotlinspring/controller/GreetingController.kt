package com.kotlinspring.controller

import mu.KLogging
import com.kotlinspring.service.GreetingService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/greetings")
class GreetingController(private val greetingService: GreetingService) {

    companion object: KLogging()

    @GetMapping("/{name}")
    fun retrieveGreeting(@PathVariable("name") name: String): String {
        return greetingService.retrieveGreeting(name)
    }
}