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
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView





class MainActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.mainmenuitems, menu)
        return true
    }
//top menu items functions
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
                Toast.makeText(this@MainActivity, "Lets mess with your phone settings", Toast.LENGTH_SHORT).show()
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
//end of top menu items functions

    companion object {
        private val PICK_PDF_CODE = 1000
        val EXTRA_ADDRESS: String = "Device_address"
    }

    //bt dev objects
    val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    val requestEnableBT = 1
    lateinit var mPairedDevice: Set<BluetoothDevice>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //check if BT adapter is ON

        if (mBluetoothAdapter == null) {
            Toast.makeText(this@MainActivity, "Device does not support Bluetooth",Toast.LENGTH_SHORT).show()
            val textView = findViewById<TextView>(R.id.bt_name)
            textView.text = resources.getString(R.string.bt_not_supported)
        } else {
            if (!mBluetoothAdapter.isEnabled) {
                Toast.makeText(this@MainActivity, "Bluetooth is not enabled, enabling...",Toast.LENGTH_SHORT).show()
                val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBluetoothIntent, requestEnableBT)

            }

           // refresh_btn.setOnClickListener{pairedDeviceList()}
        }
        // end of BT check

/*        private fun pairedDeviceList(){
               mPairedDevice = mBluetoothAdapter!!.bondedDevices
               val list:ArrayList<BluetoothDevice> = ArrayList()

               if (!mPairedDevice.isEmpty()){
                   for (device:BluetoothDevice in mPairedDevice){
                       list.add(device)
                       Log.i("device", ""+device)
                   }
               }else{
                   Toast.makeText(this@MainActivity, "No paired devices found",Toast.LENGTH_SHORT).show()
               }

               val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
               devices_list.adapter = adapter
               devices_list.onItemClickListener = AdapterView.OnItemClickListener{_, _, position, _ ->
                   val device: BluetoothDevice = list[position]
                   val address: String = device.address

                   val intent = Intent(this, ControlActivity::class.java)
                   intent.putExtra(EXTRA_ADDRESS, address)
                   startActivity(intent)
               }
           }
        */

// open pdf from storage fun
        Dexter.withActivity(this)
            .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : BaseMultiplePermissionsListener() {

            })
//btn action
        btn_view_storage.setOnClickListener {
            val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
            pdfIntent.type = "application/pdf"
            pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(Intent.createChooser(pdfIntent, "Select PDF"), PICK_PDF_CODE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//pdf open action
        if(requestCode == PICK_PDF_CODE && resultCode == Activity.RESULT_OK && data != null)
        {
            val selectedPDF = data.data
            val intent = Intent(this@MainActivity, ViewActivity::class.java)
            intent.putExtra("ViewType", "storage")
            intent.putExtra("FileUri", selectedPDF.toString())
            startActivity(intent)
        }
//bt shit
      if(requestCode == requestEnableBT){
            if(resultCode == Activity.RESULT_OK){
                if (!mBluetoothAdapter.isEnabled) {
                    Toast.makeText(this@MainActivity, "BT was enabled",Toast.LENGTH_SHORT).show()
                    val textView = findViewById<TextView>(R.id.bt_name)
                    textView.text = resources.getString(R.string.no_bt_device_connected)
                } else {
                    Toast.makeText(this@MainActivity, "BT was disabled",Toast.LENGTH_SHORT).show()
                    val textView = findViewById<TextView>(R.id.bt_name)
                    textView.text = resources.getString(R.string.bt_disabled)
                }
            }else if(resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this@MainActivity, "BT enabling has been canceled",Toast.LENGTH_SHORT).show()
                val textView = findViewById<TextView>(R.id.bt_name)
                textView.text = resources.getString(R.string.bt_disabled)
            }
        }

    }
}
