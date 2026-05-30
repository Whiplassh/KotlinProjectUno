package com.example.projectuno.module_4.task_2

import kotlinx.coroutines.*
import java.io.File
import java.security.MessageDigest
import kotlin.system.measureTimeMillis

suspend fun computeSHA256(file: File): String = withContext(Dispatchers.IO) {
    val digest = MessageDigest.getInstance("SHA-256")
    val bytes = file.readBytes()
    val hash = digest.digest(bytes)
    hash.joinToString("") { "%02x".format(it) }
}

suspend fun findJsonFiles(directory: File): List<File> = withContext(Dispatchers.IO) {
    directory.walkTopDown()
        .filter { it.isFile && it.extension == "json" }
        .toList()
}

suspend fun findDuplicatesInDirectory(directoryPath: String, timeoutSeconds: Long = 10): String {
    val output = StringBuilder()

    val time = measureTimeMillis {
        val result = withTimeoutOrNull(timeoutSeconds * 1000) {
            coroutineScope {
                try {
                    val directory = File(directoryPath)
                    if (!directory.exists() || !directory.isDirectory) {
                        output.append("Error: directory does not exist or is not a directory\n")
                        return@coroutineScope
                    }

                    output.append("Searching for JSON files in directory: $directoryPath\n")
                    val jsonFiles = findJsonFiles(directory)
                    output.append("Found ${jsonFiles.size} JSON files\n")

                    if (jsonFiles.isEmpty()) {
                        output.append("No JSON files found\n")
                        return@coroutineScope
                    }

                    output.append("Computing SHA-256 for each file...\n")
                    val hashJobs = jsonFiles.map { file ->
                        async(Dispatchers.IO) {
                            try {
                                val hash = computeSHA256(file)
                                file to hash
                            } catch (e: Exception) {
                                null
                            }
                        }
                    }

                    val fileHashes = hashJobs.awaitAll().filterNotNull()

                    // Вывод хешей после завершения всех вычислений
                    fileHashes.forEach { (file, hash) ->
                        output.append("  ${file.name}: $hash\n")
                    }

                    val duplicates = fileHashes
                        .groupBy { it.second }
                        .filter { it.value.size > 1 }

                    if (duplicates.isEmpty()) {
                        output.append("\nNo duplicates found\n")
                    } else {
                        output.append("\n=== Duplicates found ===\n")
                        duplicates.forEach { (hash, files) ->
                            output.append("\nSHA-256: $hash\n")
                            files.forEach { (file, _) ->
                                output.append("  - ${file.absolutePath}\n")
                            }
                        }
                    }
                } catch (e: CancellationException) {
                    output.append("Operation cancelled\n")
                    throw e
                } catch (e: Exception) {
                    output.append("Error: ${e.message}\n")
                }
            }
        }

        if (result == null) {
            output.append("\nTimeout: search interrupted after $timeoutSeconds seconds\n")
        }
    }

    output.append("\nExecution time: ${time / 1000.0} seconds\n")
    return output.toString()
}


fun main() = runBlocking {
    System.setOut(java.io.PrintStream(System.out, true, "UTF-8"))

    val currentDir = File(System.getProperty("user.dir"))

    val possiblePaths = listOf(
        File(currentDir, "src/main/kotlin/com/example/projectuno/module_4/task_2/test_files"),
        File(currentDir, "console/src/main/kotlin/com/example/projectuno/module_4/task_2/test_files"),
        File(currentDir.parentFile, "console/src/main/kotlin/com/example/projectuno/module_4/task_2/test_files")
    )

    val testFilesDir = possiblePaths.firstOrNull { it.exists() && it.isDirectory }

    if (testFilesDir == null) {
        println("Error: Could not find test_files directory")
        println("Current working directory: ${currentDir.absolutePath}")
        println("Tried paths:")
        possiblePaths.forEach { println("  - ${it.absolutePath}") }
        return@runBlocking
    }

    val directoryPath = testFilesDir.absolutePath

    println("=== JSON Duplicate Finder ===\n")
    val result = findDuplicatesInDirectory(directoryPath, timeoutSeconds = 10)
    println(result)
}
