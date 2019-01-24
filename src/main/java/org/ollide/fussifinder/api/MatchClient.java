package org.ollide.fussifinder.api;

import org.ollide.fussifinder.model.AjaxModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MatchClient {

    String LOAD_MORE = "ajax.match.calendar.loadmore";

    @GET("ajax.match.calendar/-/datum-bis/{dateTo}/datum-von/{dateFrom}/plz/{zip}/mannschaftsart/{teamType}")
    Call<String> matchCalendar(@Path("dateFrom") String dateFrom, @Path("dateTo") String dateTo,
                               @Path("zip") String zip, @Path("teamType") String teamType);

    @GET(LOAD_MORE + "/-/datum-bis/{dateTo}/datum-von/{dateFrom}/mime-type/JSON/plz/{zip}/mannschaftsart/"
            + "{teamType}/max/10/offset/{offset}")
    Call<AjaxModel> loadMoreMatches(@Path("dateFrom") String dateFrom, @Path("dateTo") String dateTo,
                                    @Path("zip") String zip, @Path("teamType") String teamType,
                                    @Path("offset") int offset);

    @GET("spiel/-/spiel/{id}")
    Call<String> matchDetails(@Path("id") String id);

}
