package vn.nhantd.mycareer.firebase.auth;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import vn.nhantd.mycareer.model.user.User;

public class FirebaseAuthentication {

    public static FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static User getProfileUser() {
        FirebaseUser user = getCurrentUser();

        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            String phone = user.getPhoneNumber();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
//        boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
            User u = new User();
            u.setUid(uid);
            u.setName(name);
            assert photoUrl != null;
            u.setPhotoUrl(photoUrl.toString());
            u.setPhone(phone);
            u.setEmail(email);

            return u;
        }

        return null;
    }

    public static User getProfileUser(FirebaseUser user) {

        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            String phone = user.getPhoneNumber();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
//        boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
            User u = new User();
            u.setUid(uid);
            u.setName(name);
            u.setPhotoUrl(photoUrl.toString());
            u.setPhone(phone);
            u.setEmail(email);

            return u;
        }

        return null;
    }
}
