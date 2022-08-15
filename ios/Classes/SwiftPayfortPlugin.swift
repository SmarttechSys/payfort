import Flutter
import UIKit
import PayFortSDK
public class SwiftPayfortPlugin: NSObject, FlutterPlugin {
    var window: UIWindow?
    var navigationController: UINavigationController?
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "payfort_plugin", binaryMessenger: registrar.messenger())
    let instance = SwiftPayfortPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
    
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    if call.method == "getID" {
        result(self.getId())
        
    } else if call.method == "initPayFort" {
        let frame = CGRect.init(x: 0, y: UIScreen.main.bounds.height - 60, width: UIScreen.main.bounds.width, height: 60)
            window = UIWindow.init(frame: frame)
        let controller : UIViewController =
        (UIApplication.shared.delegate?.window??.rootViewController)!;
        guard let args = call.arguments as? Dictionary<String, String> else { return }
        let token = args["sdkToken"]
        let merchantRef = args["merchantRef"]
        let name = args["name"]
        let lang = args["lang"]
        let command = args["command"]
        let amount = args["amount"]
        let email = args["email"]
        let currency = args["currency"]
        let mode = args["mode"]
        let merchant_extra = args["merchant_extra"]
        let merchant_extra1 = args["merchant_extra1"]
        let payment_option = args["payment_option"]
        var payFort:PayFortController
        if mode == "0" {
             payFort = PayFortController.init(enviroment: .sandBox)
        }else{
             payFort = PayFortController.init(enviroment: .production)
        }


        let request: NSMutableDictionary = .init()
        request.setValue(amount, forKey: "amount")
        request.setValue(command, forKey: "command")
        request.setValue(currency, forKey: "currency")
        request.setValue(email, forKey: "customer_email")
        request.setValue(lang, forKey: "language")
        request.setValue(merchantRef, forKey: "merchant_reference")
        request.setValue(token , forKey: "sdk_token")
        request.setValue(name, forKey: "customer_name")
        request.setValue(merchant_extra, forKey: "merchant_extra")
        request.setValue(merchant_extra1, forKey: "merchant_extra1")
        request.setValue(payment_option, forKey: "payment_option")

        payFort.isShowResponsePage = true
        payFort.callPayFort(withRequest: request as! [String : String], currentViewController: controller,
                            success: { (requestDic, responeDic) in
                                print("success")
                                print("responeDic=(responeDic)")
                                print("responeDic=(responeDic)")
                                result(responeDic)
        },
                            canceled: { (requestDic, responeDic) in
                                print("canceled")
                                print("requestDic=(requestDic)")
                                print("responeDic=(responeDic)")
                                result(responeDic)
        },
                            faild: { (requestDic, responeDic, message) in
                                print("faild")
                                print("requestDic=(requestDic)")
                                print("responeDic=(responeDic)")
                                print("message=(message)")
                                result(message)
        })
        
    }

  }
func getId() -> String { UIDevice.current.identifierForVendor?.uuidString ?? UUID().uuidString }
}
