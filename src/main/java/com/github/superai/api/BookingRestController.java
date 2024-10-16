package com.github.superai.api;


import com.github.superai.service.BookingTools;
import com.github.superai.service.FlightBookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/v1/api")
@Slf4j
@RequiredArgsConstructor
public class BookingRestController {

    private final FlightBookingService flightBookingService;


    @GetMapping("/bookings")
    public List<BookingTools.BookingDetails> getBookings() {


        return
                flightBookingService.getBookings();



    }

}