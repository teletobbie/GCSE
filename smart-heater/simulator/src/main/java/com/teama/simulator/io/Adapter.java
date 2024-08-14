package com.teama.simulator.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Adapter implements GpioAdapter{
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void reset() {
        logger.debug("Reset heater");
    }

}
