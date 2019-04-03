package org.ollide.fussifinder.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AjaxModel {

    private String html;
    private int lastIndex;
    private boolean success;
    @JsonProperty("final")
    private boolean finalResponse;

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public int getLastIndex() {
        return lastIndex;
    }

    public void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isFinalResponse() {
        return finalResponse;
    }

    public void setFinalResponse(boolean finalResponse) {
        this.finalResponse = finalResponse;
    }
}
