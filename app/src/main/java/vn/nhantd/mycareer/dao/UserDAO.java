package vn.nhantd.mycareer.dao;


import io.realm.RealmSet;
import vn.nhantd.mycareer.model.user.User;

public interface UserDAO {
    void add(User user);

    void update(User u);

    void delete(User u);

    User get(User u);

    RealmSet<User> getAll();

}
