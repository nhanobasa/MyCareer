package vn.nhantd.mycareer.dao;

import android.util.Log;

import org.bson.types.ObjectId;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReference;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.RealmSet;
import vn.nhantd.mycareer.model.user.User;
import vn.nhantd.mycareer.util.HexUtils;

public class UserDAOImpl extends BaseDAO implements UserDAO {

    @Override
    public void add(User u) {

        // Phải thực hiện các giao dịch trong luồng khác, không được thực hiện ở luồng chính.
        try {
            FutureTask<String> task = new FutureTask(new Runnable() {
                @Override
                public void run() {
                    Realm realm = Realm.getDefaultInstance();
                    Log.v("UserDAO", "Successfully opened a realm.");
                    realm.executeTransaction(r -> {
                        r.insert(u);
                    });
                    Log.d("UserDAO", "add user successful");
                    // Don't forget to close your realm!
                    realm.close();
                }
            }, "test");

            ExecutorService executorService = Executors.newFixedThreadPool(1);
            executorService.execute(task);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update(User u) {
        try {
            FutureTask<String> task = new FutureTask(new Runnable() {
                @Override
                public void run() {
                    Realm realm = Realm.getDefaultInstance();
                    Log.v("UserDAO", "Successfully opened a realm.");
                    realm.executeTransaction(r -> {
                        u.set_id(new ObjectId(HexUtils.convertStringToHex(u.getUid())));
                        r.copyToRealmOrUpdate(u);
                    });
                    Log.d("UserDAO", "add user successful");
                    // Don't forget to close your realm!
                    realm.close();
                }
            }, "test");

            ExecutorService executorService = Executors.newFixedThreadPool(1);
            executorService.execute(task);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(User u) {
        try {
            FutureTask<String> task = new FutureTask(new Runnable() {
                @Override
                public void run() {
                    Realm realm = Realm.getDefaultInstance();
                    Log.v("UserDAO", "Successfully opened a realm.");
                    realm.executeTransaction(r -> {
                        User user = r.where(User.class).equalTo("_id", u.get_id()).or().equalTo("uid", u.getUid()).findFirst();
                        user.deleteFromRealm();

                    });
                    Log.d("UserDAO", "add user successful");
                    // Don't forget to close your realm!
                    realm.close();
                }
            }, "test");

            ExecutorService executorService = Executors.newFixedThreadPool(1);
            executorService.execute(task);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public User get(User u) {
        try {
            realm = Realm.getDefaultInstance();
            Log.v("UserDAO", "Successfully opened a realm.");
            User user = realm.where(User.class).
                    equalTo("_id", u.get_id())
                    .or()
                    .equalTo("uid", u.getUid())
                    .findFirst();

            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public RealmSet<vn.nhantd.mycareer.model.user.User> getAll() {
        try {
            AtomicReference<RealmResults<User>> a = null;
            FutureTask<vn.nhantd.mycareer.model.user.User> task = new FutureTask(new Runnable() {
                @Override
                public void run() {
                    Realm realm = Realm.getDefaultInstance();
                    Log.v("UserDAO", "Successfully opened a realm.");
                    realm.executeTransaction(r -> {
                        RealmResults<vn.nhantd.mycareer.model.user.User> user = r.where(vn.nhantd.mycareer.model.user.User.class).findAll();

                        a.set(user);
                    });
                    Log.d("UserDAO", "add user successful");
                    // Don't forget to close your realm!
                    realm.close();
                }
            }, a);

            ExecutorService executorService = Executors.newFixedThreadPool(1);
            executorService.execute(task);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        realm.close();
    }
}
