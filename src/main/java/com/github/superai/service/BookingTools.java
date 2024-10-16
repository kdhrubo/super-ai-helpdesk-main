package com.github.superai.service;

import java.time.LocalDate;
import java.util.function.Function;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.superai.data.BookingStatus;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.core.NestedExceptionUtils;

@Configuration
@Slf4j
public class BookingTools {

    private static final Logger logger = LoggerFactory.getLogger(BookingTools.class);

    @Autowired
    private FlightBookingService flightBookingService;

    public record BookingDetailsRequest(String bookingNumber, String firstName, String lastName) {
    }

    public record ChangeBookingDatesRequest(String bookingNumber, String firstName, String lastName, String date,
            String from, String to) {
    }

    public record CancelBookingRequest(String bookingNumber, String firstName, String lastName) {
    }

    @JsonInclude(Include.NON_NULL)
    public record BookingDetails(String bookingNumber,
            String firstName,
            String lastName,
            LocalDate date,
            BookingStatus bookingStatus,
            String from,
            String to,
            String bookingClass) {
    }

    @Bean
    @Description("Get flight booking details")
    public Function<BookingDetailsRequest, BookingDetails> getBookingDetails() {
        return request -> {
            try {
                log.info("**** Get booking details request - {}", request);
                return flightBookingService.getBookingDetails(request.bookingNumber(), request.firstName(),
                        request.lastName());
            }

            catch (Exception e) {
                logger.info("Booking details: {}", NestedExceptionUtils.getMostSpecificCause(e).getMessage());
                /*
                return new BookingDetails(request.bookingNumber(), request.firstName(), request.lastName,
                        null, null, null, null, null);

                 */
                throw new RuntimeException(e);
            }
        };
    }

    @Bean
    @Description("Change flight booking dates")
    public Function<ChangeBookingDatesRequest, String> changeBooking() {
        return request -> {
            log.info("Get booking details request - {}", request);
            flightBookingService.changeBooking(request.bookingNumber(), request.firstName(), request.lastName(),
                    request.date(), request.from(), request.to());
            return "";
        };
    }

    @Bean
    @Description("Cancel flight booking")
    public Function<CancelBookingRequest, String> cancelBooking() {
        return request -> {
            log.info("****** Get booking details request - {}", request);
            flightBookingService.cancelBooking(request.bookingNumber(), request.firstName(), request.lastName());
            return "";
        };
    }
}
