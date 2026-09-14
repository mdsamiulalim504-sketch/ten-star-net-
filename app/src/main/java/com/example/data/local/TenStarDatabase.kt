package com.example.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import com.example.data.model.InternetPackage
import com.example.data.model.IspSettings
import com.example.data.model.MikroTikConfig
import com.example.data.model.MikroTikLog
import com.example.data.model.PaymentTransaction
import com.example.data.model.SpeedTestRecord
import com.example.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY id DESC")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    fun observeUser(username: String): Flow<User?>

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Long): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Update
    suspend fun updateUser(user: User)

    @Query("UPDATE users SET lineStatus = :lineStatus WHERE username = :username")
    suspend fun updateLineStatus(username: String, lineStatus: String)

    @Query("UPDATE users SET billStatus = :billStatus, lineStatus = 'ACTIVE', lastPaymentDate = :paymentDate WHERE username = :username")
    suspend fun markUserPaidAndActive(username: String, billStatus: String, paymentDate: String)

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUser(id: Long)

    @Query("SELECT COUNT(*) FROM users")
    fun getUserCount(): Flow<Int>
}

@Dao
interface PackageDao {
    @Query("SELECT * FROM packages ORDER BY speedMbps ASC")
    fun getAllPackages(): Flow<List<InternetPackage>>

    @Query("SELECT * FROM packages WHERE id = :id LIMIT 1")
    suspend fun getPackageById(id: Long): InternetPackage?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPackage(pkg: InternetPackage): Long

    @Update
    suspend fun updatePackage(pkg: InternetPackage)

    @Query("DELETE FROM packages WHERE id = :id")
    suspend fun deletePackage(id: Long)
}

@Dao
interface PaymentDao {
    @Query("SELECT * FROM payment_transactions ORDER BY timestamp DESC")
    fun getAllPayments(): Flow<List<PaymentTransaction>>

    @Query("SELECT * FROM payment_transactions WHERE username = :username ORDER BY timestamp DESC")
    fun getPaymentsForUser(username: String): Flow<List<PaymentTransaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: PaymentTransaction): Long

    @Update
    suspend fun updatePayment(payment: PaymentTransaction)
}

@Dao
interface MikroTikDao {
    @Query("SELECT * FROM mikrotik_config WHERE id = 1 LIMIT 1")
    fun getConfig(): Flow<MikroTikConfig?>

    @Query("SELECT * FROM mikrotik_config WHERE id = 1 LIMIT 1")
    suspend fun getConfigDirect(): MikroTikConfig?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveConfig(config: MikroTikConfig)

    @Query("SELECT * FROM mikrotik_logs ORDER BY timestamp DESC LIMIT 50")
    fun getRecentLogs(): Flow<List<MikroTikLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: MikroTikLog)

    @Query("DELETE FROM mikrotik_logs")
    suspend fun clearLogs()
}

@Dao
interface IspSettingsDao {
    @Query("SELECT * FROM isp_settings WHERE id = 1 LIMIT 1")
    fun getSettings(): Flow<IspSettings?>

    @Query("SELECT * FROM isp_settings WHERE id = 1 LIMIT 1")
    suspend fun getSettingsDirect(): IspSettings?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(settings: IspSettings)

    @Query("UPDATE isp_settings SET bkashNumber = :bkash, nagadNumber = :nagad WHERE id = 1")
    suspend fun updatePaymentNumbers(bkash: String, nagad: String)
}

@Dao
interface SpeedTestDao {
    @Query("SELECT * FROM speed_test_records ORDER BY timestamp DESC LIMIT 20")
    fun getRecentSpeedTests(): Flow<List<SpeedTestRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpeedTest(record: SpeedTestRecord)
}

@Database(
    entities = [
        User::class,
        InternetPackage::class,
        PaymentTransaction::class,
        MikroTikConfig::class,
        MikroTikLog::class,
        IspSettings::class,
        SpeedTestRecord::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TenStarDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun packageDao(): PackageDao
    abstract fun paymentDao(): PaymentDao
    abstract fun mikrotikDao(): MikroTikDao
    abstract fun ispSettingsDao(): IspSettingsDao
    abstract fun speedTestDao(): SpeedTestDao
}
