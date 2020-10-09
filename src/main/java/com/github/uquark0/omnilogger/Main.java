package com.github.uquark0.omnilogger;

import com.github.uquark0.omnilogger.log.ConsoleLogger;
import com.github.uquark0.omnilogger.log.OmniLogger;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main implements ModInitializer {
    public static final OmniLogger OMNI_LOGGER = new ConsoleLogger();
    public static final Logger LOGGER = LogManager.getLogger();


    @Override
    public void onInitialize() {

    }
}
