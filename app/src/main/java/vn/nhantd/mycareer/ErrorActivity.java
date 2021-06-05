package vn.nhantd.mycareer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import vn.nhantd.mycareer.util.CheckNetWork;


public class ErrorActivity extends AppCompatActivity {

    private Button btnTryConnect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        init();
        btnTryConnect.setOnClickListener(v -> {
            if (CheckNetWork.isOnline(getApplicationContext())) {
                Intent a = new Intent(ErrorActivity.this, MainActivity.class);
                a.putExtra("error-network", false);
                startActivity(a);
            }
        });

    }

    private void init() {
        btnTryConnect = findViewById(R.id.btn_try_connect);
    }


}