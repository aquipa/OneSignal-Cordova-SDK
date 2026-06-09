import { exec } from './bridge';
import { noop } from './helpers';

export default class Location {
  /**
   * Location
   */

  /**
   * Prompts the user for location permissions to allow geotagging from the OneSignal dashboard.
   * @returns void
   */
  requestPermission(): void {
    exec(noop, noop, 'OneSignalPush', 'requestLocationPermission', []);
  }

  /**
   * Disable or enable location collection (defaults to enabled if your app has location permission).
   * @param  {boolean} shared
   * @returns void
   */
  setShared(shared: boolean): void {
    exec(noop, noop, 'OneSignalPush', 'setLocationShared', [shared]);
  }

  /**
   * Whether location is currently shared with OneSignal.
   * @returns {Promise<boolean>}
   */
  isShared(): Promise<boolean> {
    return new Promise<boolean>((resolve, reject) => {
      exec(resolve, reject, 'OneSignalPush', 'isLocationShared', []);
    });
  }
}
