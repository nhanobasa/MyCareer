package vn.nhantd.mycareer.model;

import android.net.Uri;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class User {
    private String uid;
    private String name;
    private String email;
    private Uri photoUrl;

}
