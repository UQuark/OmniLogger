package com.github.uquark0.omnilogger.log;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

public class DBLogger implements OmniLogger {
    private final Connection connection;

    public DBLogger(Connection connection) {
        this.connection = connection;
    }

    private void putPlayer(PlayerEntity playerEntity) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO PLAYER (PLAYER_UUID, PLAYER_NAME) VALUES ( ?, ? )");
        statement.setObject(1, playerEntity.getUuid());
        statement.setObject(2, playerEntity.getEntityName());
        statement.execute();
        statement.close();
    }

    private int getPlayerId(PlayerEntity playerEntity) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT ID FROM PLAYER WHERE PLAYER_UUID = ?");
        statement.setObject(1, playerEntity.getUuid());
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            int id = resultSet.getInt(1);
            resultSet.close();
            statement.close();
            return id;
        } else {
            resultSet.close();
            statement.close();
            return -1;
        }
    }

    private int ensurePlayerId(PlayerEntity playerEntity) {
        try {
            int id = getPlayerId(playerEntity);
            if (id == -1) {
                putPlayer(playerEntity);
                return getPlayerId(playerEntity);
            } else
                return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void putWorld(RegistryKey<World> world) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO WORLD (WORLD_NAME) VALUES ( ? )");
        statement.setObject(1, world.getValue().toString());
        statement.execute();
        statement.close();
    }

    private int getWorldId(RegistryKey<World> world) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT ID FROM WORLD WHERE WORLD_NAME = ?");
        statement.setObject(1, world.getValue().toString());
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            int id = resultSet.getInt(1);
            resultSet.close();
            statement.close();
            return id;
        } else {
            resultSet.close();
            statement.close();
            return -1;
        }
    }

    private int ensureWorldId(RegistryKey<World> world) {
        try {
            int id = getWorldId(world);
            if (id == -1) {
                putWorld(world);
                return getWorldId(world);
            } else
                return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int getBlockId(Block block) throws SQLException {
        int blockId = Registry.BLOCK.getRawId(block);
        PreparedStatement statement = connection.prepareStatement("SELECT ID FROM BLOCK WHERE ID = ?");
        statement.setInt(1, blockId);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            resultSet.close();
            statement.close();
            return blockId;
        } else {
            resultSet.close();
            statement.close();
            return -1;
        }
    }

    private void putBlock(Block block) throws SQLException {
        int blockId = Registry.BLOCK.getRawId(block);
        String blockName = Registry.BLOCK.getId(block).toString();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO BLOCK (ID, NAME) VALUES ( ?, ? )");
        statement.setObject(1, blockId);
        statement.setObject(2, blockName);
        statement.execute();
        statement.close();
    }

    private int ensureBlockId(Block block) {
        try {
            int id = getBlockId(block);
            if (id == -1) {
                putBlock(block);
                return getBlockId(block);
            } else
                return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void putIntoDB(int playerId, int actionId, int blockX, int blockY, int blockZ, LocalTime actionTime, int blockId, int worldId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO LOG (PLAYER_ID, ACTION_ID, BLOCK_X, BLOCK_Y, BLOCK_Z, ACTION_TIME, BLOCK_ID, WORLD_ID) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? )");
        statement.setInt(1, playerId);
        statement.setInt(2, actionId);
        statement.setInt(3, blockX);
        statement.setInt(4, blockY);
        statement.setInt(5, blockZ);
        statement.setObject(6, actionTime);
        statement.setInt(7, blockId);
        statement.setInt(8, worldId);
        statement.execute();
        statement.close();
    }

    @Override
    public void log(ActionInfo info) {
        int playerId = ensurePlayerId(info.player);
        int worldId = ensureWorldId(info.world);
        int blockId = ensureBlockId(info.block);
        try {
            putIntoDB(
                    playerId,
                    info.action.id,
                    info.pos.getX(),
                    info.pos.getY(),
                    info.pos.getZ(),
                    info.time,
                    blockId,
                    worldId
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
