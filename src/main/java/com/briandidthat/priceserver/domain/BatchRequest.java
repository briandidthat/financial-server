package com.briandidthat.priceserver.domain;

import javax.validation.constraints.Size;
import java.util.List;

public record BatchRequest (@Size(min = 2, max = 5) List<String> symbols){
}
