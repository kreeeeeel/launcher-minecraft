package com.application.launcher.rest.api;

import com.application.launcher.rest.response.ProfileResponse;
import com.application.launcher.rest.response.TextureResponse;
import com.application.launcher.rest.response.TextureType;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface ProfileApi {

    @GET("/api/profile")
    Call<ProfileResponse> getProfile(@Header("Authorization") String token);

    @Multipart
    @POST("/api/profile/texture/{type}")
    Call<TextureResponse> uploadTexture(@Header("Authorization") String token, @Part MultipartBody.Part file, @Path("type") TextureType textureType);

    @DELETE("/api/profile/cape")
    Call<ResponseBody> deleteCape(@Header("Authorization") String token);

}
