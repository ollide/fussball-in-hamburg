package org.ollide.fussifinder.api;

import org.ollide.fussifinder.model.overpass.OverpassResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface OverpassClient {

    @FormUrlEncoded
    @POST("api/interpreter")
    Call<OverpassResponse> query(@Field("data") String data);

}
