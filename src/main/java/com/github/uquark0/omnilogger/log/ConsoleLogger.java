package com.github.uquark0.omnilogger.log;

import com.github.uquark0.omnilogger.Main;

public class ConsoleLogger extends TextLogger {
    @Override
    public void print(String text) {
        Main.LOGGER.info(text);
    }
}
