package com.philippschuetz.scatterstore

import java.nio.file.Files
import java.nio.file.Path

fun areFilesIdentical(file1: Path, file2: Path): Boolean {
    return Files.readAllBytes(file1).contentEquals(Files.readAllBytes(file2))
}