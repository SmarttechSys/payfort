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
          Log.e("execute getID", "executing")

          result.success(FortSdk.getDeviceId(activity))
        }
        "initPayFort" -> {
          var token = call.argument<String>("sdkToken")
          var merchantRef = call.argument<String>("merchantRef")
          var name = call.argument<String>("name")
          var lang = call.argument<String>("lang")
          var command = call.argument<String>("command")
          var amount = call.argument<Double>("amount")
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
          requestMap["amount"] = amount ?: 0.0
          requestMap["language"] = lang!!
          requestMap["merchant_reference"] = merchantRef.toString()
          requestMap["customer_name"] = name!!
          requestMap["sdk_token"] = token!!
          requestMap["merchant_extra"] = merchantExtra!!
          requestMap["merchant_extra1"] = merchantExtra1!!
          requestMap["payment_option"] = paymentOption!!
          fortrequest.requestMap = requestMap
          fortrequest.isShowResponsePage = true
          try {
            FortSdk.getInstance().registerCallback(activity, fortrequest, envoirenment, 5, fortCallback, true, object : FortInterfaces.OnTnxProcessed {
              override fun onCancel(requestParamsMap: Map<String, Any>, responseMap: Map<String, Any>) {
                Log.d("Cancelled", responseMap.toString())
                result.success(responseMap)
              }

              override fun onSuccess(requestParamsMap: Map<String, Any>, fortResponseMap: Map<String, Any>) {
                Log.i("Success", fortResponseMap.toString())
                result.success(fortResponseMap)
              }

              override fun onFailure(requestParamsMap: Map<String, Any>, fortResponseMap: Map<String, Any>) {
                Log.e("Failure", fortResponseMap.toString())
                result.success(fortResponseMap)
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
    TODO("Not yet implemented")
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    TODO("Not yet implemented")
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity;
  }

  override fun onDetachedFromActivityForConfigChanges() {
    TODO("Not yet implemented")
  }
}
