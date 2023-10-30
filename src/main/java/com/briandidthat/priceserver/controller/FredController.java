package com.briandidthat.priceserver.controller;

import com.briandidthat.priceserver.domain.fred.FredSeriesId;
import com.briandidthat.priceserver.domain.fred.Observation;
import com.briandidthat.priceserver.service.FredService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fred")
public class FredController {

    @Autowired
    private FredService service;

    @GetMapping("/mortgage-rate")
    private List<Observation> getMortgageRates(@RequestParam(required = false) Map<String, Object> params) {
        return service.getObservations(FredSeriesId.AVERAGE_MORTGAGE_RATE, params);
    }
}
