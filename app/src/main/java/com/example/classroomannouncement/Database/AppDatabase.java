package com.example.classroomannouncement.Database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.classroomannouncement.Database.Daos.AnnouncementDao;
import com.example.classroomannouncement.Database.Daos.AnalyticsDao;
import com.example.classroomannouncement.Database.Daos.MessageDao;
import com.example.classroomannouncement.Database.Daos.ProductDao;
import com.example.classroomannouncement.Database.Daos.SystemSettingsDao;
import com.example.classroomannouncement.Database.Daos.UserDao;
import com.example.classroomannouncement.Database.Entities.Announcement;
import com.example.classroomannouncement.Database.Entities.Analytics;
import com.example.classroomannouncement.Database.Entities.Message;
import com.example.classroomannouncement.Database.Entities.Product;
import com.example.classroomannouncement.Database.Entities.SystemSettings;
import com.example.classroomannouncement.Database.Entities.User;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {Announcement.class, User.class, Analytics.class,
                SystemSettings.class, Message.class, Product.class},
        version = 14,
        exportSchema = true
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract AnnouncementDao announcementDao();
    public abstract UserDao userDao();
    public abstract AnalyticsDao analyticsDao();
    public abstract SystemSettingsDao systemSettingsDao();
    public abstract MessageDao messageDao();
    public abstract ProductDao productDao();

    // Default admin credentials
    private static final String DEFAULT_ADMIN_EMAIL = "admin@school.edu";
    private static final String DEFAULT_ADMIN_NAME = "System Administrator";
    private static final String DEFAULT_ADMIN_PASSWORD = "Admin@123";

    // Default system settings
    private static final int DEFAULT_ANNOUNCEMENT_DURATION = 7;
    private static final int DEFAULT_MAX_USERS = 100;
    private static final int DEFAULT_MESSAGE_RETENTION_DAYS = 30;
    private static final int DEFAULT_MAX_PRODUCTS = 500;

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
                    "announcement_duration INTEGER NOT NULL DEFAULT 7, " +
                    "max_users INTEGER NOT NULL DEFAULT 100)");

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

    // Migration from version 7 to 8 (add chat messages table)
    private static final Migration MIGRATION_7_8 = new Migration(7, 8) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS messages (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "senderId TEXT NOT NULL, " +
                    "senderName TEXT, " +
                    "content TEXT NOT NULL, " +
                    "timestamp INTEGER NOT NULL, " +
                    "isRead INTEGER NOT NULL DEFAULT 0)");
        }
    };

    // Migration from version 8 to 9 (add message_retention_days to system_settings)
    private static final Migration MIGRATION_8_9 = new Migration(8, 9) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE system_settings ADD COLUMN message_retention_days INTEGER NOT NULL DEFAULT 30");
            database.execSQL("UPDATE system_settings SET message_retention_days = ? WHERE id = 1",
                    new Object[]{DEFAULT_MESSAGE_RETENTION_DAYS});
        }
    };

    // Migration from version 9 to 10 (fix default values in system_settings)
    private static final Migration MIGRATION_9_10 = new Migration(9, 10) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS system_settings_new (" +
                    "id INTEGER PRIMARY KEY NOT NULL DEFAULT 1, " +
                    "announcement_duration INTEGER NOT NULL DEFAULT 7, " +
                    "max_users INTEGER NOT NULL DEFAULT 100, " +
                    "message_retention_days INTEGER NOT NULL DEFAULT 30)");

            database.execSQL("INSERT INTO system_settings_new " +
                    "SELECT id, " +
                    "COALESCE(announcement_duration, 7), " +
                    "COALESCE(max_users, 100), " +
                    "COALESCE(message_retention_days, 30) " +
                    "FROM system_settings");

            database.execSQL("DROP TABLE system_settings");
            database.execSQL("ALTER TABLE system_settings_new RENAME TO system_settings");
        }
    };

    // Migration from version 10 to 11 (add products table)
    private static final Migration MIGRATION_10_11 = new Migration(10, 11) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS products (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "name TEXT NOT NULL, " +
                    "description TEXT, " +
                    "price REAL NOT NULL, " +
                    "imageUrl TEXT, " +
                    "stockQuantity INTEGER NOT NULL DEFAULT 0, " +
                    "createdAt INTEGER NOT NULL, " +
                    "isActive INTEGER NOT NULL DEFAULT 1, " +
                    "category TEXT)");
        }
    };

    // Migration from version 11 to 12 (add max_products to system_settings)
    private static final Migration MIGRATION_11_12 = new Migration(11, 12) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE system_settings ADD COLUMN max_products INTEGER NOT NULL DEFAULT 500");
            database.execSQL("UPDATE system_settings SET max_products = ? WHERE id = 1",
                    new Object[]{DEFAULT_MAX_PRODUCTS});
        }
    };

    // Migration from version 12 to 13 (final fix for system_settings structure)
    private static final Migration MIGRATION_12_13 = new Migration(12, 13) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS system_settings_new (" +
                    "id INTEGER PRIMARY KEY NOT NULL DEFAULT 1, " +
                    "announcement_duration INTEGER NOT NULL DEFAULT 7, " +
                    "max_users INTEGER NOT NULL DEFAULT 100, " +
                    "message_retention_days INTEGER NOT NULL DEFAULT 30, " +
                    "max_products INTEGER NOT NULL DEFAULT 500)");

            database.execSQL("INSERT INTO system_settings_new " +
                    "SELECT id, announcement_duration, max_users, " +
                    "message_retention_days, max_products " +
                    "FROM system_settings");

            database.execSQL("DROP TABLE system_settings");
            database.execSQL("ALTER TABLE system_settings_new RENAME TO system_settings");
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
                                    MIGRATION_6_7,
                                    MIGRATION_7_8,
                                    MIGRATION_8_9,
                                    MIGRATION_9_10,
                                    MIGRATION_10_11,
                                    MIGRATION_11_12,
                                    MIGRATION_12_13
                            )
                            .fallbackToDestructiveMigration()
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    databaseWriteExecutor.execute(() -> {
                                        db.execSQL("INSERT INTO users (name, email, password, is_admin) " +
                                                        "VALUES (?, ?, ?, ?)",
                                                new Object[]{
                                                        DEFAULT_ADMIN_NAME,
                                                        DEFAULT_ADMIN_EMAIL,
                                                        DEFAULT_ADMIN_PASSWORD,
                                                        1
                                                });

                                        db.execSQL("INSERT INTO system_settings " +
                                                        "(id, announcement_duration, max_users, message_retention_days, max_products) " +
                                                        "VALUES (1, ?, ?, ?, ?)",
                                                new Object[]{
                                                        DEFAULT_ANNOUNCEMENT_DURATION,
                                                        DEFAULT_MAX_USERS,
                                                        DEFAULT_MESSAGE_RETENTION_DAYS,
                                                        DEFAULT_MAX_PRODUCTS
                                                });

                                        db.execSQL("INSERT INTO products " +
                                                        "(name, description, price, imageUrl, stockQuantity, category, createdAt) " +
                                                        "VALUES ('Sample Notebook', 'College ruled notebook', 4.99, '', 50, 'Supplies', ?)",
                                                new Object[]{System.currentTimeMillis()});
                                    });
                                }
                            })
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