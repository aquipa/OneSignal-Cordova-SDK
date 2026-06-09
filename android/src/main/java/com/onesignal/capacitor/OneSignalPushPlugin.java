package com.onesignal.capacitor;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.onesignal.Continue;
import com.onesignal.OneSignal;
import com.onesignal.common.OneSignalWrapper;
import com.onesignal.debug.LogLevel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@CapacitorPlugin(name = "OneSignalPush")
public class OneSignalPushPlugin extends Plugin {
    @PluginMethod
    public void exec(PluginCall call) {
        String action = call.getString("action", "");
        JSArray jsArgs = call.getArray("args", new JSArray());
        JSONArray args = new JSONArray(jsArgs.toList());

        try {
            Object result = executeAction(action, args, call);
            if (result != null) {
                JSObject response = new JSObject();
                response.put("value", result);
                call.resolve(response);
            } else {
                call.resolve();
            }
        } catch (Throwable t) {
            call.reject(t.getMessage() == null ? "OneSignal native call failed" : t.getMessage(), t);
        }
    }

    private Object executeAction(String action, JSONArray args, PluginCall call) throws JSONException {
        switch (action) {
            case "init":
                OneSignalWrapper.sdkType = "capacitor";
                OneSignalWrapper.sdkVersion = "050311";
                OneSignal.initWithContext(getContext(), args.getString(0));
                return null;
            case "setLanguage":
                OneSignal.getUser().setLanguage(args.getString(0));
                return null;
            case "login":
                OneSignal.login(args.getString(0));
                return null;
            case "logout":
                OneSignal.logout();
                return null;
            case "setPrivacyConsentRequired":
                OneSignal.setConsentRequired(args.getBoolean(0));
                return null;
            case "setPrivacyConsentGiven":
                OneSignal.setConsentGiven(args.getBoolean(0));
                return null;
            case "setLogLevel":
                OneSignal.getDebug().setLogLevel(LogLevel.fromInt(args.getInt(0)));
                return null;
            case "setAlertLevel":
                OneSignal.getDebug().setAlertLevel(LogLevel.fromInt(args.getInt(0)));
                return null;
            case "addAliases":
                OneSignal.getUser().addAliases(toStringMap(args.getJSONObject(0)));
                return null;
            case "removeAliases":
                OneSignal.getUser().removeAliases(toStringCollection(args));
                return null;
            case "addTags":
                OneSignal.getUser().addTags(toStringMap(args.getJSONObject(0)));
                return null;
            case "removeTags":
                OneSignal.getUser().removeTags(toStringCollection(args));
                return null;
            case "getTags":
                return new JSONObject(OneSignal.getUser().getTags());
            case "getOnesignalId":
                return emptyToNull(OneSignal.getUser().getOnesignalId());
            case "getExternalId":
                return emptyToNull(OneSignal.getUser().getExternalId());
            case "addEmail":
                OneSignal.getUser().addEmail(args.getString(0));
                return null;
            case "removeEmail":
                OneSignal.getUser().removeEmail(args.getString(0));
                return null;
            case "addSms":
                OneSignal.getUser().addSms(args.getString(0));
                return null;
            case "removeSms":
                OneSignal.getUser().removeSms(args.getString(0));
                return null;
            case "getPushSubscriptionId":
                return emptyToNull(OneSignal.getUser().getPushSubscription().getId());
            case "getPushSubscriptionToken":
                return emptyToNull(OneSignal.getUser().getPushSubscription().getToken());
            case "getPushSubscriptionOptedIn":
                return OneSignal.getUser().getPushSubscription().getOptedIn();
            case "optInPushSubscription":
                OneSignal.getUser().getPushSubscription().optIn();
                return null;
            case "optOutPushSubscription":
                OneSignal.getUser().getPushSubscription().optOut();
                return null;
            case "getPermissionInternal":
                return OneSignal.getNotifications().getPermission();
            case "permissionNative":
                return OneSignal.getNotifications().getPermission() ? 2 : 1;
            case "canRequestPermission":
                return OneSignal.getNotifications().getCanRequestPermission();
            case "requestPermission":
                requestPermission(call, args.optBoolean(0, false));
                return null;
            case "registerForProvisionalAuthorization":
                return true;
            case "clearAllNotifications":
                OneSignal.getNotifications().clearAllNotifications();
                return null;
            case "removeNotification":
                OneSignal.getNotifications().removeNotification(args.getInt(0));
                return null;
            case "removeGroupedNotifications":
                OneSignal.getNotifications().removeGroupedNotifications(args.getString(0));
                return null;
            case "requestLocationPermission":
                OneSignal.getLocation().requestPermission(Continue.none());
                return null;
            case "setLocationShared":
                OneSignal.getLocation().setShared(args.getBoolean(0));
                return null;
            case "isLocationShared":
                return OneSignal.getLocation().isShared();
            case "addTriggers":
                OneSignal.getInAppMessages().addTriggers(toStringMap(args.getJSONObject(0)));
                return null;
            case "removeTriggers":
                OneSignal.getInAppMessages().removeTriggers(toStringList(args.getJSONArray(0)));
                return null;
            case "clearTriggers":
                OneSignal.getInAppMessages().clearTriggers();
                return null;
            case "setPaused":
                OneSignal.getInAppMessages().setPaused(args.getBoolean(0));
                return null;
            case "isPaused":
                return OneSignal.getInAppMessages().getPaused();
            case "addOutcome":
                OneSignal.getSession().addOutcome(args.getString(0));
                return null;
            case "addUniqueOutcome":
                OneSignal.getSession().addUniqueOutcome(args.getString(0));
                return null;
            case "addOutcomeWithValue":
                OneSignal.getSession().addOutcomeWithValue(args.getString(0), (float) args.optDouble(1));
                return null;
            case "trackEvent":
                OneSignal.getUser().trackEvent(args.getString(0), args.length() > 1 ? toObjectMap(args.getJSONObject(1)) : null);
                return null;
            case "enterLiveActivity":
            case "exitLiveActivity":
            case "setPushToStartToken":
            case "removePushToStartToken":
            case "setupDefaultLiveActivity":
            case "startDefaultLiveActivity":
            case "addForegroundLifecycleListener":
            case "addNotificationClickListener":
            case "addPermissionObserver":
            case "addPushSubscriptionObserver":
            case "addUserStateObserver":
            case "setInAppMessageClickHandler":
            case "setOnWillDisplayInAppMessageHandler":
            case "setOnDidDisplayInAppMessageHandler":
            case "setOnWillDismissInAppMessageHandler":
            case "setOnDidDismissInAppMessageHandler":
                return null;
            default:
                throw new JSONException("Unsupported OneSignal action: " + action);
        }
    }

    private void requestPermission(PluginCall call, boolean fallbackToSettings) {
        if (OneSignal.getNotifications().getPermission()) {
            JSObject response = new JSObject();
            response.put("value", true);
            call.resolve(response);
            return;
        }

        OneSignal.getNotifications().requestPermission(fallbackToSettings, Continue.with(result -> {
            if (result.isSuccess()) {
                JSObject response = new JSObject();
                response.put("value", result.getData());
                call.resolve(response);
            } else {
                call.reject(result.getThrowable().getMessage(), result.getThrowable());
            }
        }));
    }

    private static String emptyToNull(String value) {
        return value == null || value.isEmpty() ? null : value;
    }

    private static Map<String, String> toStringMap(JSONObject object) throws JSONException {
        Map<String, String> map = new HashMap<>();
        Iterator<String> keys = object.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            map.put(key, object.get(key).toString());
        }
        return map;
    }

    private static Collection<String> toStringCollection(JSONArray array) throws JSONException {
        Collection<String> collection = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            collection.add(array.get(i).toString());
        }
        return collection;
    }

    private static ArrayList<String> toStringList(JSONArray array) throws JSONException {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(array.getString(i));
        }
        return list;
    }

    private static Map<String, Object> toObjectMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<>();
        Iterator<String> keys = object.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = object.get(key);
            map.put(key, value == JSONObject.NULL ? null : value);
        }
        return map;
    }
}
