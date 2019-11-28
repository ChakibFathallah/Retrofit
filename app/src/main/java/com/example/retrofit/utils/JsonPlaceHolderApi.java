package com.example.retrofit.utils;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

import com.example.retrofit.model.Comment;
import com.example.retrofit.model.Post;

import java.util.List;
import java.util.Map;

public interface JsonPlaceHolderApi {

    @GET("posts")
    Call<List<Post>> getPosts(
            //@Query("userId") int userId,
            //@Query("userId") Integer userId2,
            @Query("userId") Integer[] userId,  //integer est nullable or que int ne l'est pas
            @Query("_sort") String sort,
            @Query("_order") String order);

    @GET("posts")
    Call<List<Post>> getPosts(@QueryMap Map<String,String> parameters);

    @GET("posts/{id}/comments")
    Call<List<Comment>> getComments(@Path("id") int postId);

    @GET
    Call<List<Comment>> getComments(@Url String url);

}
