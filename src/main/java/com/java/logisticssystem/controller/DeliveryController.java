package com.java.logisticssystem.controller;

import com.java.logisticssystem.dto.DeliveryDto;
import com.java.logisticssystem.dto.DestinationDto;
import com.java.logisticssystem.exception.InvalidDeliveryPayloadException;
import com.java.logisticssystem.service.DeliveryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/delivery")
public class DeliveryController
{
    private final DeliveryService deliveryservice;

    public DeliveryController(DeliveryService deliveryservice)
    {
        this.deliveryservice = deliveryservice;
    }

    @PostMapping("/add")
    public ResponseEntity<List<DeliveryDto>> handleDeliveryAdd(@Valid @RequestBody List<DeliveryDto> deliveryDtoList) throws InvalidDeliveryPayloadException, ParseException
    {
        return deliveryservice.addDeliveries(deliveryDtoList);
    }

    @GetMapping("/status")
    public ResponseEntity<List<DeliveryDto>> handleGetDeliveries(@RequestParam(required = false) @Pattern(regexp = "^\\d{2}-\\d{2}-\\d{4}$") String date,
                                                                 @RequestParam(defaultValue = "") String destination) throws ParseException
    {
        return deliveryservice.findDeliveriesByDateAndDestination(date, destination);
    }

    @PostMapping("/new-day")
    public Map<DestinationDto, List<DeliveryDto>> handleNewDay() {
        return deliveryservice.newDay();
    }

    @PostMapping("/cancel")
    public int handleCancelOrders(@RequestBody List<Long> ids) {
        return deliveryservice.cancelOrders(ids);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ InvalidDeliveryPayloadException.class , ConstraintViolationException.class})
    public String handleInvalidDeliveryPayloadException(Exception ex) {
        return ex.getMessage();
    }
}
