package com.briandidthat.financialserver.controller;

import com.briandidthat.financialserver.domain.fred.FredSeriesId;
import com.briandidthat.financialserver.domain.fred.Observation;
import com.briandidthat.financialserver.service.FredService;
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

    @GetMapping("/treasury-yield-10yr")
    private List<Observation> getTenYearTreasuryYield(@RequestParam(required = false) Map<String, Object> params) {
        return service.getObservations(FredSeriesId.TEN_YEAR_TREASURY_YIELD, params);
    }
}
