package com.example.qurantutor.network

import jcifs.smb.SmbFile
import okhttp3.Call
import okhttp3.Request

class SmbCallFactory : Call.Factory {
    override fun newCall(request: Request): Call {
        val url = request.url().toString()
        val file = SmbFile(url)
        return SmbCall(request, file)
    }
}