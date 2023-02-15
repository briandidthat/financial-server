package com.toogroovy.priceserver.domain.exception;

import java.util.Date;

public record ExceptionDetails (Date timestamp, String message, String details) {
}