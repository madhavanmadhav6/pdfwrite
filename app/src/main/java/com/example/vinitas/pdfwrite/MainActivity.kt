package com.example.vinitas.pdfwrite

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.support.v7.app.AppCompatActivity
import android.graphics.pdf.PdfDocument
import android.os.*
import android.support.annotation.RequiresApi
import java.io.FileOutputStream
import java.io.IOException
import android.print.pdf.PrintedPdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.os.Environment.DIRECTORY_DCIM
import android.os.Environment.getExternalStoragePublicDirectory
import android.print.*
import java.io.File
import android.print.PdfPrint
import android.os.Environment.DIRECTORY_DCIM
import android.os.Environment.getExternalStoragePublicDirectory
import android.os.Environment.DIRECTORY_DCIM
import android.os.Environment.getExternalStoragePublicDirectory
import android.print.PdfView
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Environment.DIRECTORY_DCIM
import android.os.Environment.getExternalStoragePublicDirectory
import android.print.PdfView.openPdfFile
import android.support.v4.content.FileProvider
import android.widget.Button


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)

class MainActivity : AppCompatActivity() {
    var priceArray = arrayOf<String>("20","30","50","60","70","80")
    var pronameArray = arrayOf<String>("200","300","500","600","700","800")

    var hsnArray = arrayOf<String>("200","300","500","600","700","800")

    var quantityArray = arrayOf<String>("2","3","5","6","7","8")
    var igsttotArray = arrayOf<String>("200","300","500","600","700","800")
    var cesstotalArray = arrayOf<String>("200","300","500","600","700","800")


    private var myWebView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView = findViewById<WebView>(R.id.webview) as WebView

        val prr=findViewById<Button>(R.id.button) as Button


        var hh="Value"
        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView,
                                                  url: String): Boolean {
                return false
            }

            override fun onPageFinished(view: WebView, url: String) {
                /*createWebPrintJob(view)*/
                /*createWebPrintJob(view)*/
                myWebView = null
            }
        }

        var gros = 0.0F


        //Html document as 'String' value

        var htmlDocument = "<html><body><h4>" + "Stock Details</h4>" +

                "<table class=Border>"+

                //S.No and Date inside non border table
                "<tr>"+
                "<td class=Border>S.No:</td>"+
                "<td class=Border>1</td>"+"</tr>"+
                "<tr>"+
                "<td class=Border>Date:</td>"+
                "<td class=Border>31/10/2018</td>"+
                "</tr>"+


                "<style>"+

                //Styles for border,header,table data
                "table, th, td {"+
                "border: 1px solid black;"+
                "border-collapse: collapse;"+
                "}"+


                //Padding for table header,table data
                "th, td {"+
                "padding: 15px;"+
                "}"+

                "td.Border {"+
                "border:none;"+
                "width:60%;"+
                "padding:8px;"+
                "}"+

                "table.Border {"+
                "border:none;"+
                "}"+


                //Align style for table header
               "th {"+
                    "text-align: left;"+
                "}"+

                "</style>"+

                // Initaiate table width=100%
                "<table style=\"width:100%\">"+

                //Table Headers 'Price','Quantity','Total'
                "<tr>"+
                "<th>Price</th>"+
                "<th>Quantity</th>"+
                "<th>Total</th>"+
                "</tr>"

                for (i in 0 until priceArray.size) {

                    //Calculate price and quantity
                   var pri = priceArray[i].toFloat()
                   var quan = quantityArray[i].toFloat()

                    //Here is the total 'grtt'
                    var gttt = (pri * quan)



                    //Displaying array values in a table cells
                    htmlDocument=htmlDocument+

                            "<tr>"+
                             "<td>${priceArray[i]}</td>"+
                             "<td>${quantityArray[i]}</td>"+
                             "<td>$gttt</td>"+
                              "</tr>"

                    if(priceArray.size==i){

                    }





               }

        //Closing tag of html
        htmlDocument=htmlDocument+
               "</body></html>"

        //Load our html data
        webView.loadDataWithBaseURL(null, htmlDocument, "text/HTML", "UTF-8", null)
        myWebView = webView


        button.setOnClickListener{
            val path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Stock Transfer"
            val fileName = "Test.pdf"
            val dir = File(path);
            if (!dir.exists())
                dir.mkdirs()



            val file = File(dir, fileName)
            val progressDialog = ProgressDialog(this@MainActivity)
            progressDialog.setMessage("Please wait")
            progressDialog.show()
            PdfView.createWebPrintJob(this@MainActivity, webView, file, fileName, object : PdfView.Callback {

                override fun success(path: String) {
                    progressDialog.dismiss()
                    val builder = AlertDialog.Builder(this@MainActivity)
                    with(builder) {
                        setTitle("File Exported")
                        setMessage("Do you want to open a file?")
                        setPositiveButton("Open") { dialog, whichButton ->
                            openPdfFile(this@MainActivity,path)
                            //sendMail(path)
                        }
                        setNegativeButton("Cancel") { dialog, whichButton ->
                            //showMessage("Close the game or anything!")
                            dialog.dismiss()
                        }

                        // Dialog
                        val dialog = builder.create()

                        dialog.show()
                    }
                }

                override fun failure() {
                    progressDialog.dismiss()

                }
            })
        }


    }

    fun sendMail(path: String) {  //Send this pdf to desired path.
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                arrayOf("muthumadhavan.vinitas@gmail.com","it@vinitas.co.in"))
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                "Purchase PO")
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                "This is an autogenerated mail from Vinitas Inventory")
        val file = File(path)
        val uri = FileProvider.getUriForFile(this@MainActivity, "com.package.name.fileproviders", file)
        emailIntent.type= "application/pdf"


        emailIntent.putExtra(Intent.EXTRA_STREAM, uri)

        startActivity(Intent.createChooser(emailIntent, "Send mail..."))
        finish()
    }


    /*@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun createWebPrintJob(webView: WebView) {

        val printManager = this
                .getSystemService(Context.PRINT_SERVICE) as PrintManager

        val printAdapter = webView.createPrintDocumentAdapter("MyDocument")

        val jobName = getString(R.string.app_name) + " Print Test"

        printManager.print(jobName, printAdapter,
                PrintAttributes.Builder().build())
    }*/


  /*  private fun createWebPrintJob(webView: WebView) {
        val jobName = getString(R.string.app_name) + " Document"
        val attributes = PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                .setResolution(PrintAttributes.Resolution("pdf", "pdf", 600, 600))
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS).build()
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/PDFTest/")
        val pdfPrint = PdfPrint(attributes)

        pdfPrint.print(webView.createPrintDocumentAdapter(jobName), path, "output_" + System.currentTimeMillis() + ".pdf")
    }*/
   /* @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun createWebPrintJob(webView: WebView) {

        val printManager = this
                .getSystemService(Context.PRINT_SERVICE) as PrintManager

        val printAdapter = webView.createPrintDocumentAdapter("MyDocument")

        val jobName = getString(R.string.app_name) + " Print Test"

        PrintAttributes attributes = new PrintAttributes.Builder()
        printManager.print(jobName, printAdapter,
                PrintAttributes.Builder()
                        .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                        .setResolution(new PrintAttributes.Resolution("pdf", "pdf", 600, 600))
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS).build())
    }*/
}
