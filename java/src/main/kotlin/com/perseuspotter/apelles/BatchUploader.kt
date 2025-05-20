package com.perseuspotter.apelles

object BatchUploader {
    fun upload(params: List<List<Any>>) {
        BatchUploaderJ.upload(params)
    }
}