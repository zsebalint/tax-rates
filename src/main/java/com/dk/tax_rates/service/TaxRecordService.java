package com.dk.tax_rates.service;

import com.dk.tax_rates.db.TaxRecordRepository;
import com.dk.tax_rates.model.TaxRecord;
import com.dk.tax_rates.web.CreateTaxRecordRequest;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaxRecordService {

  private final TaxRecordRepository repository;

  @Autowired
  public TaxRecordService(TaxRecordRepository repository) {
    this.repository = repository;
  }

  public TaxRecord saveTaxRecord(CreateTaxRecordRequest request) {
    var taxRecord = new TaxRecord();
    taxRecord.setRate(request.getRate());
    taxRecord.setMunicipality(request.getMunicipality());
    taxRecord.setValidTo(request.getEndDate());
    taxRecord.setValidFrom(request.getStartDate());
    return repository.save(taxRecord);
  }

  public TaxRecord getTaxRecordByDate(String municipality, LocalDate date) {
    var records = repository.findAllByMunicipalityAndValidityDate(municipality, date);
    records.sort(Comparator.comparing(this::taxRecordValidityDuration));
    return records.getFirst();
  }

  private long taxRecordValidityDuration(TaxRecord taxRecord) {
    return ChronoUnit.DAYS.between(taxRecord.getValidFrom(), taxRecord.getValidTo());
  }
}
