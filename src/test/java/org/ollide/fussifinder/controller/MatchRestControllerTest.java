package org.ollide.fussifinder.controller;

import org.junit.jupiter.api.Test;
import org.ollide.fussifinder.model.Period;
import org.ollide.fussifinder.model.Region;
import org.ollide.fussifinder.model.RegionType;
import org.ollide.fussifinder.service.MatchDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MatchRestController.class)
class MatchRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MatchDayService service;

    @Test
    void getMatchDays_DefaultRequest() throws Exception {
        when(service.getMatchDays(any(Region.class), any(Period.class))).thenReturn(Collections.emptyList());
        this.mockMvc.perform(get("/api/matches")).andExpect(status().isOk());

        verify(service, times(1)).getMatchDays(new Region(RegionType.CITY, "Hamburg"), new Period());
    }

    @Test
    void getMatchDays_InvalidName() throws Exception {
        this.mockMvc.perform(get("/api/matches").param("name", "multi\nline")).andExpect(status().isOk());
        verify(service, never()).getMatchDays(any(Region.class), any(Period.class));
    }
}
