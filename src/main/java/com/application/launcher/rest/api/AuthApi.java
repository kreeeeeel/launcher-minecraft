package com.application.launcher.rest.api;

import com.application.launcher.rest.request.AuthRequest;
import com.application.launcher.rest.response.TokenResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthApi {

    @POST("/api/auth/signing")
    Call<TokenResponse> auth(@Body AuthRequest authRequest);

}
