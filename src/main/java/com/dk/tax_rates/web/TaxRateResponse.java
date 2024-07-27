package com.dk.tax_rates.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxRateResponse {
  private String municipality;
  private Double taxRate;
}
