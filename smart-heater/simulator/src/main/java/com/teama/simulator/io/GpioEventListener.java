package com.teama.simulator.io;


import com.teama.simulator.ApplicationEvents;

public class GpioEventListener implements ApplicationEvents {
    private static GpioEventListener.GpioLock lock = new GpioEventListener.GpioLock();
    private final GpioAdapter adapter;

    public GpioEventListener(GpioAdapter adapter) {
        this.adapter = adapter;
    }

    public void connected() {
        synchronized(lock) {
            this.adapter.reset();
        }
    }

    public void disconnected() {
        synchronized(lock) {
            this.adapter.reset();
        }
    }

    public void problem() {
        synchronized(lock) {
            this.adapter.reset();
        }
    }

    private static class GpioLock {
        private GpioLock() {
        }
    }
}
