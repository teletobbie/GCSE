package com.teama.simulator;

import com.teama.simulator.io.GpioAdapter;
import com.teama.simulator.io.GpioAdapterFactory;
import com.teama.simulator.io.GpioEventListener;
import com.teama.simulator.network.AccessPoint;
import com.teama.simulator.network.Protocol;

import com.teama.simulator.heater.Simulator;
import com.teama.simulator.heater.TimeProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class App {
    private static final Logger logger = LogManager.getLogger(App.class.getName());
    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

    public App() {
    }

    public static void main(String[] args) throws IOException {
        logger.debug("Server starting");
        int secret = 0;
        int port = 7777;
        TimeProvider timeProvider = TimeProvider.FAST_TIME;
        logger.info("Server port:" + port);
        logger.info("Server secret:" + secret);
        logger.info("Time: " + timeProvider);
        GpioAdapter adapter = GpioAdapterFactory.createAdapter();
        ApplicationEvents eventListener = new GpioEventListener(adapter);
        eventListener.disconnected();
        SharedState state = new SharedState();
        Simulator cv = new Simulator(state, timeProvider, eventListener);
        Protocol protocol = new Protocol(String.valueOf(secret), state, timeProvider, eventListener);
        AccessPoint accesspoint = new AccessPoint(port, protocol);
        executorService.scheduleAtFixedRate(() -> {
            cv.iterate();
        }, 0L, 100L, TimeUnit.MILLISECONDS);
        executorService.submit(() -> {
            accesspoint.startListen();
        });
        logger.info("Ready.");
    }

    private static int determineSecret(String[] args) {
        return args != null && args.length >= 1 ? Integer.parseInt(args[0]) : ThreadLocalRandom.current().nextInt(100000, 999999);
    }
}
