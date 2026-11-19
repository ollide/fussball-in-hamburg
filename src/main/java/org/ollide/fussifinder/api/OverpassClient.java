package org.ollide.fussifinder.api;

import org.jspecify.annotations.NonNull;
import org.ollide.fussifinder.model.overpass.OverpassResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.PostExchange;

public interface OverpassClient {

    @PostExchange(url = "api/interpreter", contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ResponseEntity<@NonNull OverpassResponse> query(@RequestParam("data") String data);

}
