package com.atlas.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.atlas.database.dao.AtlasGatewayEntityDao;
import com.atlas.model.AtlasGatewayEntity;

@Database(entities = AtlasGatewayEntity.class, exportSchema = false, version = 1)
public abstract class AtlasDatabase extends RoomDatabase {
    private static final String DB_NAME = "ATLAS_DATABASE";
    private static AtlasDatabase instance;

    public static synchronized AtlasDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AtlasDatabase.class,
                    DB_NAME)
            .fallbackToDestructiveMigration()
            .build();
        }
        return instance;
    }

    public abstract AtlasGatewayEntityDao gatewayEntityDao();
}
