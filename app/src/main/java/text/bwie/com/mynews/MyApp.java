package text.bwie.com.mynews;

import com.mob.MobApplication;

import org.xutils.x;

/**
 * Created by XIn💕 on 2017/9/12.
 */


public class MyApp extends MobApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);

    }
}
