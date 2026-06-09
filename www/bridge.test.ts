import { beforeEach, describe, expect, test, vi } from 'vite-plus/test';

import { exec } from './bridge';

const flushPromises = () => new Promise<void>((resolve) => setTimeout(resolve, 0));

describe('native bridge', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    delete (window as { cordova?: unknown }).cordova;
    delete window.Capacitor;
  });

  test('routes calls through Cordova when available', () => {
    const cordovaExec = vi.fn();
    (window as unknown as { cordova?: { exec: typeof cordovaExec } }).cordova = {
      exec: cordovaExec,
    };
    const success = vi.fn();
    const error = vi.fn();

    exec(success, error, 'OneSignalPush', 'login', ['external-id']);

    expect(cordovaExec).toHaveBeenCalledWith(success, error, 'OneSignalPush', 'login', [
      'external-id',
    ]);
  });

  test('routes calls through Capacitor when Cordova is unavailable', async () => {
    const capacitorExec = vi.fn().mockResolvedValue({ value: true });
    window.Capacitor = {
      isNativePlatform: () => true,
      isPluginAvailable: () => true,
      Plugins: {
        OneSignalPush: {
          exec: capacitorExec,
        },
      },
    };
    const success = vi.fn();
    const error = vi.fn();

    exec(success, error, 'OneSignalPush', 'getPermissionInternal', []);
    await flushPromises();

    expect(capacitorExec).toHaveBeenCalledWith({
      service: 'OneSignalPush',
      action: 'getPermissionInternal',
      args: [],
    });
    expect(success).toHaveBeenCalledWith(true);
    expect(error).not.toHaveBeenCalled();
  });

  test('maps Capacitor listeners to persistent callback actions', () => {
    const capacitorExec = vi.fn().mockResolvedValue(undefined);
    const addListener = vi.fn();
    window.Capacitor = {
      isNativePlatform: () => true,
      isPluginAvailable: () => true,
      Plugins: {
        OneSignalPush: {
          exec: capacitorExec,
          addListener,
        },
      },
    };
    const success = vi.fn();

    exec(success, vi.fn(), 'OneSignalPush', 'addPermissionObserver', []);

    expect(addListener).toHaveBeenCalledWith('addPermissionObserver', success);
  });

  test('throws a setup error when no native bridge is available', () => {
    expect(() => exec(vi.fn(), vi.fn(), 'OneSignalPush', 'login', ['external-id'])).toThrow(
      'OneSignal native bridge is unavailable',
    );
  });
});
