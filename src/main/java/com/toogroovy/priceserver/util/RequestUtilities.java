package com.toogroovy.priceserver.util;

import com.toogroovy.priceserver.domain.Token;

import java.util.List;

public final class RequestUtilities {
    private RequestUtilities() {}
    public static boolean validateSymbol(String symbol, List<Token> tokens) {
        for (Token token : tokens) {
            if (symbol.equals(token.code())) {
                return true;
            }
        }
        return false;
    }

    public static boolean validateSymbols(List<String> symbols, List<Token> tokens) {
        for (String symbol : symbols) {
            if (!validateSymbol(symbol, tokens)) {
                return false;
            }
        }
        return true;
    }
}
