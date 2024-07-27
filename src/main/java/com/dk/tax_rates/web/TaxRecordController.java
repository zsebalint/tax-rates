package com.dk.tax_rates.web;

import com.dk.tax_rates.service.TaxRecordService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tax-rates")
@Slf4j
@Validated
public class TaxRecordController {

  private final TaxRecordService service;

  @Autowired
  public TaxRecordController(TaxRecordService service) {
    this.service = service;
  }

  @PostMapping()
  public String addTaxRecord(@Valid @RequestBody CreateTaxRecordRequest request) {
    if (request.getStartDate().isAfter(request.getEndDate())) {
      throw new IllegalArgumentException("Start date should not be later than end date");
    }
    var savedRecord = service.saveTaxRecord(request);
    return "Tax record saved with id: " + savedRecord.getId();
  }

  @GetMapping("/{municipality}")
  public TaxRateResponse getTaxRateByMunicipalityAndDate(
      @PathVariable String municipality, @RequestParam LocalDate date) {
    var taxRecord = service.getTaxRecordByDate(municipality, date);
    return new TaxRateResponse(taxRecord.getMunicipality(), taxRecord.getRate());
  }
}
