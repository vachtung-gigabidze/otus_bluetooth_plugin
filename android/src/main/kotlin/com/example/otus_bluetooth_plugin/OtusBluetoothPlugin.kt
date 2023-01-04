package com.example.otus_bluetooth_plugin

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class BlueToothViewFactory : PlatformViewFactory(StandardMessageCodec.INSTANCE)
{

  override fun create(context: Context?, viewId: Int, args: Any?): PlatformView {
    return BlueToothView(context)
  }
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class BlueToothView(context: Context?) : PlatformView {

//  private lateinit var list : MutableList<String>
  private val _devices: MutableLiveData<List<BluetoothDevice>> = MutableLiveData()
  val devices: LiveData<List<BluetoothDevice>> get() = _devices

//  private var adapter =  BluetoothAdapter.getDefaultAdapter()
//  private val bluetoothAdapter: BluetoothAdapter by lazy {
//    val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
//    bluetoothManager.adapter
//  }
//  private var scanner : BluetoothLeScanner? = null
//  private var callback : BleScanCallback? = null

 // private val settings: ScanSettings
 // private val filters: List<ScanFilter>

  private val foundDevices = HashMap<String, BluetoothDevice>()

  var view : TextView
  var listView: ListView
  var linearLayout: LinearLayout
  var button: Button
  val language = arrayOf<String>("C++",".Net","Ruby","Python","Php","Perl")
  val language2 = arrayOf<String>("C","Java","Kotlin","Rails","Java Script","Ajax","Hadoop")
  var adapter : ArrayAdapter<String>
  lateinit var mContext: Context
  private final val RUNTIME_PERMISSION_REQUEST_CODE = 2

  private val bleScanner by lazy {
    bluetoothAdapter.bluetoothLeScanner
  }

  // From the previous section:
  private val bluetoothAdapter: BluetoothAdapter by lazy {
    val bluetoothManager = context?.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    bluetoothManager.adapter
  }

  private val scanSettings = ScanSettings.Builder()
    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
    .build()

  private val scanCallback = BleScanCallback()
//    object : ScanCallback() {
//    override fun onScanResult(callbackType: Int, result: ScanResult) {
//      adapter = ArrayAdapter<String>(context!!, android.R.layout.simple_list_item_1, android.R.id.text1, language2)
//      with(result.device) {
//        Log.i("ScanCallback", "Found BLE device! Name: ${name ?: "Unnamed"}, address: $address")
//      }
//    }
//  }



  private var isScanning = false
    set(value) {
      field = value
      button.text = if (value) "Stop Scan" else "Start Scan"
    }

  init {
    view = TextView(context)
    view.setBackgroundColor(Color.rgb(255, 255, 255))
    view.text = "Rendered on a native Android view (id: 3)"

    button = Button(context)
    button.text = "Find Device"
    button.setOnClickListener {
      if (isScanning) {
        this.view.text = "Stop"
        stopBleScan()
      } else {
        this.view.text = "Start"
        startBleScan()
      }
    }

    listView =  ListView(context)
    adapter = ArrayAdapter<String>(context!!, android.R.layout.simple_list_item_1, android.R.id.text1, language)
    listView.adapter = adapter
    //view.atext = "Hello"
    linearLayout = LinearLayout(context)

    linearLayout.orientation = LinearLayout.VERTICAL
    linearLayout.addView(view)
    linearLayout.addView(button)
    linearLayout.addView(listView)
    isScanning = false;
   // button.text = "Start Find Device"


  }

  override fun getView(): View? = linearLayout


  fun startBleScan() {

    bleScanner.startScan(null, scanSettings, scanCallback)
    isScanning = true
  }

  private fun stopBleScan() {
    bleScanner.stopScan(scanCallback)
    isScanning = false
  }

  inner class BleScanCallback : ScanCallback() {
    override fun onScanResult(callbackType: Int, result: ScanResult) {
      foundDevices[result.device.address] = result.device
      _devices.postValue(foundDevices.values.toList())
    }

    override fun onBatchScanResults(results: MutableList<ScanResult>) {
      results.forEach { result ->
        foundDevices[result.device.address] = result.device
      }
      _devices.postValue(foundDevices.values.toList())
    }

    override fun onScanFailed(errorCode: Int) {
      Log.e("BluetoothScanner", "onScanFailed:  scan error $errorCode")
    }
  }



  override fun dispose() {
    stopBleScan()
  }
}

/** OtusBluetoothPlugin */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class OtusBluetoothPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel

  

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    flutterPluginBinding.platformViewRegistry.registerViewFactory("bluetoothview", BlueToothViewFactory())
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "otus_bluetooth_plugin")
    channel.setMethodCallHandler(this)
  }


  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
     if (call.method == "getPlatformVersion") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")

    } else {
      result.notImplemented()
    }
  }


  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
