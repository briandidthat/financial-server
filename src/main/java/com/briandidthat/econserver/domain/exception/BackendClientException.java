package com.briandidthat.econserver.domain.exception;

public class BackendClientException extends RuntimeException {
    public BackendClientException(String msg) {
        super(msg);
    }
}
