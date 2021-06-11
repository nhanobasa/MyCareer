package vn.nhantd.mycareer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import vn.nhantd.mycareer.model.user.Salary;

public class UpdateSalaryActivity extends AppCompatActivity {

    public static final String EXTRA_DATA = "UpdateSalaryActivity-extra-data";

    Spinner txtSalaryUnit;
    EditText txtSalaryFrom;
    EditText txtSalaryTo;
    ImageButton btnBack;
    Button btnUpdate;
    private Salary salary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_salary);

        // get data from edit activity
        Intent intent = getIntent();
        salary = (Salary) intent.getSerializableExtra("profile-salary");

        // Khởi tạo view
        initView();

        // xử lý sự kiện click

        actionClickHandler();
    }

    private void initView() {
        txtSalaryUnit = findViewById(R.id.txt_salary_unit);
        txtSalaryFrom = findViewById(R.id.txt_salary_from);
        txtSalaryTo = findViewById(R.id.txt_salary_to);

        btnBack = findViewById(R.id.btn_salary_back);
        btnUpdate = findViewById(R.id.btn_salary_update);

        // Nếu là thỏa thuận
        if (salary.getCurrency_unit().isEmpty() || salary.getCurrency_unit().equals("Thỏa thuận")) {
            salary.setCurrency_unit("Thỏa thuận");
            txtSalaryFrom.setEnabled(false);
            txtSalaryTo.setEnabled(false);
        }

        // set giá trị cho from salary & to salary
        txtSalaryFrom.setText(salary.getFrom_salary().toString());
        txtSalaryTo.setText(salary.getTo_salary().toString());

        // set giá trị cho spinner unit
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item
                , getResources().getStringArray(R.array.salary_unit));
        txtSalaryUnit.setAdapter(arrayAdapter);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        txtSalaryUnit.setSelection(arrayAdapter.getPosition(salary.getCurrency_unit()));

        // Khởi tạo spinner
        txtSalaryUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getSelectedItem().toString();

                // Nếu là thỏa thuận thì form_salary và to_salary bằng 0
                if (position == 0) {
                    txtSalaryFrom.setText("");
                    txtSalaryTo.setText("");
                } else {
                    txtSalaryFrom.setEnabled(true);
                    txtSalaryTo.setEnabled(true);

                    // set giá trị cho from salary & to salary
                    txtSalaryFrom.setText(salary.getFrom_salary().toString());
                    txtSalaryTo.setText(salary.getTo_salary().toString());
                }

                salary.setCurrency_unit(item);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void actionClickHandler() {

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        // set onClick for update button
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                // thực hiện update user

                if (txtSalaryFrom.getText().toString().equals("")) {
                    salary.setFrom_salary(0.0);
                } else {
                    salary.setFrom_salary(Double.valueOf(txtSalaryFrom.getText().toString()));
                }

                if (txtSalaryTo.getText().toString().equals("")) {
                    salary.setTo_salary(0.0);
                } else {
                    salary.setTo_salary(Double.valueOf(txtSalaryTo.getText().toString()));
                }

                data.putExtra(EXTRA_DATA, salary);
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });

    }

}