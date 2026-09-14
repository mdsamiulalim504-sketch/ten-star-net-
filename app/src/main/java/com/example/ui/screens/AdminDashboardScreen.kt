package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.InternetPackage
import com.example.data.model.MikroTikLog
import com.example.data.model.PaymentTransaction
import com.example.data.model.User
import com.example.ui.IspViewModel
import com.example.ui.components.IspFooter
import com.example.ui.theme.BkashPink
import com.example.ui.theme.BorderNavy
import com.example.ui.theme.CardNavy
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.NagadOrange
import com.example.ui.theme.StarCyan
import com.example.ui.theme.StarGold
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusGreenBg
import com.example.ui.theme.StatusRed
import com.example.ui.theme.StatusRedBg
import com.example.ui.theme.SurfaceNavy
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AdminDashboardScreen(
    viewModel: IspViewModel,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val allUsers by viewModel.allUsers.collectAsState()
    val allPackages by viewModel.allPackages.collectAsState()
    val ispSettings by viewModel.ispSettings.collectAsState()
    val mikrotikConfig by viewModel.mikrotikConfig.collectAsState()
    val mikrotikLogs by viewModel.mikrotikLogs.collectAsState()
    val allPayments by viewModel.allPayments.collectAsState()
    val userMessage by viewModel.userMessage.collectAsState()

    var showAddUserDialog by remember { mutableStateOf(false) }
    var showAddPackageDialog by remember { mutableStateOf(false) }
    var editingPackage by remember { mutableStateOf<InternetPackage?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DeepNavy,
        bottomBar = {
            Column {
                NavigationBar(
                    containerColor = CardNavy,
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = { Icon(Icons.Default.Tune, contentDescription = "Overview") },
                        label = { Text("Numbers", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepNavy,
                            selectedTextColor = StarGold,
                            indicatorColor = StarGold,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = { Icon(Icons.Default.Group, contentDescription = "Users") },
                        label = { Text("Users", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepNavy,
                            selectedTextColor = StarGold,
                            indicatorColor = StarGold,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        icon = { Icon(Icons.Default.Inventory2, contentDescription = "Packages") },
                        label = { Text("Packages", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepNavy,
                            selectedTextColor = StarGold,
                            indicatorColor = StarGold,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                    NavigationBarItem(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        icon = { Icon(Icons.Default.Bolt, contentDescription = "MikroTik") },
                        label = { Text("MikroTik", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepNavy,
                            selectedTextColor = StarGold,
                            indicatorColor = StarGold,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                    NavigationBarItem(
                        selected = selectedTab == 4,
                        onClick = { selectedTab = 4 },
                        icon = { Icon(Icons.Default.Payment, contentDescription = "Payments") },
                        label = { Text("Payments", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepNavy,
                            selectedTextColor = StarGold,
                            indicatorColor = StarGold,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                }
                // Prominent mandatory footer
                IspFooter(hotline = ispSettings?.supportHotline ?: "+880 1712-345678")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
        ) {
            // Master Admin Top Header with Fixed Admin Photo & Brand Logo
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardNavy)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .border(2.dp, StarGold, CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_admin_profile),
                            contentDescription = "Master Admin Md Samiul Alim",
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag("admin_photo_master_header"),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Md Samiul Alim",
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(StarGold)
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "MASTER ADMIN",
                                    color = DeepNavy,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                        Text(
                            text = "Ten Star Net NOC • Single Admin Control",
                            color = StarCyan,
                            fontSize = 11.sp
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.syncCloudBackend() },
                        modifier = Modifier.testTag("admin_sync_cloud_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudSync,
                            contentDescription = "Sync Cloud",
                            tint = StarCyan
                        )
                    }
                    IconButton(
                        onClick = onLogout,
                        modifier = Modifier.testTag("admin_logout_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Log Out",
                            tint = StatusRed
                        )
                    }
                }
            }

            // Body content
            Box(modifier = Modifier.weight(1f)) {
                when (selectedTab) {
                    0 -> AdminOverviewTab(
                        ispSettings = ispSettings,
                        userCount = allUsers.size,
                        activeCount = allUsers.count { it.lineStatus == "ACTIVE" },
                        disabledCount = allUsers.count { it.lineStatus != "ACTIVE" },
                        totalRevenue = allUsers.sumOf { it.monthlyBill },
                        onUpdateNumbers = { bkash, nagad ->
                            viewModel.updatePaymentNumbers(bkash, nagad)
                        },
                        onSyncCloud = { viewModel.syncCloudBackend() }
                    )
                    1 -> AdminUsersTab(
                        users = allUsers,
                        packages = allPackages,
                        onAddNewUser = { showAddUserDialog = true },
                        onToggleLine = { username, status, ip ->
                            viewModel.toggleUserLine(username, status, ip)
                        },
                        onDeleteUser = { id -> viewModel.deleteCustomer(id) }
                    )
                    2 -> AdminPackagesTab(
                        packages = allPackages,
                        onAddPackage = {
                            editingPackage = null
                            showAddPackageDialog = true
                        },
                        onEditPackage = { pkg ->
                            editingPackage = pkg
                            showAddPackageDialog = true
                        },
                        onDeletePackage = { id -> viewModel.deletePackage(id) }
                    )
                    3 -> AdminMikroTikTab(
                        config = mikrotikConfig,
                        logs = mikrotikLogs,
                        onPingAndSync = { viewModel.pingAndSyncMikroTik() }
                    )
                    4 -> AdminPaymentsTab(
                        payments = allPayments
                    )
                }
            }
        }

        // Add Customer Dialog (Strict Admin Provisioning)
        if (showAddUserDialog) {
            AddCustomerDialog(
                packages = allPackages,
                onDismiss = { showAddUserDialog = false },
                onConfirm = { newUser ->
                    viewModel.createCustomerByAdmin(newUser) {
                        showAddUserDialog = false
                    }
                }
            )
        }

        // Add / Edit Package Dialog
        if (showAddPackageDialog) {
            AddEditPackageDialog(
                existingPackage = editingPackage,
                onDismiss = { showAddPackageDialog = false },
                onConfirm = { pkg ->
                    viewModel.savePackage(pkg) {
                        showAddPackageDialog = false
                    }
                }
            )
        }

        // Toast alert dialog
        userMessage?.let { msg ->
            AlertDialog(
                onDismissRequest = { viewModel.clearUserMessage() },
                containerColor = CardNavy,
                title = { Text("ISP Admin Message", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                text = { Text(msg, color = TextSecondary, fontSize = 13.sp) },
                confirmButton = {
                    Button(
                        onClick = { viewModel.clearUserMessage() },
                        colors = ButtonDefaults.buttonColors(containerColor = StarGold, contentColor = DeepNavy)
                    ) {
                        Text("OK", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    }
}

@Composable
fun AdminOverviewTab(
    ispSettings: com.example.data.model.IspSettings?,
    userCount: Int,
    activeCount: Int,
    disabledCount: Int,
    totalRevenue: Double,
    onUpdateNumbers: (bkash: String, nagad: String) -> Unit,
    onSyncCloud: () -> Unit
) {
    var bkashInput by remember(ispSettings?.bkashNumber) {
        mutableStateOf(ispSettings?.bkashNumber ?: "01712-345678")
    }
    var nagadInput by remember(ispSettings?.nagadNumber) {
        mutableStateOf(ispSettings?.nagadNumber ?: "01812-987654")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Quick Stats Row
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MetricCard(
                title = "Total Users",
                value = "$userCount",
                subtitle = "Subscribers",
                accentColor = StarCyan,
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = "Active Lines",
                value = "$activeCount",
                subtitle = "Online PPPoE",
                accentColor = StatusGreen,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MetricCard(
                title = "Disabled Lines",
                value = "$disabledCount",
                subtitle = "Unpaid / Offline",
                accentColor = StatusRed,
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = "Monthly Rev.",
                value = "৳ ${totalRevenue.toInt()}",
                subtitle = "Billable",
                accentColor = StarGold,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Dynamic Payment Numbers Editor (CRITICAL REQUIREMENT)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = CardNavy),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, StarGold.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Tune, contentDescription = null, tint = StarGold, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "DYNAMIC PAYMENT NUMBERS",
                        color = StarGold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Change active bKash and Nagad numbers anytime. Changes update instantly across all customer dashboards.",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // bKash Number Input
                OutlinedTextField(
                    value = bkashInput,
                    onValueChange = { bkashInput = it },
                    label = { Text("Active bKash Personal Number") },
                    leadingIcon = {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(BkashPink),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("৳", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_bkash_number_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BkashPink,
                        unfocusedBorderColor = BorderNavy,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Nagad Number Input
                OutlinedTextField(
                    value = nagadInput,
                    onValueChange = { nagadInput = it },
                    label = { Text("Active Nagad Personal Number") },
                    leadingIcon = {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(NagadOrange),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("৳", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_nagad_number_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NagadOrange,
                        unfocusedBorderColor = BorderNavy,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        onUpdateNumbers(bkashInput, nagadInput)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("save_payment_numbers_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = StarGold,
                        contentColor = DeepNavy
                    )
                ) {
                    Text(
                        text = "Save & Broadcast Payment Numbers",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Online Backend Sync (Supabase / Node.js)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = CardNavy),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.CloudSync, contentDescription = null, tint = StarCyan, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ONLINE BACKEND STATUS (SUPABASE/NODE.JS)",
                        color = StarCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Configured URL: ${ispSettings?.supabaseUrl ?: "https://tenstarnet.supabase.co"}",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
                Text(
                    text = "Status: Online & Ready for Full-Stack Synchronization",
                    color = StatusGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onSyncCloud,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Trigger Instant Supabase Cloud Sync", color = StarCyan, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun MetricCard(title: String, value: String, subtitle: String, accentColor: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardNavy),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = title, color = TextSecondary, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, color = accentColor, fontSize = 20.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, color = TextMuted, fontSize = 10.sp)
        }
    }
}

@Composable
fun AdminUsersTab(
    users: List<User>,
    packages: List<InternetPackage>,
    onAddNewUser: () -> Unit,
    onToggleLine: (username: String, currentStatus: String, ip: String) -> Unit,
    onDeleteUser: (Long) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredUsers = remember(users, searchQuery) {
        if (searchQuery.isBlank()) users
        else users.filter {
            it.username.contains(searchQuery, ignoreCase = true) ||
            it.fullName.contains(searchQuery, ignoreCase = true) ||
            it.phone.contains(searchQuery)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "STRICT USER PROVISIONING", color = StarGold, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Text(text = "${users.size} Registered Customers", color = TextSecondary, fontSize = 11.sp)
            }

            Button(
                onClick = onAddNewUser,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = StarGold, contentColor = DeepNavy),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.testTag("add_new_user_button")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Create User", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Search Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search by username, name, or phone...", fontSize = 12.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = StarGold,
                unfocusedBorderColor = BorderNavy,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredUsers) { u ->
                val isLineActive = u.lineStatus == "ACTIVE"
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardNavy),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = u.fullName, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Text(text = "@${u.username} • ${u.phone}", color = StarCyan, fontSize = 12.sp)
                            }

                            // Line Status Switch Button
                            Button(
                                onClick = { onToggleLine(u.username, u.lineStatus, u.ipAddress) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isLineActive) StatusGreenBg else StatusRedBg,
                                    contentColor = if (isLineActive) StatusGreen else StatusRed
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isLineActive) StatusGreen else StatusRed
                                ),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("toggle_line_${u.username}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PowerSettingsNew,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isLineActive) "ACTIVE" else "DISABLED",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Package: ${u.packageName}", color = TextSecondary, fontSize = 11.sp)
                            Text(text = "Bill: ৳${u.monthlyBill.toInt()} (${u.billStatus})", color = if (u.billStatus == "PAID") StatusGreen else StatusRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "IP: ${u.ipAddress} • Pass: ${u.password}", color = TextMuted, fontSize = 11.sp)
                            IconButton(
                                onClick = { onDeleteUser(u.id) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = StatusRed, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminPackagesTab(
    packages: List<InternetPackage>,
    onAddPackage: () -> Unit,
    onEditPackage: (InternetPackage) -> Unit,
    onDeletePackage: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "INTERNET PACKAGES", color = StarCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Text(text = "Create, edit, or adjust pricing & speed", color = TextSecondary, fontSize = 11.sp)
            }

            Button(
                onClick = onAddPackage,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = StarCyan, contentColor = DeepNavy),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.testTag("add_package_button")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("New Package", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(packages) { pkg ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardNavy),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = pkg.name, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Text(text = "${pkg.speedMbps} Mbps Fiber Speed", color = StarCyan, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                            Text(text = "৳ ${pkg.priceBDT.toInt()}/mo", color = StarGold, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = pkg.description, color = TextSecondary, fontSize = 11.sp, lineHeight = 15.sp)

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            OutlinedButton(
                                onClick = { onEditPackage(pkg) },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Edit", fontSize = 11.sp)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedButton(
                                onClick = { onDeletePackage(pkg.id) },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = StatusRed),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Delete", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminMikroTikTab(
    config: com.example.data.model.MikroTikConfig?,
    logs: List<MikroTikLog>,
    onPingAndSync: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
    ) {
        // RouterOS Connection Status Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = CardNavy),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, StarCyan)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Router, contentDescription = null, tint = StarCyan, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = "MIKROTIK ROUTEROS API", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Host: ${config?.host ?: "192.168.88.1"}:${config?.apiPort ?: 8728}", color = StarCyan, fontSize = 11.sp)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(StatusGreenBg)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = "CONNECTED", color = StatusGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "⚡ Automated RouterOS Trigger Active: When a customer pays their bill online, MikroTik command '/ppp/secret/set <user> disabled=no' is triggered instantly to activate their line.",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onPingAndSync,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = StarCyan, contentColor = DeepNavy)
                ) {
                    Icon(imageVector = Icons.Default.Bolt, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ping Router & Test Line Automation", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(text = "AUTOMATION COMMAND AUDIT LOG", color = StarGold, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(logs) { log ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardNavy)
                        .border(1.dp, BorderNavy, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Target: ${log.targetUsername}", color = StarCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (log.status == "SUCCESS") StatusGreenBg else StatusRedBg)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(text = log.status, color = if (log.status == "SUCCESS") StatusGreen else StatusRed, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = log.command, color = StarGold, fontSize = 11.sp, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = log.details, color = TextSecondary, fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun AdminPaymentsTab(
    payments: List<PaymentTransaction>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
    ) {
        Text(text = "ONLINE BILLING TRANSACTIONS", color = StarCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(10.dp))

        if (payments.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No payment transactions recorded yet.", color = TextSecondary, fontSize = 13.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(payments) { p ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = CardNavy),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = "${p.userFullName} (@${p.username})", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text(text = "${p.paymentMethod} • Sender: ${p.senderNumber}", color = TextSecondary, fontSize = 11.sp)
                                }
                                Text(text = "৳ ${p.amount.toInt()}", color = StarCyan, fontSize = 16.sp, fontWeight = FontWeight.Black)
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "TrxID: ${p.trxId}", color = StarGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)

                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(SurfaceNavy)
                                    .padding(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.Default.Bolt, contentDescription = null, tint = StatusGreen, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = p.mikrotikResponse, color = StatusGreen, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

// Dialog: Strict Customer Account Creation by Admin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCustomerDialog(
    packages: List<InternetPackage>,
    onDismiss: () -> Unit,
    onConfirm: (User) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var selectedPackage by remember { mutableStateOf(packages.firstOrNull()) }
    var ipAddress by remember { mutableStateOf("192.168.88.${(101..220).random()}") }
    var lineStatus by remember { mutableStateOf("ACTIVE") }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CardNavy,
        title = {
            Column {
                Text("Create Customer Account", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("Strict Admin Registration Policy", color = StarGold, fontSize = 11.sp)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Assigned Username") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("new_user_username_input")
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Initial Password") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("new_user_password_input")
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Customer Full Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Mobile Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Customer Address") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Package Selector
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedPackage?.name ?: "Select Package",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Internet Package") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(CardNavy)
                    ) {
                        packages.forEach { pkg ->
                            DropdownMenuItem(
                                text = { Text("${pkg.name} - ৳${pkg.priceBDT.toInt()}", color = TextPrimary) },
                                onClick = {
                                    selectedPackage = pkg
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = ipAddress,
                    onValueChange = { ipAddress = it },
                    label = { Text("MikroTik IP / PPPoE IP") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (username.isNotBlank() && password.isNotBlank()) {
                        val pkg = selectedPackage ?: packages.firstOrNull()
                        val newUser = User(
                            username = username.trim(),
                            password = password.trim(),
                            fullName = if (fullName.isBlank()) username.trim() else fullName.trim(),
                            phone = if (phone.isBlank()) "01700-000000" else phone.trim(),
                            address = if (address.isBlank()) "Dhaka, Bangladesh" else address.trim(),
                            packageId = pkg?.id ?: 1,
                            packageName = pkg?.name ?: "Ten Star Basic",
                            speedMbps = pkg?.speedMbps ?: 15,
                            monthlyBill = pkg?.priceBDT ?: 600.0,
                            billStatus = "PAID",
                            lineStatus = lineStatus,
                            ipAddress = ipAddress.trim(),
                            pppoeUsername = "${username.trim()}@tenstarnet",
                            expiryDate = "30 Oct 2026",
                            daysRemaining = 30
                        )
                        onConfirm(newUser)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = StarGold, contentColor = DeepNavy),
                modifier = Modifier.testTag("confirm_create_user_button")
            ) {
                Text("Create User & Sync RouterOS", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// Dialog: Add / Edit Package
@Composable
fun AddEditPackageDialog(
    existingPackage: InternetPackage?,
    onDismiss: () -> Unit,
    onConfirm: (InternetPackage) -> Unit
) {
    var name by remember { mutableStateOf(existingPackage?.name ?: "") }
    var speedStr by remember { mutableStateOf(existingPackage?.speedMbps?.toString() ?: "20") }
    var priceStr by remember { mutableStateOf(existingPackage?.priceBDT?.toInt()?.toString() ?: "700") }
    var desc by remember { mutableStateOf(existingPackage?.description ?: "Dedicated optical fiber broadband with low latency.") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CardNavy,
        title = {
            Text(
                if (existingPackage == null) "Create New Package" else "Edit Package",
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Package Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("package_name_input")
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = speedStr,
                    onValueChange = { speedStr = it },
                    label = { Text("Speed (Mbps)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("package_speed_input")
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = priceStr,
                    onValueChange = { priceStr = it },
                    label = { Text("Monthly Price (BDT ৳)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("package_price_input")
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Package Description") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val pkg = InternetPackage(
                        id = existingPackage?.id ?: 0L,
                        name = name.ifBlank { "Ten Star Standard" },
                        speedMbps = speedStr.toIntOrNull() ?: 20,
                        priceBDT = priceStr.toDoubleOrNull() ?: 700.0,
                        validityDays = 30,
                        description = desc,
                        isPopular = false,
                        isActive = true
                    )
                    onConfirm(pkg)
                },
                colors = ButtonDefaults.buttonColors(containerColor = StarCyan, contentColor = DeepNavy),
                modifier = Modifier.testTag("confirm_save_package_button")
            ) {
                Text("Save Package", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
