package com.briandidthat.priceserver.domain.exception;

public class BackendClientException extends RuntimeException {
    public BackendClientException(String msg) {
        super(msg);
    }
}
