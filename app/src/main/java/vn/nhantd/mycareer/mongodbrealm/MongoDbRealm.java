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
                .baseUrl("https://realm.mongodb.com")
                .build());

        RealmLog.setLevel(LogLevel.ALL);


        Credentials credentials = Credentials.anonymous();
        app.loginAsync(credentials, result -> {
            if (result.isSuccess()) {
                Log.v("QUICKSTART", "Successfully authenticated anonymously.");
                io.realm.mongodb.User u = app.currentUser();
                String partitionValue = "_patition=sync";
                SyncConfiguration config = new SyncConfiguration.Builder(
                        u,
                        partitionValue)
                        .allowQueriesOnUiThread(true)
                        .allowWritesOnUiThread(true)
                        .build();

                Realm.setDefaultConfiguration(config);

            } else {
                Log.e("QUICKSTART", "Failed to log in. Error: " + result.getError());
            }
        });

    }
}
