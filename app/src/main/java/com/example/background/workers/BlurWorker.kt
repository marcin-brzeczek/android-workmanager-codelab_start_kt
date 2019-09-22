package com.example.background.workers

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.background.KEY_IMAGE_URI
import com.example.background.R
import java.lang.IllegalArgumentException

class BlurWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    private val TAG = BlurWorker::class.java.simpleName

    override fun doWork(): Result {
        try {
            val resourceUri = inputData.getString(KEY_IMAGE_URI)
//            val bitmapPicture: Bitmap = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.test)

            resourceUri?.let {
                if (it.isBlank()) {
                    throw IllegalArgumentException("Invalid input uri")
                }
            }

            val resolver = applicationContext.contentResolver
            val bitmapPicture: Bitmap = BitmapFactory.decodeStream(resolver.openInputStream(Uri.parse(resourceUri!!)))
            val blurredBitmapPicture = blurBitmap(bitmapPicture, applicationContext)
            val uri = writeBitmapToFile(applicationContext, blurredBitmapPicture)
            makeStatusNotification("Output is " + uri.toString(), applicationContext)

            val outputData = Data.Builder()
                .putString(KEY_IMAGE_URI, uri.toString())
                .build()
            return Result.success(outputData)
        } catch (throwable: Throwable) {
            Log.e(TAG, "Error applying blur", throwable)
            return Result.failure()
        }
    }
}