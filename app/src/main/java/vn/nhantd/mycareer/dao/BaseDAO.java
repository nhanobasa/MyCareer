package vn.nhantd.mycareer.dao;

import android.util.Log;

import com.google.android.gms.tasks.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.log.LogLevel;
import io.realm.log.RealmLog;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;
import vn.nhantd.mycareer.BuildConfig;

public abstract class BaseDAO {
    Realm realm;
    App app;

    private void addChangeListenerToRealm(Realm uiThreadRealm) {
        // all Users in the realm
        RealmResults<vn.nhantd.mycareer.model.user.User> users = uiThreadRealm.where(vn.nhantd.mycareer.model.user.User.class).findAllAsync();

        users.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<vn.nhantd.mycareer.model.user.User>>() {
            @Override
            public void onChange(RealmResults<vn.nhantd.mycareer.model.user.User> users, OrderedCollectionChangeSet changeSet) {
                //
            }
        });

    }
}
