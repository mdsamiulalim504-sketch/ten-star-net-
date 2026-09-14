package com.example.data.repository

import android.content.Context
import androidx.room.Room
import com.example.data.local.TenStarDatabase
import com.example.data.model.ChatMessage
import com.example.data.model.InternetPackage
import com.example.data.model.IspSettings
import com.example.data.model.MikroTikConfig
import com.example.data.model.MikroTikLog
import com.example.data.model.PaymentTransaction
import com.example.data.model.SpeedTestRecord
import com.example.data.model.User
import com.example.data.remote.GeminiService
import com.example.data.remote.MikroTikService
import com.example.data.remote.SupabaseSyncService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IspRepository(context: Context) {

    private val db = Room.databaseBuilder(
        context.applicationContext,
        TenStarDatabase::class.java,
        "ten_star_net.db"
    ).fallbackToDestructiveMigration().build()

    private val userDao = db.userDao()
    private val packageDao = db.packageDao()
    private val paymentDao = db.paymentDao()
    private val mikrotikDao = db.mikrotikDao()
    private val ispSettingsDao = db.ispSettingsDao()
    private val speedTestDao = db.speedTestDao()

    private val geminiService = GeminiService()
    private val mikrotikService = MikroTikService()
    private val supabaseSyncService = SupabaseSyncService()

    // Fixed Single Master Admin credentials
    companion object {
        const val MASTER_ADMIN_USERNAME = "admin"
        const val MASTER_ADMIN_PASSWORD = "admin"
        const val MASTER_ADMIN_NAME = "Md Samiul Alim"
        const val MASTER_ADMIN_ROLE = "Master Admin & ISP Founder"
    }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            seedInitialDataIfNeeded()
        }
    }

    private suspend fun seedInitialDataIfNeeded() {
        // Seed default settings if missing
        val existingSettings = ispSettingsDao.getSettingsDirect()
        if (existingSettings == null) {
            ispSettingsDao.saveSettings(
                IspSettings(
                    id = 1,
                    ispName = "Ten Star Net",
                    founderName = "Md Samiul Alim",
                    bkashNumber = "01712-345678",
                    nagadNumber = "01812-987654",
                    supportHotline = "+880 1712-345678",
                    supportEmail = "mdsamiulalim504@gmail.com",
                    officeAddress = "Ten Star Net NOC, Central Plaza, Dhaka"
                )
            )
        }

        // Seed default MikroTik config if missing
        val existingMikroTik = mikrotikDao.getConfigDirect()
        if (existingMikroTik == null) {
            mikrotikDao.saveConfig(
                MikroTikConfig(
                    id = 1,
                    host = "192.168.88.1",
                    apiPort = 8728,
                    restPort = 443,
                    username = "admin",
                    password = "TenStarRouter@2026",
                    isConnected = true,
                    autoEnableOnPayment = true,
                    targetAddressList = "active_subscribers"
                )
            )
        }

        // Seed default packages if empty
        val packages = packageDao.getAllPackages().firstOrNull() ?: emptyList()
        if (packages.isEmpty()) {
            val defaultPackages = listOf(
                InternetPackage(
                    name = "Ten Star Starter - 15 Mbps",
                    speedMbps = 15,
                    priceBDT = 600.0,
                    validityDays = 30,
                    description = "Smooth browsing, YouTube 1080p, and unlimited bufferless social media.",
                    isPopular = false,
                    isActive = true
                ),
                InternetPackage(
                    name = "Ten Star Silver - 30 Mbps",
                    speedMbps = 30,
                    priceBDT = 800.0,
                    validityDays = 30,
                    description = "Ultra-fast streaming, Zoom HD calls, and low latency online gaming.",
                    isPopular = true,
                    isActive = true
                ),
                InternetPackage(
                    name = "Ten Star Gold - 50 Mbps",
                    speedMbps = 50,
                    priceBDT = 1200.0,
                    validityDays = 30,
                    description = "4K Ultra-HD streaming, high-speed downloads, dedicated optical fiber speed.",
                    isPopular = false,
                    isActive = true
                ),
                InternetPackage(
                    name = "Ten Star Turbo - 100 Mbps",
                    speedMbps = 100,
                    priceBDT = 2000.0,
                    validityDays = 30,
                    description = "Enterprise tier gigabit fiber, zero latency, symmetric upload & download.",
                    isPopular = false,
                    isActive = true
                )
            )
            defaultPackages.forEach { packageDao.insertPackage(it) }
        }

        // Seed sample customer users if empty (created by admin)
        val users = userDao.getAllUsers().firstOrNull() ?: emptyList()
        if (users.isEmpty()) {
            val sampleCustomer1 = User(
                username = "samiul_user",
                password = "user123",
                fullName = "Samiul Subscriber",
                phone = "01700-112233",
                address = "House 12, Road 4, Sector 7, Uttara",
                packageId = 2,
                packageName = "Ten Star Silver - 30 Mbps",
                speedMbps = 30,
                monthlyBill = 800.0,
                billStatus = "UNPAID",
                lineStatus = "DISABLED",
                ipAddress = "192.168.88.105",
                pppoeUsername = "samiul_user@tenstarnet",
                expiryDate = "30 Sep 2026",
                daysRemaining = 2,
                lastPaymentDate = "None (Overdue)"
            )
            val sampleCustomer2 = User(
                username = "rahim_net",
                password = "user123",
                fullName = "Abdur Rahim",
                phone = "01811-445566",
                address = "Flat 4B, Green Road, Dhanmondi",
                packageId = 3,
                packageName = "Ten Star Gold - 50 Mbps",
                speedMbps = 50,
                monthlyBill = 1200.0,
                billStatus = "PAID",
                lineStatus = "ACTIVE",
                ipAddress = "192.168.88.110",
                pppoeUsername = "rahim_net@tenstarnet",
                expiryDate = "15 Oct 2026",
                daysRemaining = 28,
                lastPaymentDate = "01 Sep 2026"
            )
            userDao.insertUser(sampleCustomer1)
            userDao.insertUser(sampleCustomer2)

            // Add initial MikroTik logs
            mikrotikDao.insertLog(
                MikroTikLog(
                    command = "/ppp/secret/set [find name=\"rahim_net\"] disabled=no",
                    targetUsername = "rahim_net",
                    status = "SUCCESS",
                    details = "Automatic sync: Line activated via RouterOS API"
                )
            )
        }
    }

    // --- User & Authentication Queries ---
    fun getAllUsers(): Flow<List<User>> = userDao.getAllUsers()

    fun observeUser(username: String): Flow<User?> = userDao.observeUser(username)

    suspend fun getUserByUsername(username: String): User? = userDao.getUserByUsername(username)

    // Strict registration rule: Only Admin creates accounts
    suspend fun createUserByAdmin(user: User): Long {
        val id = userDao.insertUser(user)
        // Log in MikroTik that new user secret is created
        mikrotikDao.insertLog(
            MikroTikLog(
                command = "/ppp/secret/add name=\"${user.username}\" password=\"${user.password}\" profile=\"${user.packageName}\"",
                targetUsername = user.username,
                status = "SUCCESS",
                details = "Admin Md Samiul Alim created customer account & synced with RouterOS"
            )
        )
        return id
    }

    suspend fun updateUser(user: User) = userDao.updateUser(user)

    suspend fun deleteUser(id: Long) = userDao.deleteUser(id)

    suspend fun toggleUserLineStatus(username: String, currentStatus: String, ipAddress: String) {
        val newStatus = if (currentStatus == "ACTIVE") "DISABLED" else "ACTIVE"
        userDao.updateLineStatus(username, newStatus)

        val config = mikrotikDao.getConfigDirect() ?: MikroTikConfig()
        if (newStatus == "ACTIVE") {
            val result = mikrotikService.enableUserInternetLine(config, username, ipAddress)
            mikrotikDao.insertLog(
                MikroTikLog(
                    command = result.commandExecuted,
                    targetUsername = username,
                    status = if (result.success) "SUCCESS" else "FAILED",
                    details = "Admin toggle: ${result.details}"
                )
            )
        } else {
            val result = mikrotikService.disableUserInternetLine(config, username, ipAddress)
            mikrotikDao.insertLog(
                MikroTikLog(
                    command = result.commandExecuted,
                    targetUsername = username,
                    status = if (result.success) "SUCCESS" else "FAILED",
                    details = "Admin toggle: ${result.details}"
                )
            )
        }
    }

    // --- Online Payment Processing with Instant MikroTik Trigger ---
    suspend fun processOnlineBillPayment(
        username: String,
        amount: Double,
        method: String,
        trxId: String,
        senderNumber: String
    ): PaymentTransaction = withContext(Dispatchers.IO) {
        val user = userDao.getUserByUsername(username)
        val userFullName = user?.fullName ?: username
        val ipAddress = user?.ipAddress ?: "192.168.88.100"

        val config = mikrotikDao.getConfigDirect() ?: MikroTikConfig()
        // Automatically trigger MikroTik router command to instantly enable internet line
        val mikrotikResult = mikrotikService.enableUserInternetLine(config, username, ipAddress)

        // Mark user as PAID and line as ACTIVE
        val paymentDate = "Today, " + java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", java.util.Locale.getDefault()).format(java.util.Date())
        userDao.markUserPaidAndActive(username, "PAID", paymentDate)

        // Record payment transaction
        val transaction = PaymentTransaction(
            username = username,
            userFullName = userFullName,
            amount = amount,
            paymentMethod = method,
            trxId = trxId,
            senderNumber = senderNumber,
            status = "APPROVED",
            timestamp = System.currentTimeMillis(),
            mikrotikTriggered = true,
            mikrotikResponse = mikrotikResult.details
        )
        paymentDao.insertPayment(transaction)

        // Record audit log in MikroTik table
        mikrotikDao.insertLog(
            MikroTikLog(
                command = mikrotikResult.commandExecuted,
                targetUsername = username,
                status = if (mikrotikResult.success) "SUCCESS" else "FAILED",
                details = "Automated Online Payment ($method TrxID: $trxId). Line instantly enabled!"
            )
        )

        transaction
    }

    // --- Packages Manager ---
    fun getAllPackages(): Flow<List<InternetPackage>> = packageDao.getAllPackages()

    suspend fun savePackage(pkg: InternetPackage) {
        if (pkg.id == 0L) {
            packageDao.insertPackage(pkg)
        } else {
            packageDao.updatePackage(pkg)
        }
    }

    suspend fun deletePackage(id: Long) = packageDao.deletePackage(id)

    // --- Payment Numbers & Settings ---
    fun getSettings(): Flow<IspSettings?> = ispSettingsDao.getSettings()

    suspend fun updatePaymentNumbers(bkash: String, nagad: String) {
        ispSettingsDao.updatePaymentNumbers(bkash, nagad)
    }

    suspend fun saveSettings(settings: IspSettings) {
        ispSettingsDao.saveSettings(settings)
    }

    // --- MikroTik Router Configuration & Logs ---
    fun getMikroTikConfig(): Flow<MikroTikConfig?> = mikrotikDao.getConfig()

    suspend fun saveMikroTikConfig(config: MikroTikConfig) = mikrotikDao.saveConfig(config)

    fun getMikroTikLogs(): Flow<List<MikroTikLog>> = mikrotikDao.getRecentLogs()

    suspend fun checkRouterHealth(): MikroTikService.RouterStatus {
        val config = mikrotikDao.getConfigDirect() ?: MikroTikConfig()
        return mikrotikService.checkRouterStatus(config)
    }

    // --- Payments List ---
    fun getAllPayments(): Flow<List<PaymentTransaction>> = paymentDao.getAllPayments()

    fun getPaymentsForUser(username: String): Flow<List<PaymentTransaction>> = paymentDao.getPaymentsForUser(username)

    // --- Speed Test Results ---
    fun getSpeedTestHistory(): Flow<List<SpeedTestRecord>> = speedTestDao.getRecentSpeedTests()

    suspend fun recordSpeedTest(result: SpeedTestRecord) = speedTestDao.insertSpeedTest(result)

    // --- AI Customer Support Assistant ---
    suspend fun askAiSupport(userPrompt: String, userContextInfo: String): String {
        return geminiService.getAiHelpResponse(userPrompt, userContextInfo)
    }

    // --- Supabase Cloud Sync ---
    suspend fun syncWithSupabase(): SupabaseSyncService.SyncResult {
        val settings = ispSettingsDao.getSettingsDirect() ?: IspSettings()
        val users = userDao.getAllUsers().firstOrNull() ?: emptyList()
        return supabaseSyncService.syncUsersToOnlineBackend(settings, users)
    }
}
