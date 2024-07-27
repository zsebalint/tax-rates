package com.dk.tax_rates.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTaxRecordRequest {
  @NotNull
  @NotBlank(message = "Municipality should be not empty")
  private String municipality;

  @NotNull private LocalDate startDate;

  @NotNull private LocalDate endDate;

  @NotNull
  @Positive(message = "Tax rate should be positive")
  private Double rate;
}
