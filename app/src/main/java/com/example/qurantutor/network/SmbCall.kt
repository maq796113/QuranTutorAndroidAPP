package com.example.qurantutor.network

import jcifs.smb.SmbFile
import kotlinx.coroutines.*
import okhttp3.*
import okio.Timeout
import java.io.IOException

class SmbCall(private val request: Request, private val file: SmbFile) : okhttp3.Call {
    private var isCancelled = false
    override fun request(): Request {
        return request
    }

    override fun execute(): Response {
        if (isCancelled) {
            throw IOException("Cancelled")
        }
        val inputStream = file.inputStream
        val content = inputStream.readBytes()
        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .body(ResponseBody.create(MediaType.parse("application/octet-stream"), content))
            .build()
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun enqueue(responseCallback: Callback) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = execute()
                withContext(Dispatchers.Main) {
                    responseCallback.onResponse(this@SmbCall, response)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    responseCallback.onFailure(this@SmbCall, e as IOException)
                }
            }
        }
    }

    override fun isExecuted(): Boolean {
        return false
    }

    override fun cancel() {
        isCancelled = true
    }

    override fun isCanceled(): Boolean {
        return isCancelled
    }

    override fun timeout(): Timeout {
        return Timeout()
    }

    override fun clone(): Call {
        return this
    }
}