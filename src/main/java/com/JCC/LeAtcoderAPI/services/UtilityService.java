package com.JCC.LeAtcoderAPI.services;

import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class UtilityService {
    static void sleepInSeconds(int seconds) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(() -> {}, seconds, TimeUnit.SECONDS);
        executor.shutdown();
    }
}
