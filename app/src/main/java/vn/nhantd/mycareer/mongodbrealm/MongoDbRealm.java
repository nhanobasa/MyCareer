package vn.nhantd.mycareer.mongodbrealm;

import android.app.Application;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.log.LogLevel;
import io.realm.log.RealmLog;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.sync.SyncConfiguration;

public class MongoDbRealm extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        String appID = "mycareer-kywcm";
        App app = new App(new AppConfiguration.Builder(appID)
                .appName("MyCareer")
                .appVersion("1.0")
                .baseUrl("https://realm.mongodb.com")
                .build());

        RealmLog.setLevel(LogLevel.ALL);


        Credentials credentials = Credentials.emailPassword("tdnhan.it@gmail.com", "123456");
        app.loginAsync(credentials, result -> {
            if (result.isSuccess()) {
                Log.v("Realm", "Successfully authenticated email.");
                io.realm.mongodb.User u = app.currentUser();

                String partitionValue = "sync";
                SyncConfiguration config = new SyncConfiguration.Builder(
                        u,
                        partitionValue)
                        .allowQueriesOnUiThread(true)
                        .allowWritesOnUiThread(true)
                        .build();

                Realm.setDefaultConfiguration(config);

            } else {
                Log.e("Realm", "Failed to log in. Error: " + result.getError());
            }
        });

    }
}
