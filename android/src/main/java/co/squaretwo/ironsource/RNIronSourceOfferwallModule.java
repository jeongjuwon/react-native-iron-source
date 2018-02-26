package co.squaretwo.ironsource;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.support.annotation.Nullable;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.OfferwallListener;

public class RNIronSourceOfferwallModule extends ReactContextBaseJavaModule {
    private static final String TAG = "RNIronSourceOfferwall";
    private static final int OFFER_WALL_REQUEST = 1;

    private ReactApplicationContext reactContext;

    public RNIronSourceOfferwallModule(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext = reactContext;
    }

    @Override
    public String getName() {
        return TAG;
    }

    @ReactMethod
    public void initializeOfferwall() {
      IronSource.setOfferwallListener(new OfferwallListener() {
        /**
          * Invoked when there is a change in the Offerwall availability status.
          * @param - available - value will change to YES when Offerwall are available.
          * You can then show the offerwall by calling showOfferwall(). Value will *change to NO when Offerwall isn't available.
          */
        @Override
        public void onOfferwallAvailable(boolean isAvailable) {
            String eventName = "";
            if (isAvailable) {
                eventName = "IronSource-onOfferwallAvailable";
            } else {
                eventName = "IronSource-onOfferwallUnavailable";
            }
            Log.d(TAG, eventName);
            sendEvent(eventName, null);
        }
        /**
          * Invoked when the Offerwall successfully loads for the user, after calling the 'showOfferwall' method
          */
        @Override
        public void onOfferwallOpened() {
            String eventName = "IronSource-onOfferwallOpened";
            Log.d(TAG, eventName);
            sendEvent(eventName, null);

        }
        /**
          * Invoked when the method 'showOfferWall' is called and the OfferWall fails to load.
          * @param error - A IronSourceError Object which represents the reason of 'showOfferwall' failure.
          */
        @Override
        public void onOfferwallShowFailed(IronSourceError error) {
          String eventName = "IronSource-onOfferwallShowFailed";
          Log.d(TAG, eventName);
          sendEvent(eventName, null);
        }

        /**
          * Invoked each time the user completes an Offer.
          * Award the user with the credit amount corresponding to the value of the *‘credits’ parameter.
          * @param credits - The number of credits the user has earned.
          * @param totalCredits - The total number of credits ever earned by the user.
          * @param totalCreditsFlag - In some cases, we won’t be able to provide the exact
          * amount of credits since the last event (specifically if the user clears
          * the app’s data). In this case the ‘credits’ will be equal to the ‘totalCredits’, and this flag will be ‘true’.
          * @return boolean - true if you received the callback and rewarded the user, otherwise false.
          */
        @Override
        public boolean onOfferwallAdCredited(int credits, int totalCredits, boolean totalCreditsFlag) {
          String eventName = "IronSource-onOfferwallAdCredited";
          Log.d(TAG, eventName);

          WritableMap params = Arguments.createMap();
          params.putInt("credits", credits);
          params.putInt("totalCredits", totalCredits);
          params.putBoolean("totalCreditsFlag", totalCreditsFlag);

          sendEvent(eventName, params);
          return true;
        }
        /**
          * Invoked when the method 'getOfferWallCredits' fails to retrieve
          * the user's credit balance info.
          * @param error - A IronSourceError object which represents the reason of 'getOfferwallCredits' failure.
          * If using client-side callbacks to reward users, it is mandatory to return true on this event
          */
        @Override
        public void onGetOfferwallCreditsFailed(IronSourceError error) {
          String eventName = "IronSource-onGetOfferwallCreditsFailed";
          Log.d(TAG, eventName);
          sendEvent(eventName, null);
        }
        /**
          * Invoked when the user is about to return to the application after closing
          * the Offerwall.
          */
        @Override
        public void onOfferwallClosed() {
          String eventName = "IronSource-onOfferwallClosed";
          Log.d(TAG, eventName);
          sendEvent(eventName, null);
        }
      });
    }

    @ReactMethod
    public void showOfferwall() {
      Log.d(TAG, "showOfferwall() called!!");
      boolean available = IronSource.isOfferwallAvailable();
      if (available) {
          Log.d(TAG, "isOfferwallAvailable() = true");
          IronSource.showOfferwall("DefaultOfferWall");
      } else {
          Log.d(TAG, "isOfferwallAvailable() = false");
      }
    }

    @ReactMethod
    public void getOfferwallCredits() {
      IronSource.getOfferwallCredits();
    }

    private void sendEvent(String eventName, @Nullable WritableMap params) {
        getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }
}
