package com.java.logisticssystem.configuration;

import com.java.logisticssystem.ApplicationGlobalData;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class ActuatorConfig implements InfoContributor
{
    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("Current Date", ApplicationGlobalData.currentDate);
        builder.withDetail("Company Profit", ApplicationGlobalData.companyProfit.get());
    }
}
