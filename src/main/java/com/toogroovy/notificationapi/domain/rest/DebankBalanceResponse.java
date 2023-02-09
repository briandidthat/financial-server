package com.toogroovy.notificationapi.domain.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public class DebankBalanceResponse {
    @JsonProperty("total_usd_value")
    private String totalUsdValue;
    @JsonProperty("chain_list")
    private List<Chain> chainList;

    public static class Chain {
        private String id;
        @JsonProperty("community_id")
        private Long communityId;
        private String name;
        @JsonProperty("native_token_id")
        private String nativeToken;
        @JsonProperty("logo_url")
        private String logoUrl;
        @JsonProperty("wrapped_token_id")
        private String wrappedTokenAddress;
        @JsonProperty("usd_value")
        private BigDecimal usdValue;
    }

    public DebankBalanceResponse() {}

    public DebankBalanceResponse(String totalUsdValue, List<Chain> chainList) {
        this.totalUsdValue = totalUsdValue;
        this.chainList = chainList;
    }

    public String getTotalUsdValue() {
        return totalUsdValue;
    }

    public void setTotalUsdValue(String totalUsdValue) {
        this.totalUsdValue = totalUsdValue;
    }

    public List<Chain> getChainList() {
        return chainList;
    }

    public void setChainList(List<Chain> chainList) {
        this.chainList = chainList;
    }

    @Override
    public String toString() {
        return "DebankBalanceResponse{" +
                "totalUsdValue='" + totalUsdValue + '}';
    }
}
