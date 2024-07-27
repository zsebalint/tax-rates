package com.dk.tax_rates.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Data;

@Data
@Entity
@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = {"municipality", "validFrom", "validTo"}))
public class TaxRecord {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  private String municipality;
  private LocalDate validFrom;
  private LocalDate validTo;
  private Double rate;
}
