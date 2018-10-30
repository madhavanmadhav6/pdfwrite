package android.print

import android.Manifest
import android.content.ActivityNotFoundException
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.support.v4.content.FileProvider
import android.app.Activity
import android.content.DialogInterface
import android.Manifest.permission
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import com.example.vinitas.pdfwrite.R.string.app_name
import android.webkit.WebView
import com.example.vinitas.pdfwrite.R
import java.io.File


object PdfView {

    private val REQUEST_CODE = 101

    /**
     * convert webview content into to pdf file
     * @param activity pass the current activity context
     * @param webView webview
     * @param directory directory path where pdf file will be saved
     * @param fileName name of the pdf file.
     */
    fun createWebPrintJob(activity: Activity, webView: WebView, directory: File, fileName: String, callback: Callback) {

        //check the marshmallow permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE)
                callback.failure()
                return
            }
        }

        val jobName = activity.getString(R.string.app_name) + " Document"
        var attributes: PrintAttributes? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            attributes = PrintAttributes.Builder()
                    .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                    .setResolution(PrintAttributes.Resolution("pdf", "pdf", 600, 600))
                    .setMinMargins(PrintAttributes.Margins.NO_MARGINS).build()
        }
        val pdfPrint = PdfPrint(attributes!!)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pdfPrint.print(webView.createPrintDocumentAdapter(jobName), directory, fileName, object : PdfPrint.CallbackPrint {
                override fun success(path: String) {
                    callback.success(path)
                }

                override fun onFailure() {
                    callback.failure()
                }
            })
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                pdfPrint.print(webView.createPrintDocumentAdapter(), directory, fileName, object : PdfPrint.CallbackPrint {
                    override fun success(path: String) {
                        callback.success(path)
                    }

                    override fun onFailure() {
                        callback.failure()
                    }
                })
            }
        }
    }


    /**
     * create alert dialog to open the pdf file
     * @param activity pass the current activity context
     * @param title  to show the heading of the alert dialog
     * @param message  to show on the message area.
     * @param path file path create on storage directory
     */
    fun openPdfFile(activity: Activity,   path: String) {

        //check the marshmallow permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE)
                return
            }
        }






            fileChooser(activity, path)


    }

    /** callback interface to get the result back after created pdf file */
    interface Callback {
        fun success(path: String)
        fun failure()
    }


    /**
     * @param activity pass the current activity context
     * @param path storage full path
     */
    private fun fileChooser(activity: Activity, path: String) {
        val file = File(path)
        val target = Intent("android.intent.action.VIEW")
        val uri = FileProvider.getUriForFile(activity, "com.package.name.fileproviders", file)
        target.setDataAndType(uri, "application/pdf")
        target.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        val intent = Intent.createChooser(target, "Open File")
        try {
            activity.startActivity(intent)
        } catch (var6: ActivityNotFoundException) {
            var6.printStackTrace()
        }

    }

}