package vn.nhantd.mycareer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import vn.nhantd.mycareer.R;
import vn.nhantd.mycareer.adapter.EmployerAdapter;
import vn.nhantd.mycareer.databinding.FragmentEmployerBinding;
import vn.nhantd.mycareer.model.employeer.Employer;
import vn.nhantd.mycareer.ui.EmployerViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmployerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployerFragment extends Fragment {
    private FragmentEmployerBinding binding;
    private EmployerViewModel model;
    private EmployerAdapter adapter;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EmployerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmployerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmployerFragment newInstance(String param1, String param2) {
        EmployerFragment fragment = new EmployerFragment();
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
        model = new ViewModelProvider(this).get(EmployerViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_employer, container, false);
        binding = FragmentEmployerBinding.bind(v);


        // Xử lý recycler view cho phần gợi ý top job
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerviewTopEmployer.setLayoutManager(llm);

        model.getmEmployer().observe(this, new Observer<List<Employer>>() {
            @Override
            public void onChanged(List<Employer> employers) {
                if (employers != null) {
                    adapter = new EmployerAdapter(getContext(), employers);
                    binding.recyclerviewTopEmployer.setAdapter(adapter);
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}