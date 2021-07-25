import 'dart:async';

import 'package:flutter/material.dart';
import 'package:payfort_plugin/payfort_plugin.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  Map? result;

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {}

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: RaisedButton(
            child: Text('pay'),
            onPressed: () {
              PayfortPlugin.getID.then((value) => {
                    //use this call to get device id and send it to server
                    PayfortPlugin.performPaymentRequest(
                            'YOR_MERCHANT_REF',
                            'YOUR_SDK_TOKEN',
                            'ahmed',
                            'en',
                            'user@mail.com',
                            '100',
                            'PURCHASE',
                            'EGP',
                            '0' //zero for test mode and one for production
                            )
                        .then((value) => {
                              debugPrint(
                                  'card number is ${value!['card_number']}')
                            })
                  });
            },
          ),
        ),
      ),
    );
  }
}
