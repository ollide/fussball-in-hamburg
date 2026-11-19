package org.ollide.fussifinder.api;

import org.ollide.fussifinder.model.AjaxModel;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface MatchClient {

    String LOAD_MORE = "ajax.match.calendar.loadmore";

    @GetExchange("ajax.match.calendar/-/datum-bis/{dateTo}/datum-von/{dateFrom}/plz/{zip}/mannschaftsart/{teamType}")
    String matchCalendar(@PathVariable("dateFrom") String dateFrom, @PathVariable("dateTo") String dateTo,
                               @PathVariable("zip") String zip, @PathVariable("teamType") String teamType);

    @GetExchange(LOAD_MORE + "/-/datum-bis/{dateTo}/datum-von/{dateFrom}/mime-type/JSON/plz/{zip}/mannschaftsart/"
            + "{teamType}/max/10/offset/{offset}")
    AjaxModel loadMoreMatches(@PathVariable("dateFrom") String dateFrom, @PathVariable("dateTo") String dateTo,
                                    @PathVariable("zip") String zip, @PathVariable("teamType") String teamType,
                                    @PathVariable("offset") int offset);

    @GetExchange("spiel/-/spiel/{id}")
    String matchDetails(@PathVariable("id") String id);

}
