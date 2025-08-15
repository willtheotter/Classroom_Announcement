package com.example.classroomannouncement.Database;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.content.Context;

import com.example.classroomannouncement.Database.Daos.AnnouncementDao;
import com.example.classroomannouncement.Database.Daos.AnalyticsDao;
import com.example.classroomannouncement.Database.Daos.SystemSettingsDao;
import com.example.classroomannouncement.Database.Daos.UserDao;
import com.example.classroomannouncement.Database.Entities.Announcement;
import com.example.classroomannouncement.Database.Entities.Analytics;
import com.example.classroomannouncement.Database.Entities.SystemSettings;
import com.example.classroomannouncement.Database.Entities.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {Announcement.class, User.class, Analytics.class, SystemSettings.class},
        version = 7,  // Updated version for schema fixes
        exportSchema = true
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract AnnouncementDao announcementDao();
    public abstract UserDao userDao();
    public abstract AnalyticsDao analyticsDao();
    public abstract SystemSettingsDao systemSettingsDao();

    // Default admin credentials
    private static final String DEFAULT_ADMIN_EMAIL = "admin@school.edu";
    private static final String DEFAULT_ADMIN_NAME = "System Administrator";
    private static final String DEFAULT_ADMIN_PASSWORD = "Admin@123";

    // Default system settings
    private static final int DEFAULT_ANNOUNCEMENT_DURATION = 7; // days
    private static final int DEFAULT_MAX_USERS = 100;

    private static volatile AppDatabase INSTANCE;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);

    // Migration from version 1 to 2 (add admin user)
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("INSERT OR IGNORE INTO users (name, email, password, is_admin) " +
                            "VALUES (?, ?, ?, ?)",
                    new Object[]{
                            DEFAULT_ADMIN_NAME,
                            DEFAULT_ADMIN_EMAIL,
                            DEFAULT_ADMIN_PASSWORD,
                            1
                    });
        }
    };

    // Migration from version 2 to 3 (create analytics table)
    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS analytics (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "eventType TEXT, " +
                    "details TEXT, " +
                    "timestamp INTEGER NOT NULL, " +
                    "userId INTEGER)");
        }
    };

    // Migration from version 3 to 4 (add expiry fields to announcements)
    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE announcements ADD COLUMN expiryTime INTEGER");
            database.execSQL("ALTER TABLE announcements ADD COLUMN isExpired INTEGER NOT NULL DEFAULT 0");
            database.execSQL("UPDATE announcements SET expiryTime = createdAt + 604800000");
        }
    };

    // Migration from version 4 to 5 (add system settings table)
    private static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS system_settings (" +
                    "id INTEGER PRIMARY KEY NOT NULL DEFAULT 1, " +
                    "announcement_duration INTEGER NOT NULL, " +
                    "max_users INTEGER NOT NULL)");

            database.execSQL("INSERT INTO system_settings (id, announcement_duration, max_users) " +
                            "VALUES (1, ?, ?)",
                    new Object[]{DEFAULT_ANNOUNCEMENT_DURATION, DEFAULT_MAX_USERS});
        }
    };

    // Migration from version 5 to 6 (fix analytics table schema)
    private static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // No changes needed - just increment version
        }
    };

    // Migration from version 6 to 7 (fix system_settings table schema)
    private static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // No changes needed - just increment version
        }
    };

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "classroom_db"
                            )
                            .addMigrations(
                                    MIGRATION_1_2,
                                    MIGRATION_2_3,
                                    MIGRATION_3_4,
                                    MIGRATION_4_5,
                                    MIGRATION_5_6,
                                    MIGRATION_6_7
                            )
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    databaseWriteExecutor.execute(() -> {
                                        // Create default admin account
                                        db.execSQL("INSERT INTO users (name, email, password, is_admin) " +
                                                        "VALUES (?, ?, ?, ?)",
                                                new Object[]{
                                                        DEFAULT_ADMIN_NAME,
                                                        DEFAULT_ADMIN_EMAIL,
                                                        DEFAULT_ADMIN_PASSWORD,
                                                        1
                                                });

                                        // Create default system settings
                                        db.execSQL("INSERT INTO system_settings (id, announcement_duration, max_users) " +
                                                        "VALUES (1, ?, ?)",
                                                new Object[]{DEFAULT_ANNOUNCEMENT_DURATION, DEFAULT_MAX_USERS});
                                    });
                                }
                            })
                            // Remove in production - only for development
                            //.fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static void verifyAdminAccount(Context context) {
        databaseWriteExecutor.execute(() -> {
            UserDao dao = getDatabase(context).userDao();
            if (dao.getUserByEmail(DEFAULT_ADMIN_EMAIL) == null) {
                User admin = new User(
                        DEFAULT_ADMIN_NAME,
                        DEFAULT_ADMIN_EMAIL,
                        DEFAULT_ADMIN_PASSWORD,
                        true
                );
                dao.insert(admin);
            }
        });
    }
}