package vn.nhantd.mycareer.dao;

import android.util.Log;

import org.bson.types.ObjectId;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import io.realm.Realm;
import io.realm.log.LogLevel;
import io.realm.log.RealmLog;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.sync.SyncConfiguration;
import vn.nhantd.mycareer.model.user.User;

public class UserDAOImpl extends BaseDAO implements UserDAO {

    @Override
    public void add(User u) {

        try {
            FutureTask<String> task = new FutureTask(new Runnable() {
                @Override
                public void run() {
                    Realm.getInstanceAsync(Realm.getDefaultConfiguration(), new Realm.Callback() {
                        @Override
                        public void onSuccess(Realm realm) {
                            Log.v("UserDAO", "Successfully opened a realm.");
                            realm.executeTransaction(r -> {
                                r.insert(u);
                            });
                            Log.d("UserDAO", "add user successful");
                            // Don't forget to close your realm!
                            realm.close();
                        }
                    });
                }
            }, "test");

            ExecutorService executorService = Executors.newFixedThreadPool(2);
            executorService.execute(task);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update(ObjectId _id) {

    }

    @Override
    public void delete(ObjectId _id) {

    }

    @Override
    public void get(ObjectId _id) {

    }

    @Override
    public void getAll() {

    }


}
