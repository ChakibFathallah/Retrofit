package com.example.retrofit;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.retrofit.model.Comment;
import com.example.retrofit.model.Post;
import com.example.retrofit.utils.JsonPlaceHolderApi;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        //getPosts();

        getComments();

    }

    private void getPosts()
    {
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


}
