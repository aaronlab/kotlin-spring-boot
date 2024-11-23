package org.example.coursecatalogservice.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class GreetingService {

    @Value("\${stage}")
    lateinit var stage: String

    fun retrieveGreeting(name: String) = "Hello $name! Stage: $stage"
}