package com.example.finalproject.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Cases implements Serializable {
    @SerializedName("currCases")
    private String currCases;
    @SerializedName("recCases")
    private String recCases;
    @SerializedName("deaths")
    private String deaths;

    public Cases() {
    }

    public Cases(String currCases, String recCases, String deaths) {
        this.currCases = currCases;
        this.recCases = recCases;
        this.deaths = deaths;
    }

    public String getCurrCases() {
        return currCases;
    }

    public String getRecCases() {
        return recCases;
    }

    public String getDeaths() {
        return deaths;
    }
}