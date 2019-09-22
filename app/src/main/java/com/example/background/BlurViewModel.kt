/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.background

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.background.workers.BlurWorker


class BlurViewModel(application: Application) : AndroidViewModel(application) {

    var workManager: WorkManager
    internal var imageUri: Uri? = null
    internal var outputUri: Uri? = null

    private fun createDataInputForUri(): Data {
        val dataBuilder = Data.Builder()
        imageUri?.let { dataBuilder.putString(KEY_IMAGE_URI, imageUri.toString()) }
        return dataBuilder.build()
    }

    init {
        workManager = WorkManager.getInstance(application)
    }

    private fun uriOrNull(uriString: String?): Uri? {
        return if (!uriString.isNullOrEmpty()) {
            Uri.parse(uriString)
        } else {
            null
        }
    }

    fun applyBlur(blurLevel: Int) {
        val blurRequest = OneTimeWorkRequest.Builder(BlurWorker::class.java)
            .setInputData(createDataInputForUri())
            .build()
        workManager.enqueue(blurRequest)
    }

    /**
     * Setters
     */
    internal fun setImageUri(uri: String?) {
        imageUri = uriOrNull(uri)
    }

    internal fun setOutputUri(outputImageUri: String?) {
        outputUri = uriOrNull(outputImageUri)
    }
}