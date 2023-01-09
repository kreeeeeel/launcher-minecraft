package com.application.launcher.rest.api;

import com.application.launcher.rest.response.ClientResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.ArrayList;

public interface LauncherApi {

    @GET("/api/launcher/info/{name}")
    Call<ClientResponse> getLauncher(@Header("Authorization") String token, @Path("name") String launcher);

    @GET("/api/launcher/info/settings/{name}")
    Call<ArrayList<String>> getLauncherSettings(@Header("Authorization") String token, @Path("name") String launcher, @Query("connect") boolean connect, @Query("fullscreen") boolean fullscreen);

    @GET("/api/launcher")
    @Streaming
    Call<ResponseBody> download(@Header("Authorization") String token, @Query("path") String launcher);

}
