package com.briandidthat.priceserver.util;

import com.briandidthat.priceserver.domain.Token;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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

    public static HttpServletRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Assert.state(requestAttributes != null, "Could not find current request via RequestContextHolder");
        Assert.isInstanceOf(ServletRequestAttributes.class, requestAttributes);
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }
}
