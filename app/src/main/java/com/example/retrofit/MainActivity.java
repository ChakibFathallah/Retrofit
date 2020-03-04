package com.example.retrofit;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.retrofit.model.Comment;
import com.example.retrofit.model.Post;
import com.example.retrofit.utils.JsonPlaceHolderApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView textViewResult;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.text_view_result);

        // for patch hardcoded it and set null value
        Gson gson  = new GsonBuilder().serializeNulls().create();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                // custom header for all request
                /*
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        Request newRequest  = originalRequest.newBuilder()
                                .header("Interceptor-header","xyz")
                                .build();

                        return chain.proceed(newRequest);
                    }
                })

                 */
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

       // getPosts();

        //getComments();

        //createPost();

          updatePost();

         //deletePost();

    }



    private void getPosts()
    {

        // QueryMap
       /* Map<String,String> parameters = new HashMap<>();
        parameters.put("user_id","10");
        parameters.put("_sort","id");
        parameters.put("_order","desc");

        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(parameters);

        */


       // Call<List<Post>> call = jsonPlaceHolderApi.getPosts(1,"id", "desc");
        // Call<List<Post>> call = jsonPlaceHolderApi.getPosts(4,1,null, null);
       Call<List<Post>> call = jsonPlaceHolderApi.getPosts(new Integer[]{6,2,4},null, null);

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if (!response.isSuccessful()){
                    textViewResult.setText("Code: " + response.code());
                    return;
                }
                List<Post> posts = response.body();

                for (Post post: posts){
                    String content = "";
                    content += "Id = " + post.getId() + "\n" ;
                    content += "userId = " + post.getUserId() + "\n" ;
                    content += "title = " + post.getTitle() + "\n" ;
                    content += "text = " + post.getText() + "\n\n" ;

                    textViewResult.append(content);

                }

            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    private void getComments()
    {
        //Call<List<Comment>> call = jsonPlaceHolderApi.getComments(2);
        Call<List<Comment>> call = jsonPlaceHolderApi
                                    .getComments("posts/3/comments");

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (!response.isSuccessful()){
                    textViewResult.setText("code:" + response.code());
                    return;
                }

                List<Comment> comments = response.body();

                for (Comment comment : comments ){
                    String content = "";
                    content += "Id = " + comment.getId() + "\n" ;
                    content += "postId = " + comment.getPostId() + "\n" ;
                    content += "name = " + comment.getName() + "\n" ;
                    content += "email = " + comment.getEmail() + "\n" ;
                    content += "text = " + comment.getText() + "\n\n" ;

                    textViewResult.append(content);
                }

            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }


    private void createPost(){

        //Post post = new Post(23,"new title","new text");
        //Call<Post> call = jsonPlaceHolderApi.createPost(post);


       // Call<Post> call = jsonPlaceHolderApi.createPost(23,"new title","new text");

        Map<String,String> fields = new HashMap<>();
        fields.put("userId","25");
        fields.put("Title","New title");

        Call<Post> call = jsonPlaceHolderApi.createPost(fields);


        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if (!response.isSuccessful()){
                    textViewResult.setText("code"+ response.code());
                    return;
                }

                Post postResponse = response.body();

                String content = "";
                content += "Code" + response.code() + "\n" ;
                content += "ID = " + postResponse.getId() + "\n" ;
                content += "User ID = " + postResponse.getUserId() + "\n" ;
                content += "Title = " + postResponse.getTitle() + "\n" ;
                content += "text = " + postResponse.getText() + "\n\n" ;

                textViewResult.setText(content);

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

                textViewResult.setText(t.getMessage());

            }
        });
    }


    private void updatePost(){
        Post post = new Post(12,null,"new text");
       // Call<Post>  call = jsonPlaceHolderApi.putPost(5,post);
       // Call<Post>  call = jsonPlaceHolderApi.putPost("abc",5,post);

        Map<String,String> headers = new HashMap<>();
        headers.put("Map-Header1","def");
        headers.put("Map-Header2","ghi");
        Call<Post>  call = jsonPlaceHolderApi.patchPost(headers,5,post);


        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()){
                    textViewResult.setText("code"+ response.code());
                    return;
                }

                Post postResponse = response.body();

                String content = "";
                content += "Code" + response.code() + "\n" ;
                content += "ID = " + postResponse.getId() + "\n" ;
                content += "User ID = " + postResponse.getUserId() + "\n" ;
                content += "Title = " + postResponse.getTitle() + "\n" ;
                content += "text = " + postResponse.getText() + "\n\n" ;

                textViewResult.setText(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }


    private void deletePost() {
        Call<Void> call = jsonPlaceHolderApi.deletePost(5);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                textViewResult.setText("Code" + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                textViewResult.setText( t.getMessage());
            }
        });
    }

}
