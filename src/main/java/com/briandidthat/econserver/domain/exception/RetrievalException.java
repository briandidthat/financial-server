package com.briandidthat.econserver.domain.exception;

public class RetrievalException extends RuntimeException {
    public RetrievalException(String msg) {
        super(msg);
    }
    public RetrievalException(Exception e) {
        super(e.getMessage());
    }
}
