package org.ollide.fussifinder.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ollide.fussifinder.ResourceHelper;
import org.ollide.fussifinder.api.MatchClient;
import org.ollide.fussifinder.model.AjaxModel;
import retrofit2.mock.Calls;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class MatchClientCrawlServiceTest {

    private static final String TWO_MATCHES_ZIP = "12345";
    private static final String TWO_MATCHES_LOAD_MORE_ZIP = "45678";

    private MatchClientCrawlService matchClientCrawlService;
    private MatchClient matchClient;

    private String response2Matches;
    private String response2MatchesLoadMore;
    private String responseLoadMore;

    @BeforeEach
    void setUp() throws Exception {
        matchClient = mock(MatchClient.class);

        // 2 Matches
        response2Matches = ResourceHelper.readMatches("2_matches_2_days.html");
        when(matchClient.matchCalendar(anyString(), anyString(), eq(TWO_MATCHES_ZIP), anyString()))
                .thenReturn(Calls.response(response2Matches));

        // 2 Matches load more
        response2MatchesLoadMore = ResourceHelper.readMatches("2_matches_2_days_load_more.html");
        when(matchClient.matchCalendar(anyString(), anyString(), eq(TWO_MATCHES_LOAD_MORE_ZIP), anyString()))
                .thenReturn(Calls.response(response2MatchesLoadMore));


        responseLoadMore = ResourceHelper.readMatches("load_more.html");

        AjaxModel ajaxModel = new AjaxModel();
        ajaxModel.setLastIndex(19);
        ajaxModel.setSuccess(true);
        ajaxModel.setFinalResponse(true);
        ajaxModel.setHtml(responseLoadMore);
        when(matchClient.loadMoreMatches(anyString(), anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(Calls.response(ajaxModel));

        matchClientCrawlService = new MatchClientCrawlService(matchClient);
    }

    @Test
    void testGetMatchCalendar() {
        String matchCalendar = matchClientCrawlService.getMatchCalendar("", "", TWO_MATCHES_ZIP);
        verify(matchClient, never()).loadMoreMatches(anyString(), anyString(), anyString(), anyString(), anyInt());
        assertEquals(response2Matches, matchCalendar);
    }

    @Test
    void testGetMatchCalendarLoadMore() {
        String matchCalendar = matchClientCrawlService.getMatchCalendar("", "", TWO_MATCHES_LOAD_MORE_ZIP);
        verify(matchClient, times(1))
                .loadMoreMatches(anyString(), anyString(), eq(TWO_MATCHES_LOAD_MORE_ZIP), anyString(), anyInt());

        // Response contains both HTTP responses -> length == combined length
        assertEquals(response2MatchesLoadMore.length() + responseLoadMore.length(), matchCalendar.length());
    }
}
