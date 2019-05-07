package com.example.letsprint

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.ACTION_BLUETOOTH_SETTINGS
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.mainmenuitems, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.about_link -> {
               // Toast.makeText(this@MainActivity, "Hi my name is..", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@MainActivity, AboutActivity::class.java))
                true
            }
            R.id.settings_link -> {
                startActivity(Intent(ACTION_BLUETOOTH_SETTINGS))
                //Toast.makeText(this@MainActivity, "Lets mess with your phone settings", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.exit_link -> {
                Toast.makeText(this@MainActivity, "I'm done here", Toast.LENGTH_SHORT).show()
                //exit
                exitProcess(-1 )
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private val PICK_PDF_CODE = 1000
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Dexter.withActivity(this)
            .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : BaseMultiplePermissionsListener() {

            })

        btn_view_assets.setOnClickListener {
            val intent = Intent(this@MainActivity, ViewActivity::class.java)
            intent.putExtra("ViewType", "assets")
            startActivity(intent)
        }
        btn_view_storage.setOnClickListener {
            val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
            pdfIntent.type = "application/pdf"
            pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(Intent.createChooser(pdfIntent, "Select PDF"), PICK_PDF_CODE)
        }

    }
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_PDF_CODE && resultCode == Activity.RESULT_OK && data != null)
        {
            val selectedPDF = data.data
            val intent = Intent(this@MainActivity, ViewActivity::class.java)
            intent.putExtra("ViewType", "storage")
            intent.putExtra("FileUri", selectedPDF.toString())
            startActivity(intent)
        }


    }
}
