import { exec } from './bridge';
import { noop } from './helpers';

export default class Session {
  /**
   * Outcomes
   */

  /**
   * Add an outcome with the provided name, captured against the current session.
   * @param  {string} name
   * @returns void
   */
  addOutcome(name: string): void {
    exec(noop, noop, 'OneSignalPush', 'addOutcome', [name]);
  }

  /**
   * Add a unique outcome with the provided name, captured against the current session.
   * @param  {string} name
   * @returns void
   */
  addUniqueOutcome(name: string): void {
    exec(noop, noop, 'OneSignalPush', 'addUniqueOutcome', [name]);
  }

  /**
   * Add an outcome with the provided name and value, captured against the current session.
   * @param  {string} name
   * @param  {number} value
   * @returns void
   */
  addOutcomeWithValue(name: string, value: number): void {
    exec(noop, noop, 'OneSignalPush', 'addOutcomeWithValue', [name, value]);
  }
}
