package com.revi1337.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component @Slf4j
public class NVDScheduledTask {

//    @Scheduled(cron = "*/5 * * * * *")
    public void requestNVCRequest() {
        log.info("requestNVCRequest Executed");
    }

}
