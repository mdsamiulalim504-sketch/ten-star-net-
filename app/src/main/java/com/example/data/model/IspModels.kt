package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val password: String,
    val fullName: String,
    val phone: String,
    val address: String,
    val packageId: Long,
    val packageName: String,
    val speedMbps: Int,
    val monthlyBill: Double,
    val billStatus: String, // "PAID", "UNPAID", "OVERDUE"
    val lineStatus: String, // "ACTIVE", "DISABLED"
    val ipAddress: String,
    val pppoeUsername: String,
    val expiryDate: String,
    val daysRemaining: Int = 30,
    val createdAt: Long = System.currentTimeMillis(),
    val lastPaymentDate: String = "N/A"
)

@Entity(tableName = "packages")
data class InternetPackage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val speedMbps: Int,
    val priceBDT: Double,
    val validityDays: Int = 30,
    val description: String,
    val isPopular: Boolean = false,
    val isActive: Boolean = true
)

@Entity(tableName = "payment_transactions")
data class PaymentTransaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val userFullName: String,
    val amount: Double,
    val paymentMethod: String, // "bKash", "Nagad"
    val trxId: String,
    val senderNumber: String,
    val status: String, // "APPROVED", "PENDING", "REJECTED"
    val timestamp: Long = System.currentTimeMillis(),
    val mikrotikTriggered: Boolean = true,
    val mikrotikResponse: String = "OK: Line Activated via MikroTik RouterOS"
)

@Entity(tableName = "mikrotik_config")
data class MikroTikConfig(
    @PrimaryKey val id: Int = 1,
    val host: String = "192.168.88.1",
    val apiPort: Int = 8728,
    val restPort: Int = 443,
    val username: String = "admin",
    val password: String = "TenStarRouter@2026",
    val isConnected: Boolean = true,
    val autoEnableOnPayment: Boolean = true,
    val targetAddressList: String = "active_subscribers",
    val lastSyncTime: Long = System.currentTimeMillis()
)

@Entity(tableName = "mikrotik_logs")
data class MikroTikLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val command: String,
    val targetUsername: String,
    val status: String, // "SUCCESS", "FAILED"
    val details: String
)

@Entity(tableName = "isp_settings")
data class IspSettings(
    @PrimaryKey val id: Int = 1,
    val ispName: String = "Ten Star Net",
    val founderName: String = "Md Samiul Alim",
    val bkashNumber: String = "01712-345678",
    val nagadNumber: String = "01812-987654",
    val supportHotline: String = "+880 1712-345678",
    val supportEmail: String = "support@tenstarnet.com",
    val officeAddress: String = "Ten Star Net NOC, Central Plaza, Dhaka",
    val supabaseUrl: String = "https://tenstarnet.supabase.co",
    val supabaseAnonKey: String = "sb_anon_public_key_tenstarnet_demo"
)

@Entity(tableName = "speed_test_records")
data class SpeedTestRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val downloadMbps: Double,
    val uploadMbps: Double,
    val pingMs: Int,
    val jitterMs: Int,
    val serverName: String = "Ten Star Net Core Gateway"
)

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: String, // "USER" or "AI"
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)
