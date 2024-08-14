package com.teama.simulator.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GpioAdapterFactory {

    private static final Logger logger = LogManager.getLogger();

    private GpioAdapterFactory() {
    }

    public static GpioAdapter createAdapter() {
        logger.info("Creating FX adapter.");
        return new Adapter();
    }
}
