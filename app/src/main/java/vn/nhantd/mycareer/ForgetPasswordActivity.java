package vn.nhantd.mycareer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    private EditText email;
    private Button btnSendEmail;
    private Button btnGotIt;
    ImageView back;
    private static final String TAG = "ForgetPasswordActivity";
    private LinearLayout alertSentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        init();

    }

    private void init() {
        email = findViewById(R.id.txt_email_in_4get_passwd);
        btnSendEmail = findViewById(R.id.btn_send_email_4get_passwd);
        btnGotIt = findViewById(R.id.btn_got_it);
        alertSentEmail = findViewById(R.id.alert_sent_email);
        back = findViewById(R.id.back);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        btnSendEmail.setOnClickListener(v -> {
            String emailAddress = email.getText().toString();
            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email sent.");
                                alertSentEmail.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        });

        btnGotIt.setOnClickListener(v -> {
            alertSentEmail.setVisibility(View.GONE);
        });
        back.setOnClickListener(v -> finish());
    }
}