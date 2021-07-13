package com.raywenderlich.android.trippey.files

import java.io.File
import java.io.FileOutputStream

class FilesHelperImpl(private val directory: File) : FilesHelper {

    override fun saveData(fileName: String, data: String) {
        // 1. Get a reference to the file you want to use
        val file = buildFile(fileName)

        // 2. Create FileOutputStream since we are writing to file here
        val fileOutputStream = buildOutputStream(file)

        // 3. Write data in bytes
        try {
            fileOutputStream.use {
                it.write(data.toByteArray())
            }
        } catch (error: Throwable) {
            error.printStackTrace()
        }
    }

    override fun getData(): List<File> =
        directory.listFiles()?.toList() ?: emptyList()

    override fun deleteData(fileName: String) {
        val targetFile = buildFile(fileName)

        if (targetFile.exists()) {
            targetFile.delete()
        }
    }

    /**
     *  Build a file with the given fileName inside the directory
     */
    private fun buildFile(fileName: String): File {
        return File(directory, fileName)
    }

    /**
     *  Build OutputStream for the given file.
     *  This OutputStream will be used in writing data to the given file.
     */
    private fun buildOutputStream(file: File): FileOutputStream {
        return FileOutputStream(file)
    }
}