package com.example.turkiyedepremharitasi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("pure_api.php")
    Call<List<Movie>> getLastMovies();
}