package com.briandidthat.priceserver.domain.exception;

import java.time.LocalDateTime;

public record ExceptionDetails (LocalDateTime timestamp, String message, String details) {
}