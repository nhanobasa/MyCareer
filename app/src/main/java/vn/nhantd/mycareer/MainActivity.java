package vn.nhantd.mycareer;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import vn.nhantd.mycareer.databinding.ActivityMainBinding;
import vn.nhantd.mycareer.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

    }

    private void init() {
        loadFragment(HomeFragment.newInstance("", "Chery"));
        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {
                Fragment fragmentSelected = null;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        fragmentSelected = HomeFragment.newInstance("", "Chery");
                        break;
//                    case R.id.nav_job:
//                        fragmentSelected = CuliFragment.newInstance(R.drawable.culi, "Culi");
//                        break;
//                    case R.id.nav_notification:
//                        fragmentSelected = MokaFragment.newInstance(R.drawable.moka, "Moka");
//                        break;
//                    case R.id.nav_profile_user:
//                        fragmentSelected = MokaFragment.newInstance(R.drawable.moka, "Moka");
//                        break;
                }
                loadFragment(fragmentSelected);
                return true;

            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack("fragment_home")
                .commit();
    }


}