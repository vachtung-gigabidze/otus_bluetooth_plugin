package com.example.otus_bluetooth_plugin

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.*
import android.os.Build
import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result


/** OtusBluetoothPlugin */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class OtusBluetoothPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel

  private lateinit var list : MutableList<String>

  private val adapter = BluetoothAdapter.getDefaultAdapter()
  private var scanner : BluetoothLeScanner? = null
  private var callback : BleScanCallback? = null

  private val settings: ScanSettings
  private val filters: List<ScanFilter>

  private val foundDevices = HashMap<String, BluetoothDevice>()

  init {
    settings = buildSetting()
    filters = buildFilter()
  }

  private fun buildSetting() =
    ScanSettings.Builder()
      .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build()


  private fun buildFilter() = listOf(ScanFilter.Builder().build())

  fun startScan(){
      if (callback == null){
        callback = BleScanCallback()
        scanner = adapter.bluetoothLeScanner
        scanner?.startScan(filters, settings, callback)
      }
  }

  fun stopScan(){
    if (callback != null){
      scanner?.stopScan(callback)
      scanner = null
      callback = null
    }
  }

  inner class BleScanCallback : ScanCallback(){
    override fun onScanResult(callbackType: Int, result: ScanResult) {
      foundDevices[result.device.address] = result.device
    }

    override fun onBatchScanResults(results: MutableList<ScanResult>) {
      results.forEach {  result ->
      foundDevices[result.device.address] = result.device}
    }

    override fun onScanFailed(errorCode: Int) {
      super.onScanFailed(errorCode)
    }

  }

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "otus_bluetooth_plugin")
    channel.setMethodCallHandler(this)
  }

//  @RequiresApi(Build.VERSION_CODES.P)
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
