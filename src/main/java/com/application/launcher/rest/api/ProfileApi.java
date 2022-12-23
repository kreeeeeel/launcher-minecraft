package com.application.launcher.rest.api;

import com.application.launcher.rest.response.ProfileResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ProfileApi {

    @GET("/api/profile")
    Call<ProfileResponse> getProfile(@Header("Authorization") String token);

}
