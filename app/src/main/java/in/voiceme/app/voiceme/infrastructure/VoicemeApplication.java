package in.voiceme.app.voiceme.infrastructure;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.evernote.android.job.JobManager;
import com.facebook.FacebookSdk;
import com.squareup.otto.Bus;

import in.voiceme.app.voiceme.Module;
import in.voiceme.app.voiceme.loginV2.RefreshTokenJobCreator;
import in.voiceme.app.voiceme.services.ServiceFactory;
import in.voiceme.app.voiceme.services.WebService;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

/**
 * Created by Harish on 7/20/2016.
 */
public class VoicemeApplication extends Application {
    private static Auth auth;
    private static Bus bus;
    private static Context context;
    private WebService webService;
    private VoicemeApplication instance;

    public VoicemeApplication() {
        bus = new Bus();
    }

    public static Context getContext() {
        return context;
    }

    public static Auth getAuth() {
        return auth;
    }

    public static Bus getBus() {
        return bus;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //Fabric.with(this, new Crashlytics());
        auth = new Auth(this);
        FacebookSdk.sdkInitialize(this);
        Module.register(this);
        webService = ServiceFactory.createRetrofitService(WebService.class);
        Timber.plant(new Timber.DebugTree() {
            // Add the line number to the TAG
            @Override
            protected String createStackElementTag(StackTraceElement element) {
                return super.createStackElementTag(element) + ":" + element.getLineNumber();
            }
        });
        context = getApplicationContext();
        /**
         *Creates a periodic job to refresh token
         */
        JobManager.create(this).addJobCreator(new RefreshTokenJobCreator());

        initDatabase();
    }

    public WebService getWebService() {
        return webService;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initDatabase() {

        Realm.init(instance);

        RealmConfiguration realmConfiguration =
                new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();

        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
