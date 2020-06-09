package app.dev.lead.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://vetlink2.pvamu.edu/lead/";
//    private static final String BASE_URL = "http://192.168.0.136/lead/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            int timeOut = 5 * 60;

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(timeOut, TimeUnit.SECONDS)
                    .writeTimeout(timeOut, TimeUnit.SECONDS)
                    .readTimeout(timeOut, TimeUnit.SECONDS)
                    .build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

             retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();


        }
        return retrofit;
    }
}
