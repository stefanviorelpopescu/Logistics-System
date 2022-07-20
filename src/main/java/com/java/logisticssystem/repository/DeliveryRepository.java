package com.java.logisticssystem.repository;

import com.java.logisticssystem.model.Delivery;
import com.java.logisticssystem.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long>
{
    List<Delivery> findAllByDeliveryDateAndDestination_NameContainingIgnoreCase(long deliveryDate, String destination);
    List<Delivery> findAllByDeliveryDate(long deliveryDate);

    default int changeDeliveriesStatus(List<Delivery> deliveries, OrderStatus newStatus) {
        int changedDeliveriesCount = 0;
        for (Delivery delivery : deliveries)
        {
            if (changeDeliveryStatus(delivery, newStatus))
            {
                changedDeliveriesCount++;
            }
        }
        return changedDeliveriesCount;
    }

    default boolean changeDeliveryStatus(Delivery delivery, OrderStatus newStatus)
    {
        boolean shouldChangeStatus = true;
        switch (newStatus) {
            case NEW:
                break;
            case DELIVERING:
            case DELIVERED:
                if (delivery.getStatus().equals(OrderStatus.CANCELED)){
                    shouldChangeStatus = false;
                }
                break;
            case CANCELED:
                if (delivery.getStatus().equals(OrderStatus.DELIVERED)) {
                    shouldChangeStatus = false;
                }
                break;
        }
        if (shouldChangeStatus) {
            delivery.setStatus(newStatus);
            save(delivery);
        }
        return shouldChangeStatus;
    }
}
