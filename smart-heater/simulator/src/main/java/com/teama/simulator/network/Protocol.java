package com.teama.simulator.network;


import com.teama.simulator.ApplicationEvents;
import com.teama.simulator.SharedState;
import com.teama.simulator.heater.TimeProvider;

import com.teama.simulator.heater.Action;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;

public class Protocol {
    static final Logger logger = LogManager.getLogger();
    private final String secret;
    private final SharedState state;
    private final TimeProvider timeProvider;
    private final ApplicationEvents eventListener;
    private boolean sessionConnected = false;

    public Protocol(String secret, SharedState state, TimeProvider timeProvider, ApplicationEvents eventListener) {
        this.secret = secret;
        this.state = state;
        this.timeProvider = timeProvider;
        this.eventListener = eventListener;
    }

    public String processMessage(String in) {
        MessageType type = Protocol.MessageType.fromString(in);
        type.valideerBericht(in, this.sessionConnected);
        switch (type) {
            case INIT:
                return this.valideerSecret(in);
            case STAT:
                return this.bepaalHuidigeStat();
            case ACTION:
                return this.verwerkActuator(in);
            default:
                throw new IllegalStateException("WTF");
        }
    }

    private String verwerkActuator(String in) {
        String[] parts = Protocol.MessageType.parts(in);
        this.state.put(new Action(parts[2], parts[3]));
        return "#ACT-OK";
    }

    private String bepaalHuidigeStat() {
        return String.valueOf(this.state.geefStatus());
    }

    private String valideerSecret(String in) {
        if (in.equals("$CV-CONNECT-$-"+this.secret)) {
            this.sessionConnected = true;
            logger.info("Session started");
            this.eventListener.connected();
            return "#CONNECT-OK#" + this.timeProvider.currentInstant().getEpochSecond() + "#" + Instant.now().getEpochSecond();
        } else {
            logger.error("Session failed");
            throw new IllegalStateException("incorrect secret");
        }
    }

    public void resetSession() {
        logger.info("Reset session");
        this.sessionConnected = false;
        this.eventListener.disconnected();
    }

    static enum MessageType {
        INIT {
            public void valideerBericht(String in, boolean sessionConnected) {
                if (!in.startsWith("$CV-CONNECT-$-")) {
                    logger.error("Invalide Session msg");
                    throw new IllegalStateException("message is invalide");
                } else if (sessionConnected) {
                    logger.error("Connect ontvangen, maar niet verwacht");
                    throw new IllegalStateException("Er is al een sessie dus er kan niet nog een sessie starten");
                }
            }
        },
        ACTION {
            public void valideerBericht(String in, boolean sessionConnected) {
                String[] parts = parts(in);
                if (in.startsWith("$CV-ACT-$") && parts.length == 4) {
                    if (!sessionConnected) {
                        logger.error("Action ontvangen, maar niet verwacht");
                        throw new IllegalStateException("Er is nog geen sessie dus er mag geen actie gegeven worden");
                    }
                } else {
                    logger.error("Invalide Act msg");
                    throw new IllegalStateException("message is invalide");
                }
            }
        },
        STAT {
            public void valideerBericht(String in, boolean sessionConnected) {
                if (!in.equals("$CV-STAT?")) {
                    logger.error("Invalide Stat msg");
                    throw new IllegalStateException("message is invalide");
                } else if (!sessionConnected) {
                    logger.error("Status ontvangen, maar niet verwacht");
                    throw new IllegalStateException("Er is nog geen sessie dus er mag geen status opgevraagd worden");
                }
            }
        };

        private MessageType() {
        }

        public abstract void valideerBericht(String var1, boolean var2);

        static String[] parts(String msg) {
            return msg.split("\\$");
        }

        static MessageType fromString(String in) {
            if (in.contains("ACT")) {
                return ACTION;
            } else if (in.contains("CONNECT")) {
                return INIT;
            } else if (in.contains("STAT")) {
                return STAT;
            } else {
                throw new IllegalArgumentException("Unsupported msg :" + in);
            }
        }
    }
}
