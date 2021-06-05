package vn.nhantd.mycareer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import vn.nhantd.mycareer.firebase.auth.FirebaseAuthentication;

public class MenuActivity extends AppCompatActivity {
    private Button btnLogin;
    private Button btnLogout;
    FirebaseAuth auth;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        init();
        handler();
    }

    private void init() {
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuthentication.getCurrentUser();

        btnLogin = findViewById(R.id.btn_login_in_menu);
        btnLogout = findViewById(R.id.btn_logout_in_menu);

        if (user != null) {
            btnLogin.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);
        }

    }

    private void handler() {
        btnLogin.setOnClickListener(v -> {
            int menuItem = getIntent().getIntExtra("current-menu-item", 0);
            Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
            intent.putExtra("current-menu-item", menuItem);
            startActivity(intent);
        });
        btnLogout.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(MenuActivity.this, MainActivity.class));
            btnLogin.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);
        });
    }

}