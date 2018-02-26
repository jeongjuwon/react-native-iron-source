'use strict';

import { NativeModules, DeviceEventEmitter } from 'react-native';

const RNIronSourceOfferwall = NativeModules.RNIronSourceOfferwall;

const eventHandlers = {
  'IronSource-onOfferwallAvailable': new Map(),
  'IronSource-onOfferwallUnavailable': new Map(),
  'IronSource-onOfferwallOpened': new Map(),
  'IronSource-onOfferwallShowFailed': new Map(),
  'IronSource-onOfferwallAdCredited': new Map(),
  'IronSource-onGetOfferwallCreditsFailed': new Map(),
  'IronSource-onOfferwallClosed': new Map(),
};

const addEventListener = (type, handler) => {
  switch (type) {
    case 'IronSource-onOfferwallAvailable':
    case 'IronSource-onOfferwallUnavailable':
    case 'IronSource-onOfferwallOpened':
    case 'IronSource-onOfferwallShowFailed':
    case 'IronSource-onOfferwallAdCredited':
    case 'IronSource-onGetOfferwallCreditsFailed':
    case 'IronSource-onOfferwallClosed':
      eventHandlers[type].set(handler, DeviceEventEmitter.addListener(type, handler));
      break;
    default:
      console.log(`Event with type ${type} does not exist.`);
  }
};

const removeEventListener = (type, handler) => {
  if (!eventHandlers[type].has(handler)) {
    return;
  }
  eventHandlers[type].get(handler).remove();
  eventHandlers[type].delete(handler);
};

const removeAllListeners = () => {
  DeviceEventEmitter.removeAllListeners('IronSource-onOfferwallAvailable');
  DeviceEventEmitter.removeAllListeners('IronSource-onOfferwallUnavailable');
  DeviceEventEmitter.removeAllListeners('IronSource-onOfferwallOpened');
  DeviceEventEmitter.removeAllListeners('IronSource-onOfferwallShowFailed');
  DeviceEventEmitter.removeAllListeners('IronSource-onOfferwallAdCredited');
  DeviceEventEmitter.removeAllListeners('IronSource-onGetOfferwallCreditsFailed');
  DeviceEventEmitter.removeAllListeners('IronSource-onOfferwallClosed');
};

module.exports = {
  ...RNIronSourceOfferwall,
  initializeOfferwall: () => RNIronSourceOfferwall.initializeOfferwall(),
  showOfferwall: () => RNIronSourceOfferwall.showOfferwall(),
  addEventListener,
  removeEventListener,
  removeAllListeners,
};
