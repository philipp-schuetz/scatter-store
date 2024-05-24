package com.philippschuetz.scatterstore.splitting

import org.junit.jupiter.api.AfterEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.io.path.Path

class SplitFileKtTest {
    private val splittingResourcesBasePath = "src/test/resources/splitting/"

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun splitFile() {
        splitFile(
            Path(splittingResourcesBasePath + "to-split.txt"),
            2,
            UUID.randomUUID().toString(),
            Path(splittingResourcesBasePath)
        )
    }
}