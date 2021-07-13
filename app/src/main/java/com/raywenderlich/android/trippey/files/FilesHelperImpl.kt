package com.raywenderlich.android.trippey.files

import java.io.File
import java.io.FileOutputStream

class FilesHelperImpl(private val directory: File) : FilesHelper {

    override fun saveData(fileName: String, data: String) {
        TODO("Not yet implemented")
    }

    override fun getData(): List<File> {
        TODO("Not yet implemented")
    }

    override fun deleteData(fileName: String) {
        TODO("Not yet implemented")
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