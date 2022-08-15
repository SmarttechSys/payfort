import 'dart:async';

import 'package:flutter/services.dart';

class PayfortPlugin {
  static const MethodChannel _channel = const MethodChannel('payfort_plugin');

  ///
  /// this method is for getting user device id to help in generating SDK token
  static Future<String?> get getID async {
    final String? version = await _channel.invokeMethod('getID');
    return version;
  }

  ///
  /// this method is for calling payfort sdk in both android and ios to perform payment process with
  /// specified parameters.
  static Future<Map?> performPaymentRequest(
      String merchantRef,
      String sdkToken,
      String name,
      String language,
      String email,
      String amount,
      String command,
      String currency,
      String mode,
      String merchantExtra,
      String merchantExtra1,
      String paymentOption) async {
    Map? result = await _channel.invokeMethod('initPayFort', {
      'sdkToken': sdkToken,
      'merchantRef': merchantRef,
      'amount': amount,
      'email': email,
      'lang': language,
      'command': command,
      'name': name,
      'currency': currency,
      'mode': mode,
      "merchant_extra": merchantExtra,
      "merchant_extra1": merchantExtra1,
      "payment_option": paymentOption
    });
    return result;
  }
}
