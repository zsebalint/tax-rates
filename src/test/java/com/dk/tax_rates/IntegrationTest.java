package com.dk.tax_rates;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dk.tax_rates.web.CreateTaxRecordRequest;
import com.dk.tax_rates.web.TaxRateResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class IntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper mapper;

  @Test
  public void shouldSaveValidTaxRecord() throws Exception {
    var request =
        CreateTaxRecordRequest.builder()
            .municipality("Copenhagen")
            .rate(0.2)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(1))
            .build();

    mockMvc
        .perform(
            post("/api/tax-rates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
        .andExpect(status().isOk());
  }

  @Test
  public void shouldNotSaveValidTaxRecordForTheSameSchedulingTwice() throws Exception {
    var request1 =
        CreateTaxRecordRequest.builder()
            .municipality("Copenhagen")
            .rate(0.2)
            .startDate(LocalDate.now().plusDays(1))
            .endDate(LocalDate.now().plusDays(2))
            .build();

    mockMvc
        .perform(
            post("/api/tax-rates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request1)))
        .andExpect(status().isOk());

    var request2 =
        CreateTaxRecordRequest.builder()
            .municipality("Copenhagen")
            .rate(0.3)
            .startDate(LocalDate.now().plusDays(1))
            .endDate(LocalDate.now().plusDays(2))
            .build();

    mockMvc
        .perform(
            post("/api/tax-rates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request2)))
        .andExpect(status().isBadRequest())
        .andExpect(
            content()
                .json(
                    "{\"title\":\"Validation Error\",\"status\":400,\"detail\":\"Tax record with the given municipality and dates already exist\"}"));
  }

  @Test
  public void shouldNotSaveTaxRecordWithoutMunicipality() throws Exception {
    var request =
        CreateTaxRecordRequest.builder()
            .municipality("")
            .rate(0.2)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(1))
            .build();

    mockMvc
        .perform(
            post("/api/tax-rates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(
            content()
                .json(
                    "{\"title\":\"Validation Error\",\"status\":400,\"detail\":\"Input validation failed\",\"violations\":[{\"fieldName\":\"municipality\",\"message\":\"Municipality should be not empty\"}]}"));
  }

  @Test
  public void shouldGetTheCorrectTaxRate() throws Exception {
    var request1 =
        CreateTaxRecordRequest.builder()
            .municipality("Copenhagen")
            .rate(0.2)
            .startDate(LocalDate.of(2024, 1, 1))
            .endDate(LocalDate.of(2024, 12, 31))
            .build();

    var request2 =
        CreateTaxRecordRequest.builder()
            .municipality("Copenhagen")
            .rate(0.1)
            .startDate(LocalDate.of(2024, 1, 1))
            .endDate(LocalDate.of(2024, 1, 1))
            .build();
    mockMvc
        .perform(
            post("/api/tax-rates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request1)))
        .andExpect(status().isOk());

    mockMvc
        .perform(
            post("/api/tax-rates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request2)))
        .andExpect(status().isOk());

    var response =
        mockMvc
            .perform(get("/api/tax-rates/Copenhagen?date=2024-01-01"))
            .andExpect(status().isOk())
            .andReturn();

    TaxRateResponse responseObject =
        mapper.readValue(response.getResponse().getContentAsByteArray(), TaxRateResponse.class);

    Assertions.assertEquals(0.1, responseObject.getTaxRate());
  }
}
