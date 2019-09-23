package com.example.background.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.background.OUTPUT_PATH
import java.io.File
import java.lang.Exception

class CleanupWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val tag = CleanupWorker::javaClass.name

        try {
            val outputDirectory = File(applicationContext.filesDir, OUTPUT_PATH)
            if (outputDirectory.exists()) {
                val entries = outputDirectory.listFiles()
                if (!entries.isNullOrEmpty()) {
                    entries.forEach { entry ->
                        val name = entry.name
                        if (name.isNotBlank() && name.endsWith(".png")) {
                            val deleted = entry.delete()
                            Log.i(tag, String.format("Deleted %s - %s", name, deleted))
                        }
                    }
                }
            }
            return Result.success()
        } catch (exception: Exception) {
            Log.i(tag, String.format("Error cleaning up: $exception"))
            return Result.failure()
        }
    }
}