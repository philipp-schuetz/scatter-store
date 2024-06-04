package com.philippschuetz.scatterstore.splitting

import com.philippschuetz.scatterstore.areFilesIdentical
import org.junit.jupiter.api.AfterEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.io.path.Path
import kotlin.io.path.deleteExisting
import kotlin.io.path.exists

class SplitFileKtTest {
    private val splittingResourcesBasePath = "src/test/resources/splitting/"
    private val fileShards = listOf(
        Path(splittingResourcesBasePath + "shard-1"),
        Path(splittingResourcesBasePath + "shard-2")
    )
    private val fileShardsTmp = listOf(
        Path(splittingResourcesBasePath + "shard-tmp-1"),
        Path(splittingResourcesBasePath + "shard-tmp-2")
    )

    @Test
    fun splitFile() {
        splitFile(
            Path(splittingResourcesBasePath + "to-split.txt"),
            2,
            "shard-tmp",
            Path(splittingResourcesBasePath)
        )
        for (i in fileShards.indices) {
            assertTrue(areFilesIdentical(fileShards[i], fileShardsTmp[i]))
        }
    }

    @AfterEach
    fun tearDown() {
        for (encryptedFile in fileShards) {
            if (encryptedFile.exists()) {
                encryptedFile.deleteExisting()
            }
        }
    }
}