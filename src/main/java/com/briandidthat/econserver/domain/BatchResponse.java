package com.briandidthat.econserver.domain;

import com.briandidthat.econserver.domain.AssetPrice;

import java.util.List;

public record BatchResponse(List<AssetPrice> assetPrices) {

    @Override
    public String toString() {
        return "BatchResponse{" +
                "assetPrices=" + assetPrices +
                '}';
    }
}
