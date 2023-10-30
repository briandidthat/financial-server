package com.briandidthat.financialserver.domain.exception;

public class BackendClientException extends RuntimeException {
    public BackendClientException(String msg) {
        super(msg);
    }
}
