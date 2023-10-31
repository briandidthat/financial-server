package com.briandidthat.financialserver.controller;

import com.briandidthat.financialserver.domain.fred.FredResponse;
import com.briandidthat.financialserver.domain.fred.FredSeriesId;
import com.briandidthat.financialserver.service.FredService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("/fred")
public class FredController {

    @Autowired
    private FredService service;

    @GetMapping("/observations/{operation}")
    public FredResponse getObservations(@PathVariable String operation, @RequestParam(required = false) LinkedHashMap<String, Object> params) {
        final String seriesId = FredSeriesId.getSeriesId(operation);
        return service.getObservations(seriesId, params);
    }
}
