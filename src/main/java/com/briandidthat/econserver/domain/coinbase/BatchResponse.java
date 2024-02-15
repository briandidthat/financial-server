package com.briandidthat.econserver.domain.coinbase;

import java.util.List;

public record BatchResponse(List<SpotPrice> spotPrices) {
    @Override
    public String toString() {
        return "BatchResponse{" + "spotPrices=" + spotPrices + '}';
    }
}
