package vn.nhantd.mycareer.dao;

import org.bson.types.ObjectId;

import vn.nhantd.mycareer.model.user.User;

public interface UserDAO {
    void add(User user);

    void update(ObjectId _id);

    void delete(ObjectId _id);

    void get(ObjectId _id);

    void getAll();

}
