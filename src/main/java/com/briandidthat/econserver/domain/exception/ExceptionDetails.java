package com.briandidthat.econserver.domain.exception;

import java.time.LocalDateTime;

public record ExceptionDetails (LocalDateTime timestamp, String message, String details) {
}