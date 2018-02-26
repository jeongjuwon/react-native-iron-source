package co.squaretwo.ironsource;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.support.annotation.Nullable;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.integration.IntegrationHelper;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.RewardedVideoListener;
import com.ironsource.adapters.supersonicads.SupersonicConfig;

public class RNIronSourceModule extends ReactContextBaseJavaModule {
    private static final String TAG = "RNIronSource";
    private static final int OFFER_WALL_REQUEST = 1;

    private ReactApplicationContext reactContext;

    public RNIronSourceModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return TAG;
    }

    @ReactMethod
    public void initializeIronSource(final String appId, final String userId) {
        IronSource.setUserId(userId);
        // SupersonicConfig.getConfigObj().setClientSideCallbacks(true);
        IronSource.init(reactContext.getCurrentActivity(), appId);
    }

    @ReactMethod
    public void getTestDeviceId() {
        IntegrationHelper.validateIntegration(reactContext.getCurrentActivity());
    }

    @ReactMethod
    public void onResume() {
        IronSource.onResume(reactContext.getCurrentActivity());
    }

    @ReactMethod
    public void onPause() {
        IronSource.onPause(reactContext.getCurrentActivity());
    }
}
