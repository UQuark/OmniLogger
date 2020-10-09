package com.github.uquark0.omnilogger.log;

import com.github.uquark0.omnilogger.Server;

public class ConsoleLogger extends TextLogger {
    @Override
    public void print(String text) {
        Server.LOGGER.info(text);
    }
}
