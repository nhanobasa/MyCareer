package vn.nhantd.mycareer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.nhantd.mycareer.api.ApiService;
import vn.nhantd.mycareer.model.Transaction;

public class ApplyActivity extends AppCompatActivity {
    private static final String TAG = "ApplyActivity";
    private Uri cvUri;
    private FirebaseStorage storage;
    private Transaction applyTransaction;
    private String job_id;
    Button btnUpload;
    Button btnConfirmApply;
    TextView processStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_apply);

        btnUpload = findViewById(R.id.btn_upload_cv);
        btnConfirmApply = findViewById(R.id.btn_confirm_apply);
        processStatus = findViewById(R.id.txt_apply_process_status);

        storage = FirebaseStorage.getInstance();

        Intent intent = getIntent();
        job_id = intent.getStringExtra("job_id");
        applyTransaction = (Transaction) intent.getSerializableExtra("apply_transaction");

        // btn upload cv form device to firebase storage and get file url

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("application/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1999);
            }
        });


        btnConfirmApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // [START upload_create_reference]
                // Create a storage reference from our app
                StorageReference storageRef = storage.getReference();

                // Nếu upload CV cá nhân lên
                if (cvUri != null) {
                    String fileName = cvUri.getLastPathSegment();
                    fileName = fileName.substring(fileName.lastIndexOf("/") + 1); // ví dụ: TranDucNhan-CV.pdf
                    // Upload file and metadata to the path 'images/mountains.jpg'
                    UploadTask uploadTask = storageRef.child("job/" + job_id + "/" + fileName).putFile(cvUri);

                    // thực hiện quá trình upload và đẩy transaction lên service
                    uploadFileProcess(uploadTask);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1999) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    cvUri = data.getData();
                    String fileName = cvUri.getLastPathSegment();
                    fileName = fileName.substring(fileName.lastIndexOf("/") + 1); // ví dụ: TranDucNhan-CV.pdf
                    btnUpload.setText(fileName);
                }
            }
        }
    }

    private void uploadFileProcess(UploadTask uploadTask) {
        // Listen for state changes, errors, and completion of the upload.
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                Log.d(TAG, "Upload is " + progress + "% done");
                btnUpload.setEnabled(false);
                processStatus.setText("Hồ sơ đang được tải lên " + progress + "%");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "Upload is paused");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Handle successful uploads on complete
                // ...
                Log.d(TAG, "Upload is done!");
                processStatus.setText("Hồ sơ đang được tải lên thành công!");
            }
        });

        // Sau khi tải file lên thành công, nhận url download file để add vào transaction
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return task.getResult().getStorage().getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
        urlTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Uri> task) {
                Uri uriDowload = task.getResult();
                Log.d(TAG, "URi download: " + uriDowload);
                if (job_id != null && applyTransaction != null) {
                    applyTransaction.getCv_path().add(uriDowload.toString());
                    callApiSetApplyTransaction(job_id, applyTransaction);
                }
            }
        });
    }

    private void callApiSetApplyTransaction(String job_id, Transaction applyTransaction) {
        ApiService.apiService.setJobTransaction(job_id, applyTransaction).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // Nếu cập nhật thành công
                try {
                    if (response.body().equals("OK")) {
                        Log.i(TAG, "Ứng tuyển thành công!");
                        btnUpload.setEnabled(true);
                        processStatus.setText("Nộp hồ sơ ứng tuyển thành công!");
                        Thread.sleep(2000);
                        setResult(Activity.RESULT_OK);
                        finish();
                    } else {
                        Log.i(TAG, "Thay đổi trạng thật bại!");
                        btnUpload.setEnabled(true);
                        processStatus.setText("Nộp hồ sơ ứng tuyển thất bại!");
                        Thread.sleep(2000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(TAG, "Thay đổi trạng thật bại!");
                try {
                    btnUpload.setEnabled(true);
                    processStatus.setText("Nộp hồ sơ ứng tuyển thất bại!");
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}