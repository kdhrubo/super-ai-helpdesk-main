package com.github.superai.marketing;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record GenerateCalendarRequest(String articles,
                                      @DateTimeFormat(pattern = "MM/dd/yyyy")
                                      LocalDate startDate) {
}
