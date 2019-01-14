package org.ollide.fussifinder.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MatchClient {

    @GET("ajax.match.calendar/-/datum-bis/{dateTo}/datum-von/{dateFrom}/plz/{zip}/mannschaftsart/{teamType}")
    Call<String> matchCalendar(@Path("dateFrom") String dateFrom, @Path("dateTo") String dateTo,
                                     @Path("zip") String zip, @Path("teamType") String teamType);

    @GET("spiel/-/spiel/{id}")
    Call<String> matchDetails(@Path("id") String id);

}
