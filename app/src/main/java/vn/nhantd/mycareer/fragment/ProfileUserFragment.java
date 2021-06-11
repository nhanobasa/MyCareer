package vn.nhantd.mycareer.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.nhantd.mycareer.EditProfileUserActivity;
import vn.nhantd.mycareer.R;
import vn.nhantd.mycareer.adapter.WorkProgressAdapter;
import vn.nhantd.mycareer.api.ApiService;
import vn.nhantd.mycareer.databinding.FragmentProfileUserBinding;
import vn.nhantd.mycareer.model.user.User;
import vn.nhantd.mycareer.model.user.WorkProgress;
import vn.nhantd.mycareer.ui.ProfileUserViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileUserFragment extends Fragment {
    private FragmentProfileUserBinding binding;
    private User user;
    List<WorkProgress> workProgressList = new ArrayList<>();
    private WorkProgressAdapter adapter;
    ProfileUserViewModel model;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String TAG = "ProfileUserFragment";

    public ProfileUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileUserFragment newInstance(String param1, String param2) {
        ProfileUserFragment fragment = new ProfileUserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile_user, container, false);

//        binding = FragmentProfileUserBinding.inflate(inflater, container, false);
        binding = FragmentProfileUserBinding.bind(v);
        binding.btnProfileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfileUserActivity.class);
                intent.putExtra("profile-user", model.getUser().getValue());
                startActivityForResult(intent, 1);
            }
        });

        // Tạo view model
        model = new ViewModelProvider(this).get(ProfileUserViewModel.class);


        return binding.getRoot();
    }

    private void getAllWorkProgress(User user) {
        ApiService.apiService.getAllWorkProgressOfUser(user.getUid()).enqueue(new Callback<List<WorkProgress>>() {
            @Override
            public void onResponse(Call<List<WorkProgress>> call, Response<List<WorkProgress>> response) {

                if (response.body() != null) {
                    workProgressList = response.body();
                    adapter.setData(workProgressList);
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "get all work progress of user successful!");
                    Log.d(TAG, adapter.getData().toString());
                } else
                    Log.d(TAG, "get all work progress of user fail!");
            }

            @Override
            public void onFailure(Call<List<WorkProgress>> call, Throwable t) {
                Log.d(TAG, "get all work progress of user fail!");
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerviewProfileWorkProgress.setLayoutManager(llm);

        model.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    binding.setProfileUserViewModel(model);
                    if (user.getPhotoUrl() != null)
                        Picasso.get().load(user.getPhotoUrl()).into(binding.imgProfile);
                    adapter = new WorkProgressAdapter(getContext(), workProgressList);
                    binding.recyclerviewProfileWorkProgress.setAdapter(adapter);
                    //get list work progress
                    getAllWorkProgress(user);
                }
            }
        });


    }

    @Override
    public void onDestroy() {
        binding = null;
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                User result = (User) data.getSerializableExtra(EditProfileUserActivity.EXTRA_DATA);
                ApiService.apiService.updateUser(result.get_id(), result).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.body().equals("OK")) {
                            Log.d(TAG, "update user successful!");
                        } else {
                            Log.d(TAG, "update user failure!");
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d(TAG, "update user failure!");
                    }
                });
            }
        }
    }
}