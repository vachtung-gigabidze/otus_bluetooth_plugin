package com.example.otus_plugin_bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.startActivityForResult
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

  override fun create(context: Context, viewId: Int, args: Any?): PlatformView {
    return BlueToothView(context)
  }
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class BlueToothView(context: Context) : PlatformView {


  private val _devices: MutableLiveData<List<BluetoothDevice>> = MutableLiveData()

  private val foundDevices = HashMap<String, BluetoothDevice>()

  var view : TextView
  var listView: ListView
  var linearLayout: LinearLayout
  var button: Button
  val deviceList = arrayOf<String>("")
  var adapter : ArrayAdapter<String>
  lateinit var mContext: Context
  private final val RUNTIME_PERMISSION_REQUEST_CODE = 2

  private val bleScanner by lazy {
    bluetoothAdapter.bluetoothLeScanner
  }

  // From the previous section:
  private val bluetoothAdapter: BluetoothAdapter by lazy {
    val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    bluetoothManager.adapter
  }




  private val scanSettings = ScanSettings.Builder()
    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
    .build()

  private val scanCallback = BleScanCallback()




  private var isScanning = false
    set(value) {
      field = value
      button.text = if (value) "Stop Scan" else "Start Scan"
    }

  init {
    mContext = context
    view = TextView(context)
    view.setBackgroundColor(Color.rgb(255, 255, 255))
    view.text = "Rendered on a native Android view (id: 4)"

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
    adapter = ArrayAdapter<String>(context!!, android.R.layout.simple_list_item_1, android.R.id.text1, deviceList)
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

  fun fillList(list: List<String>){
    adapter = ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, android.R.id.text1, list)
    listView.adapter = adapter
  }

  override fun getView(): View? = linearLayout


  fun startBleScan() {
    if (bluetoothAdapter.isEnabled) {
      bleScanner.startScan(null, scanSettings, scanCallback)
      isScanning = true
    } else {view.text = "ble not enabled"}
  }

  private fun stopBleScan() {
    bleScanner.stopScan(scanCallback)
    isScanning = false
  }

  inner class BleScanCallback : ScanCallback() {
    override fun onScanResult(callbackType: Int, result: ScanResult) {
      //view.text = result.device.name.toString()
      foundDevices[result.device.address] = result.device
    //  _devices.postValue(foundDevices.values.toList())
      fillList( foundDevices.values.map { bluetoothDevice -> bluetoothDevice.name.toString() }.toList())
    }

    override fun onBatchScanResults(results: MutableList<ScanResult>) {
     // view.text = "result"
      results.forEach { result ->
        foundDevices[result.device.address] = result.device
      }
     // _devices.postValue(foundDevices.values.toList())
    }

    override fun onScanFailed(errorCode: Int) {
      view.text = "error"
      Log.e("Синийзуб>", "onScanFailed:  scan error $errorCode")
    }
  }



  override fun dispose() {
    stopBleScan()
  }
}
/** OtusPluginBluetoothPlugin */
class OtusPluginBluetoothPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    flutterPluginBinding.platformViewRegistry.registerViewFactory("bluetoothview", BlueToothViewFactory())
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "otus_plugin_bluetooth")
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
