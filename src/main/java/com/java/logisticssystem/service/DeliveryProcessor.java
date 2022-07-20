package com.java.logisticssystem.service;

import com.java.logisticssystem.ApplicationGlobalData;
import com.java.logisticssystem.model.Delivery;
import com.java.logisticssystem.model.Destination;
import com.java.logisticssystem.model.OrderStatus;
import com.java.logisticssystem.repository.DeliveryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.java.logisticssystem.model.OrderStatus.DELIVERED;
import static com.java.logisticssystem.model.OrderStatus.DELIVERING;

@Component
public class DeliveryProcessor
{
    private final DeliveryRepository deliveryRepository;

    private final Logger logger = LoggerFactory.getLogger(DeliveryProcessor.class);

    public DeliveryProcessor(DeliveryRepository deliveryRepository)
    {
        this.deliveryRepository = deliveryRepository;
    }

    @Async
    public void submitNewTask(Destination destination, List<Delivery> deliveries)
    {
        int deliveringCount = deliveryRepository.changeDeliveriesStatus(deliveries, DELIVERING);
        logger.info("Starting " + deliveringCount + " deliveries for " + destination.getName() + " at distance " + destination.getDistance() + " on thread " + Thread.currentThread().getName());

        try
        {
            Thread.sleep(destination.getDistance() * 200);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        int deliveredCount = deliveryRepository.changeDeliveriesStatus(deliveries, DELIVERED);
        ApplicationGlobalData.companyProfit.addAndGet(deliveredCount);
        logger.info("Finished " + deliveredCount + " deliveries for " + destination.getName() + " at distance " + destination.getDistance() + " on thread " + Thread.currentThread().getName());
    }

}
