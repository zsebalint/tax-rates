package com.dk.tax_rates.db;

import com.dk.tax_rates.model.TaxRecord;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxRecordRepository extends JpaRepository<TaxRecord, Long> {

  @Query(
      "select t from TaxRecord t where t.municipality = ?1 and t.validFrom<= ?2 and t.validTo>= ?2")
  List<TaxRecord> findAllByMunicipalityAndValidityDate(String municipality, LocalDate date);
}
