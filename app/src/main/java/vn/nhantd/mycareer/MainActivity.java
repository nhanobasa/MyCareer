package vn.nhantd.mycareer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import vn.nhantd.mycareer.databinding.ActivityMainBinding;
import vn.nhantd.mycareer.firebase.auth.FirebaseAuthentication;
import vn.nhantd.mycareer.firebase.messaging.CloudMessagingUtils;
import vn.nhantd.mycareer.fragment.EmployerFragment;
import vn.nhantd.mycareer.fragment.HomeFragment;
import vn.nhantd.mycareer.fragment.NotificationFragment;
import vn.nhantd.mycareer.fragment.ProfileUserFragment;
import vn.nhantd.mycareer.util.CheckNetWork;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "FCM";
    private ActivityMainBinding binding;
    private BottomNavigationView navigationView;
    private LinearLayout linearLayoutStartApp;
    private LinearLayout linearLayoutRunApp;
    private Button btnMenu;
    Fragment fragmentSelected;
    int menuItem = 0;

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // subscribeToTopic
        String topic = "MyCareer";
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Đăng ký chủ đề " + topic + " thành công!";
                        if (!task.isSuccessful()) {
                            msg = "Đăng ký chủ đề " + topic + " thất bại!";
                        }
                        Log.d(TAG, msg);
                    }
                });

        init();
        // check xem user có thực hiện action với job hay không
        checkAction();
        setAnimationLayout();
        handler();
        CloudMessagingUtils.setTokenFCM(getApplicationContext());

    }

    private void init() {

        System.out.println("INIT");

        // find view by ID
        linearLayoutStartApp = findViewById(R.id.layout_start_app);
        linearLayoutRunApp = findViewById(R.id.layout_run_app);
        btnMenu = findViewById(R.id.btn_menu);

        // bottom navigation and fragment
        loadFragment(HomeFragment.newInstance("", "Chery"));
        navigationView = findViewById(R.id.bottom_navigation);
        fragmentSelected = HomeFragment.newInstance("", "");

        menuItem = R.id.nav_home;
        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    menuItem = R.id.nav_home;
                    fragmentSelected = new HomeFragment();
                    break;
                case R.id.nav_job:
                    menuItem = R.id.nav_job;
                    fragmentSelected = EmployerFragment.newInstance("", "");
                    break;
                case R.id.nav_notification:
                    if (checkLogin()) {
                        menuItem = R.id.nav_notification;
                        fragmentSelected = NotificationFragment.newInstance("", "");
                        break;
                    }
                    break;
                case R.id.nav_profile_user:
                    if (checkLogin()) {
                        menuItem = R.id.nav_profile_user;
                        fragmentSelected = ProfileUserFragment.newInstance("", "");
                        break;
                    }

                    break;
            }


            loadFragment(fragmentSelected);
            return true;

        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    //animation
    private void setAnimationLayout() {
        Animation ani = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation);
        ani.setStartOffset(1200);
        ani.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                checkNetworkConnected();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        linearLayoutStartApp.startAnimation(ani);
    }

    private void checkNetworkConnected() {
        boolean checkNetwork = CheckNetWork.isOnline(getApplicationContext());

        if (checkNetwork == true) {
            linearLayoutRunApp.setVisibility(View.VISIBLE);
            linearLayoutStartApp.setVisibility(View.GONE);
        } else {
            Intent a = new Intent(MainActivity.this, ErrorActivity.class);
            startActivity(a);
        }
    }

    private void handler() {
        btnMenu.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            intent.putExtra("current-menu-item", menuItem);
            startActivity(intent);
        });
    }

    private boolean checkLogin() {
        user = FirebaseAuthentication.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivityForResult(intent, 101);
            return false;
        }

        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_CANCELED) {
                navigationView.setSelectedItemId(menuItem);
            }
        }
    }

    private void checkAction() {
        Intent intent = getIntent();
        boolean checkAction = intent.getBooleanExtra("check-action", false);
        if (checkAction) {
            linearLayoutRunApp.setVisibility(View.VISIBLE);
            linearLayoutStartApp.setVisibility(View.GONE);
        }
    }
}