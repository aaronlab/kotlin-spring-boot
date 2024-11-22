package net.aaronlab.demo

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SampleTest {
    private val testSample: Sample = Sample()

    @Test
    fun testSum() {
        val expected = 42
        assertEquals(expected, testSample.sum(40, 2))
    }
}