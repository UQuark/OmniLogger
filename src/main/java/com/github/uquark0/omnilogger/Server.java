package com.github.uquark0.omnilogger;

import com.github.uquark0.omnilogger.log.DBLogger;
import com.github.uquark0.omnilogger.log.OmniLogger;
import me.uquark.quarkcore.data.DatabaseProvider;
import net.fabricmc.api.DedicatedServerModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class Server implements DedicatedServerModInitializer {
    public static OmniLogger OMNI_LOGGER;
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitializeServer() {
        try {
            Connection connection = DatabaseProvider.getConnection("OmniLogger", "storage", "", "");
            OMNI_LOGGER = new DBLogger(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
