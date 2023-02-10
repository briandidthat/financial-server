package com.toogroovy.priceserver.domain;

import java.io.Serializable;

public record ApiResponse (String price, String symbol, String source) implements Serializable {
}
