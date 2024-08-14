package com.teama.simulator.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class AccessPoint {
    static final Logger logger = LogManager.getLogger();
    private static final boolean autoflush = true;
    private final int port;
    private final Protocol protocol;

    public AccessPoint(int port, Protocol protocol) {
        this.port = port;
        this.protocol = protocol;
    }

    public void startListen() {
        while(true) {
            this.protocol.resetSession();

            try {
                logger.info("Wacht op afnemer");
                this.connectAndProcess();
            } catch (BindException var2) {
                logger.error("Port niet beschikbaar");
                throw new RuntimeException(var2);
            } catch (IOException var3) {
                logger.error("Verbinding verbroken");
            } catch (RuntimeException var4) {
                logger.error("Probleem:" + var4.getMessage());
            }
        }
    }

    private void connectAndProcess() throws IOException {
        ServerSocket serverSocket = new ServerSocket(this.port);
        Throwable var2 = null;

        try {
            Socket clientSocket = serverSocket.accept();
            Throwable var4 = null;

            try {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                Throwable var6 = null;

                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    Throwable var8 = null;

                    try {
                        logger.info("Verbonden");

                        String inputLine;
                        while((inputLine = in.readLine()) != null) {
                            logger.info("Bericht ontvangen");
                            logger.debug("<< " + inputLine);
                            String outputLine = this.protocol.processMessage(inputLine);
                            out.println(outputLine);
                            logger.info("Bericht verstuurd");
                            logger.debug(">> " + outputLine);
                        }
                    } catch (Throwable var81) {
                        var8 = var81;
                        throw var81;
                    } finally {
                        if (in != null) {
                            if (var8 != null) {
                                try {
                                    in.close();
                                } catch (Throwable var80) {
                                    var8.addSuppressed(var80);
                                }
                            } else {
                                in.close();
                            }
                        }

                    }
                } catch (Throwable var83) {
                    var6 = var83;
                    throw var83;
                } finally {
                    if (out != null) {
                        if (var6 != null) {
                            try {
                                out.close();
                            } catch (Throwable var79) {
                                var6.addSuppressed(var79);
                            }
                        } else {
                            out.close();
                        }
                    }

                }
            } catch (Throwable var85) {
                var4 = var85;
                throw var85;
            } finally {
                if (clientSocket != null) {
                    if (var4 != null) {
                        try {
                            clientSocket.close();
                        } catch (Throwable var78) {
                            var4.addSuppressed(var78);
                        }
                    } else {
                        clientSocket.close();
                    }
                }

            }
        } catch (Throwable var87) {
            var2 = var87;
            throw var87;
        } finally {
            if (serverSocket != null) {
                if (var2 != null) {
                    try {
                        serverSocket.close();
                    } catch (Throwable var77) {
                        var2.addSuppressed(var77);
                    }
                } else {
                    serverSocket.close();
                }
            }

        }

    }
}