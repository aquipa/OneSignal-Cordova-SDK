export type ExecCallback = (...args: any[]) => unknown;
export type ExecArgs = unknown[];

type CordovaBridge = {
  exec: (
    success?: ExecCallback,
    error?: ExecCallback,
    service?: string,
    action?: string,
    args?: ExecArgs,
  ) => void;
};

type PluginListenerHandle = {
  remove: () => Promise<void> | void;
};

type CapacitorOneSignalPlugin = {
  exec?: (options: { service: string; action: string; args: ExecArgs }) => Promise<unknown>;
  addListener?: (
    eventName: string,
    listenerFunc: (event: unknown) => void,
  ) => Promise<PluginListenerHandle> | PluginListenerHandle;
};

type CapacitorBridge = {
  isNativePlatform?: () => boolean;
  isPluginAvailable?: (pluginName: string) => boolean;
  Plugins?: Record<string, CapacitorOneSignalPlugin | undefined>;
};

declare global {
  interface Window {
    Capacitor?: CapacitorBridge;
  }
}

const listenerActions = new Set([
  'addForegroundLifecycleListener',
  'addNotificationClickListener',
  'addPermissionObserver',
  'addPushSubscriptionObserver',
  'addUserStateObserver',
  'setInAppMessageClickHandler',
  'setOnWillDisplayInAppMessageHandler',
  'setOnDidDisplayInAppMessageHandler',
  'setOnWillDismissInAppMessageHandler',
  'setOnDidDismissInAppMessageHandler',
]);

function getCapacitorPlugin(): CapacitorOneSignalPlugin | undefined {
  const capacitor = window.Capacitor;
  if (!capacitor) {
    return undefined;
  }

  const plugin = capacitor.Plugins?.OneSignalPush ?? capacitor.Plugins?.OneSignal;
  if (!plugin) {
    return undefined;
  }

  if (capacitor.isNativePlatform && !capacitor.isNativePlatform()) {
    return undefined;
  }

  if (capacitor.isPluginAvailable && !capacitor.isPluginAvailable('OneSignalPush')) {
    return undefined;
  }

  return plugin;
}

function normalizeCapacitorResult(result: unknown): unknown {
  if (
    typeof result === 'object' &&
    result !== null &&
    'value' in result &&
    Object.keys(result).length === 1
  ) {
    return (result as { value: unknown }).value;
  }
  return result;
}

export function exec(
  success?: ExecCallback,
  error?: ExecCallback,
  service: string = 'OneSignalPush',
  action: string = '',
  args?: ExecArgs,
): void {
  const cordova = window.cordova as unknown as CordovaBridge | undefined;
  if (cordova?.exec) {
    if (args === undefined) {
      cordova.exec(success, error, service, action);
    } else {
      cordova.exec(success, error, service, action, args);
    }
    return;
  }

  const plugin = getCapacitorPlugin();
  if (!plugin?.exec) {
    throw new Error(
      'OneSignal native bridge is unavailable. Install this SDK as a Cordova plugin or sync the OneSignal Capacitor plugin with your native app.',
    );
  }

  if (listenerActions.has(action) && plugin.addListener && success) {
    void plugin.addListener(action, success);
  }

  void plugin
    .exec({ service, action, args: args ?? [] })
    .then((result) => {
      const value = normalizeCapacitorResult(result);
      if (!listenerActions.has(action)) {
        success?.(value);
      }
    })
    .catch((exception: unknown) => {
      error?.(exception);
    });
}
