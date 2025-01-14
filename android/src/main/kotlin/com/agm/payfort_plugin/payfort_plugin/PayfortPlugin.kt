package com.agm.payfort_plugin.payfort_plugin

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import com.payfort.fortpaymentsdk.FortSdk
import com.payfort.fortpaymentsdk.callbacks.FortCallBackManager
import com.payfort.fortpaymentsdk.callbacks.FortCallback
import com.payfort.fortpaymentsdk.callbacks.FortInterfaces
import com.payfort.fortpaymentsdk.domain.model.FortRequest


import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import java.util.HashMap

/** PayfortPlugin */
class PayfortPlugin(): FlutterPlugin, MethodCallHandler,ActivityAware {
  private lateinit var channel : MethodChannel
  private lateinit var context: Context
  private lateinit var activity: Activity
  private var fortCallback: FortCallBackManager? = null
  var deviceId = "";
  var sdkToken = ""
  private val CHANNEL = "agm.flutter.apps/e-commerce"

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "payfort_plugin")
    channel.setMethodCallHandler(this)

  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    when (call.method) {
        "getID" -> {
          result.success(FortSdk.getDeviceId(activity))
        }
        "initPayFort" -> {
          var token = call.argument<String>("sdkToken")
          var merchantRef = call.argument<String>("merchantRef")
          var lang = call.argument<String>("lang")
          var command = call.argument<String>("command")
          var amount = call.argument<String>("amount")
          var email = call.argument<String>("email")
          var currency = call.argument<String>("currency")!!
          var mode = call.argument<String>("mode")!!
          var merchantExtra = call.argument<String>("merchant_extra")!!
          var merchantExtra1 = call.argument<String>("merchant_extra1")!!
          var paymentOption = call.argument<String>("payment_option")!!
          var envoirenment = if (mode == "0"){
            FortSdk.ENVIRONMENT.TEST
          }else{
            FortSdk.ENVIRONMENT.PRODUCTION
          }
          fortCallback = FortCallBackManager.Factory.create() as FortCallback
          deviceId = FortSdk.getDeviceId(activity)!!

          val fortrequest = FortRequest()
          val requestMap: MutableMap<String, Any> = HashMap()
          requestMap["command"] = command!!
          requestMap["customer_email"] = email!!
          requestMap["currency"] = currency!!
          requestMap["amount"] = amount?.toInt() ?: 0
          requestMap["language"] = lang!!
          requestMap["merchant_reference"] = merchantRef.toString()
          requestMap["sdk_token"] = token!!
          requestMap["merchant_extra"] = merchantExtra!!
          requestMap["merchant_extra1"] = merchantExtra1!!
          requestMap["payment_option"] = paymentOption!!
          fortrequest.requestMap = requestMap
          fortrequest.isShowResponsePage = true
          try {
            FortSdk.getInstance().registerCallback(activity, fortrequest, envoirenment, 5, fortCallback, true, object : FortInterfaces.OnTnxProcessed {
              override fun onCancel(requestParamsMap: Map<String, Any>, responseMap: Map<String, Any>) {
                result.success(mapOf("result" to "canceled"))
              }

              override fun onSuccess(requestParamsMap: Map<String, Any>, fortResponseMap: Map<String, Any>) {
                result.success(mapOf("result" to "success"))
              }

              override fun onFailure(requestParamsMap: Map<String, Any>, fortResponseMap: Map<String, Any>) {
                result.success(mapOf("result" to "failed"))
              }
            })
          } catch (e: Exception) {
            Log.e("execute Payment", "all FortSdk", e)
          }


        }
        else -> {
          result.notImplemented()
        }
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }


  override fun onDetachedFromActivity() {

  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {

  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity;
  }

  override fun onDetachedFromActivityForConfigChanges() {

  }
}
