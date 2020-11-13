package m2m.security;

import android.app.Activity;
import android.util.Log;

import io.flutter.plugin.common.EventChannel;

class DataToFlutter implements EventChannel.StreamHandler {

    public static String CHANNEL = "com.jzhu.counter/plugin";

    static EventChannel channel;

    private Activity activity;
    private Timer = new Timer();

    private DataToFlutter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onListen(Object arguments, EventChannel.EventSink events) {
        tim

    }

    @Override
    public void onCancel(Object arguments) {
        Log.i("FlutterPluginCounter", "FlutterPluginCounter:onCancel");
    }
}
