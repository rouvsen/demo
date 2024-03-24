package com.rouvsen.demo.scheduler;


import com.rouvsen.demo.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchedulerTransaction {

    private final CardService cardService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void dailyCashbackProcessing() {
        cardService.processCashbackForTransactions();
    }

}
