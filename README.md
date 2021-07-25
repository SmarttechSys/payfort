# payfort_plugin

A flutter plugin to help flutter developer to integrate with payfort SDK easily, it supports android and ios from version 12.



## Getting Started

Add this to pubspec.yaml

```dart
dependencies:
  payfort_plugin: ^0.2.0+5
```

### Usage

```dart
PayfortPlugin.getID.then((deviceID) => { //use this deviceID to  send it to your server to get YOR_MERCHANT_REF and YOUR_SDK_TOKEN
                         PayfortPlugin.performPaymentRequest(
                              'YOR_MERCHANT_REF',
                              'YOUR_SDK_TOKEN',
                              'NAME',
                              'LANGUAGE',
                              'EMAIL',
                              'AMOUNT',
                              'PURCHASE',
                              'CURRENCY',
                              'MODE' //zero for test mode and one for production
                               ).then((value) => {
                                // value object contains payfort response, such card number, transaction reference, ...
                                debugPrint('card number is ${value['card_number']}')
                         })

                  });
```
### Contributing 

We Accept the following contributions

* Improving code documentation 
* Reporting issues
* bug fixing

## Maintainers

Magints


