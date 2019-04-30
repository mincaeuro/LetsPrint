package com.example.letsprint

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log

import android.widget.Toast
import kotlinx.android.synthetic.main.activity_view.*
import android.bluetooth.BluetoothDevice

class ViewActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionbar = supportActionBar
        actionbar!!.title = "PDF File"
        actionbar.setDisplayHomeAsUpEnabled(true)
        
        setContentView(R.layout.activity_view)
        if (intent !=null){
            val viewType = intent.getStringExtra("ViewType")

            if (!TextUtils.isEmpty(viewType) || viewType != null){
                if(viewType == "assets")
                {
                    pdf_view.fromAsset("L442_1NP.pdf")
                        .password("null")// if pass needed
                        .defaultPage(0) //enter page nr.
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .enableDoubletap(true)
                       /** .onDraw{canvas, pageWidth, pageHeight, displayedPage ->
                            // Enter code here
                        }
                        .onDrawAll{canvas, pageWidth, pageHeight, displayedPage ->
                            // Enter code here
                        }
                        .onPageChange { page, pageCount ->
                            // enter ur code here
                        } **/
                        .onPageError { page, t ->
                            Toast.makeText(this@ViewActivity, "Error while opening page$page",Toast.LENGTH_SHORT).show()
                            Log.d("ERROR",""+t.localizedMessage)
                        }
                        .onTap { false }
                        .onRender{nbPages, pageWidth, pageHeight ->
                            pdf_view.fitToWidth() //screen fit
                        }
                        .enableAnnotationRendering(true)
                        .invalidPageColor(Color.RED)
                        .load()
                }
                else if (viewType == "storage")
                {
                    val selectedPdf = Uri.parse(intent.getStringExtra("FileUri"))
                    actionbar.title = selectedPdf.toString()
                    pdf_view.fromUri(selectedPdf)
                        // Toast.makeText(this@ViewActivity, "Loading file..",Toast.LENGTH_SHORT)
                        .password("null")// if pass needed
                        .defaultPage(0) //enter page nr.
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .enableDoubletap(true)
                       /** .onDraw{canvas, pageWidth, pageHeight, displayedPage ->
                            // Enter code here
                        }.onDrawAll{canvas, pageWidth, pageHeight, displayedPage ->
                            // Enter code here
                        }
                        .onPageChange { page, pageCount ->
                            // enter ur code here

                        }
                        **/
                        .onPageError { page, t ->
                            Toast.makeText(this@ViewActivity, "Error while opening page$page",Toast.LENGTH_SHORT).show()
                            Log.d("ERROR",""+t.localizedMessage)
                        }
                        .onTap { false }
                        .onRender{nbPages, pageWidth, pageHeight ->
                            pdf_view.fitToWidth() //screen fit
                        }
                        .enableAnnotationRendering(true)
                        .invalidPageColor(Color.RED)
                        .load()


                    print_btn.setOnClickListener {
                        Toast.makeText(this@ViewActivity, "Printing file:..$selectedPdf", Toast.LENGTH_SHORT).show()
                    }


                }
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }



}
