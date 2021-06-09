package vn.nhantd.mycareer.firebase.auth;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.bson.types.ObjectId;

import vn.nhantd.mycareer.model.user.User;
import vn.nhantd.mycareer.model.user.User_career_goals;
import vn.nhantd.mycareer.model.user.User_career_goals_salary;
import vn.nhantd.mycareer.model.user.User_education;
import vn.nhantd.mycareer.model.user.User_experience;
import vn.nhantd.mycareer.model.user.User_references;
import vn.nhantd.mycareer.util.HexUtils;

public class FirebaseAuthentication {

    public static FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static User getProfileUser() {
        FirebaseUser user = getCurrentUser();
        return genUser(user);
    }

    public static User getProfileUser(FirebaseUser user) {
        return genUser(user);
    }

    private static User genUser(FirebaseUser user) {
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

            User u = User.builder()
                    ._id(new ObjectId(HexUtils.convertStringToHex(uid)).toHexString())
                    .uid(uid)
                    .name(name)
                    .photoUrl(photoUrl != null ? photoUrl.toString() : null)
                    .phone(phone)
                    .email(email)
                    ._partition("sync")
                    .build();

            u.setExperience(new User_experience());
            User_career_goals careerGoals = new User_career_goals();
            careerGoals.setSalary(new User_career_goals_salary());
            u.setCareer_goals(careerGoals);
            u.setEducation(new User_education());
            u.setReferences(new User_references());

            return u;
        }
        return null;
    }
}
