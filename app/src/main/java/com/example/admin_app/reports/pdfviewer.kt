package com.example.admin_app.reports

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.example.admin_app.R
import com.example.admin_app.databinding.ActivityPdfviewerBinding
import com.example.admin_app.databinding.ActivityReportsBinding
import com.github.barteksc.pdfviewer.PDFView
import java.io.File

lateinit var pdfView : PDFView

var urlpdf : String = ""
class pdfviewer : AppCompatActivity() {

    private lateinit var binding: ActivityPdfviewerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfviewerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        pdfView = binding.idPDFView
        PRDownloader.initialize(this)
        binding.progressBar.visibility = View.VISIBLE
        val fileName = "myFile.pdf"
        if (urlpdf!=null) {
            downloadPdfFromInternet(
                urlpdf,
                getRootDirPath(this),
                fileName
            )
        }
    }
    fun  seturl(url :String){
        urlpdf = url

    }
    fun getRootDirPath(context: Context): String {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val file: File = ContextCompat.getExternalFilesDirs(
                context.applicationContext,
                null
            )[0]
            file.absolutePath
        } else {
            context.applicationContext.filesDir.absolutePath
        }
    }
    private fun showPdfFromFile(file: File) {
        pdfView.fromFile(file)
            .password(null)
            .defaultPage(0)
            .enableSwipe(true)
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .onPageError { page, _ ->
                Toast.makeText(
                    this@pdfviewer,
                    "Error at page: $page", Toast.LENGTH_LONG
                ).show()
            }
            .load()
    }
    private fun downloadPdfFromInternet(url: String, dirPath: String, fileName: String) {
        PRDownloader.download(
            url,
            dirPath,
            fileName
        ).build()
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    Toast.makeText(this@pdfviewer, "downloadComplete", Toast.LENGTH_LONG)
                        .show()
                    val downloadedFile = File(dirPath, fileName)
                    binding.progressBar.visibility = View.GONE
                    showPdfFromFile(downloadedFile)
                }

                override fun onError(error: Error?) {
                    Toast.makeText(
                        this@pdfviewer,
                        "Error in downloading file : $error",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            })
    }
}