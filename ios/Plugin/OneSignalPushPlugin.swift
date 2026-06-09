import Capacitor
import Foundation
import OneSignalFramework

@objc(OneSignalPushPlugin)
public class OneSignalPushPlugin: CAPPlugin, CAPBridgedPlugin {
    public let identifier = "OneSignalPushPlugin"
    public let jsName = "OneSignalPush"
    public let pluginMethods: [CAPPluginMethod] = [
        CAPPluginMethod(name: "exec", returnType: CAPPluginReturnPromise)
    ]

    @objc func exec(_ call: CAPPluginCall) {
        let action = call.getString("action") ?? ""
        let args = call.getArray("args", JSArray())

        switch action {
        case "init":
            OneSignalWrapper.sdkType = "capacitor"
            OneSignalWrapper.sdkVersion = "050311"
            if let appId = args.first as? String {
                OneSignal.initialize(appId, withLaunchOptions: nil)
            }
            call.resolve()
        case "login":
            OneSignal.login(args.first as? String ?? "")
            call.resolve()
        case "logout":
            OneSignal.logout()
            call.resolve()
        case "setPrivacyConsentRequired":
            OneSignal.setConsentRequired(args.first as? Bool ?? false)
            call.resolve()
        case "setPrivacyConsentGiven":
            OneSignal.setConsentGiven(args.first as? Bool ?? false)
            call.resolve()
        case "setLanguage":
            OneSignal.User.setLanguage(args.first as? String ?? "")
            call.resolve()
        case "getOnesignalId":
            call.resolve(["value": emptyToNull(OneSignal.User.onesignalId)])
        case "getExternalId":
            call.resolve(["value": emptyToNull(OneSignal.User.externalId)])
        case "getPushSubscriptionId":
            call.resolve(["value": emptyToNull(OneSignal.User.pushSubscription.id)])
        case "getPushSubscriptionToken":
            call.resolve(["value": emptyToNull(OneSignal.User.pushSubscription.token)])
        case "getPushSubscriptionOptedIn":
            call.resolve(["value": OneSignal.User.pushSubscription.optedIn])
        case "optInPushSubscription":
            OneSignal.User.pushSubscription.optIn()
            call.resolve()
        case "optOutPushSubscription":
            OneSignal.User.pushSubscription.optOut()
            call.resolve()
        case "addEmail":
            OneSignal.User.addEmail(args.first as? String ?? "")
            call.resolve()
        case "removeEmail":
            OneSignal.User.removeEmail(args.first as? String ?? "")
            call.resolve()
        case "addSms":
            OneSignal.User.addSms(args.first as? String ?? "")
            call.resolve()
        case "removeSms":
            OneSignal.User.removeSms(args.first as? String ?? "")
            call.resolve()
        case "addAlias":
            call.resolve()
        case "addAliases":
            if let aliases = args.first as? [String: String] { OneSignal.User.addAliases(aliases) }
            call.resolve()
        case "removeAliases":
            OneSignal.User.removeAliases(args.compactMap { $0 as? String })
            call.resolve()
        case "addTags":
            if let tags = args.first as? [String: String] { OneSignal.User.addTags(tags) }
            call.resolve()
        case "removeTags":
            OneSignal.User.removeTags(args.compactMap { $0 as? String })
            call.resolve()
        case "getTags":
            call.resolve(["value": OneSignal.User.tags])
        case "getPermissionInternal":
            call.resolve(["value": OneSignal.Notifications.permission])
        case "permissionNative":
            call.resolve(["value": OneSignal.Notifications.permissionNative.rawValue])
        case "canRequestPermission":
            call.resolve(["value": OneSignal.Notifications.canRequestPermission])
        case "requestPermission":
            OneSignal.Notifications.requestPermission({ accepted in
                call.resolve(["value": accepted])
            }, fallbackToSettings: args.first as? Bool ?? false)
        case "registerForProvisionalAuthorization":
            OneSignal.Notifications.registerForProvisionalAuthorization { accepted in
                call.resolve(["value": accepted])
            }
        case "clearAllNotifications":
            OneSignal.Notifications.clearAll()
            call.resolve()
        case "setLogLevel":
            if let value = args.first as? Int32, let level = ONE_S_LOG_LEVEL(rawValue: value) {
                OneSignal.Debug.setLogLevel(level)
            }
            call.resolve()
        case "setAlertLevel":
            if let value = args.first as? Int32, let level = ONE_S_LOG_LEVEL(rawValue: value) {
                OneSignal.Debug.setAlertLevel(level)
            }
            call.resolve()
        case "setLocationShared":
            OneSignal.Location.isShared = args.first as? Bool ?? false
            call.resolve()
        case "isLocationShared":
            call.resolve(["value": OneSignal.Location.isShared])
        case "requestLocationPermission":
            OneSignal.Location.requestPermission()
            call.resolve()
        case "addTriggers":
            if let triggers = args.first as? [String: String] { OneSignal.InAppMessages.addTriggers(triggers) }
            call.resolve()
        case "removeTriggers":
            if let triggers = args.first as? [String] { OneSignal.InAppMessages.removeTriggers(triggers) }
            call.resolve()
        case "clearTriggers":
            OneSignal.InAppMessages.clearTriggers()
            call.resolve()
        case "setPaused":
            OneSignal.InAppMessages.paused = args.first as? Bool ?? false
            call.resolve()
        case "isPaused":
            call.resolve(["value": OneSignal.InAppMessages.paused])
        case "addOutcome":
            OneSignal.Session.addOutcome(args.first as? String ?? "")
            call.resolve()
        case "addUniqueOutcome":
            OneSignal.Session.addUniqueOutcome(args.first as? String ?? "")
            call.resolve()
        case "addOutcomeWithValue":
            OneSignal.Session.addOutcome(args.first as? String ?? "", (args.count > 1 ? args[1] as? NSNumber : nil)?.doubleValue ?? 0)
            call.resolve()
        case "enterLiveActivity", "exitLiveActivity", "setPushToStartToken", "removePushToStartToken", "setupDefaultLiveActivity", "startDefaultLiveActivity", "trackEvent", "addForegroundLifecycleListener", "addNotificationClickListener", "addPermissionObserver", "addPushSubscriptionObserver", "addUserStateObserver", "setInAppMessageClickHandler", "setOnWillDisplayInAppMessageHandler", "setOnDidDisplayInAppMessageHandler", "setOnWillDismissInAppMessageHandler", "setOnDidDismissInAppMessageHandler":
            call.resolve()
        default:
            call.reject("Unsupported OneSignal action: \(action)")
        }
    }

    private func emptyToNull(_ value: String?) -> Any {
        guard let value = value, !value.isEmpty else { return NSNull() }
        return value
    }
}
