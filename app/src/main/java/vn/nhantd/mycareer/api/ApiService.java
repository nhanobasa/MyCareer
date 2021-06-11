package vn.nhantd.mycareer.api;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.nhantd.mycareer.model.employeer.Employer;
import vn.nhantd.mycareer.model.job.CareerCategory;
import vn.nhantd.mycareer.model.job.Job;
import vn.nhantd.mycareer.model.user.User;
import vn.nhantd.mycareer.model.user.WorkProgress;

public interface ApiService {

    Gson gson = new GsonBuilder()
            .setLenient()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(40, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
//            .addInterceptor(new RequestInterceptor())// This is used to add ApplicationInterceptor.
//            .addNetworkInterceptor(new RequestInterceptor())// This is used to add NetworkInterceptor.
            .build();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl("http://192.168.1.9:10399/mycareer/api/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);


    @POST("user/add")
    Call<User> createUsr(@Body User user);

    @GET("user/profile")
    Call<User> getUser(@Query("id") String _id);

    @GET("user/work_progress")
    Call<List<WorkProgress>> getAllWorkProgressOfUser(@Query("user_id") String user_id);

    @POST("user/{id}")
    Call<String> updateUser(@Path("id") String _id, @Body User user);

    @GET("category/top")
    Call<List<CareerCategory>> getTopCareerCategory();

    @GET("job/all")
    Call<List<Job>> getAllJobs(@Query("limit") Integer limit);

    @GET("job/all/{user_id}")
    Call<List<Job>> getJobForUser( @Path("user_id") String _id, @Query("limit") Integer limit);

    @GET("employer/profile")
    Call<Employer> getEmployer(@Query("id") String _id);
}
