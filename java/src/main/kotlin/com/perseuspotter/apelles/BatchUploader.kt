package com.perseuspotter.apelles

import java.lang.Exception

object BatchUploader {
    fun upload(params: List<List<Any>>) {
        BatchUploaderJ.upload(params)
    }
}