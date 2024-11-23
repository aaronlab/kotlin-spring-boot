package com.kotlinspring.service

import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class GreetingService {

    companion object: KLogging()

    @Value("\${stage}")
    lateinit var stage: String

    fun retrieveGreeting(name: String): String {
        logger.info("Hello $name! Stage: $stage")

        return "Hello $name! Stage: $stage"
    }
}