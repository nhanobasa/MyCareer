package vn.nhantd.mycareer.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import vn.nhantd.mycareer.R;
import vn.nhantd.mycareer.adapter.JobForUserAdapter;
import vn.nhantd.mycareer.adapter.TopJobAdapter;
import vn.nhantd.mycareer.databinding.FragmentHomeBinding;
import vn.nhantd.mycareer.model.job.Job;
import vn.nhantd.mycareer.ui.HomeViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeViewModel model;
    private TopJobAdapter adapter;
    private JobForUserAdapter jobForUserAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        System.out.println("ON CREATE!!!!!!");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("Home fragment - create view");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        binding = FragmentHomeBinding.bind(v);
        mainProcess();

        // xử lý sự kiện cho scroll quá mức -> reload trang
        binding.swipeContainerHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // cho load lại data
                mainProcess();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (binding.swipeContainerHome != null) {
                                binding.swipeContainerHome.setRefreshing(false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, 1500);

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void mainProcess() {
        model = new ViewModelProvider(this).get(HomeViewModel.class);

        // Xử lý recycler view cho phần gợi ý top job
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.recyclerviewRecommendedTopJob.setLayoutManager(llm);

        model.getTopJob().observe(this, new Observer<List<Job>>() {
            @Override
            public void onChanged(List<Job> jobs) {
                if (jobs != null) {
                    adapter = new TopJobAdapter(getContext(), jobs);
                    binding.recyclerviewRecommendedTopJob.setAdapter(adapter);
                }
            }
        });

        // Xử lý recycler view cho phần gợi ý top job
        LinearLayoutManager llv = new LinearLayoutManager(getContext());
        llv.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerviewRecommendedJob.setLayoutManager(llv);

        model.getJob4User().observe(this, new Observer<List<Job>>() {
            @Override
            public void onChanged(List<Job> jobs) {
                if (jobs != null) {
                    jobForUserAdapter = new JobForUserAdapter(getContext(), jobs);
                    binding.recyclerviewRecommendedJob.setAdapter(jobForUserAdapter);
                }
            }
        });
    }
}