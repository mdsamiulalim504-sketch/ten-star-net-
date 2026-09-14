package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ChatMessage
import com.example.data.model.User
import com.example.ui.IspViewModel
import com.example.ui.components.IspFooter
import com.example.ui.components.SpeedometerGauge
import com.example.ui.theme.BkashPink
import com.example.ui.theme.BorderNavy
import com.example.ui.theme.CardNavy
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.NagadOrange
import com.example.ui.theme.StarCyan
import com.example.ui.theme.StarCyanDark
import com.example.ui.theme.StarGold
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusGreenBg
import com.example.ui.theme.StatusRed
import com.example.ui.theme.StatusRedBg
import com.example.ui.theme.SurfaceNavy
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDashboardScreen(
    user: User,
    viewModel: IspViewModel,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showPaymentSheet by remember { mutableStateOf(false) }

    val ispSettings by viewModel.ispSettings.collectAsState()
    val speedValue by viewModel.speedTestSpeed.collectAsState()
    val speedStage by viewModel.speedTestStage.collectAsState()
    val downloadMbps by viewModel.downloadMbps.collectAsState()
    val uploadMbps by viewModel.uploadMbps.collectAsState()
    val pingMs by viewModel.pingMs.collectAsState()
    val jitterMs by viewModel.jitterMs.collectAsState()
    val isTestingSpeed by viewModel.isTestingSpeed.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()
    val isAiThinking by viewModel.isAiThinking.collectAsState()
    val allPayments by viewModel.allPayments.collectAsState()
    val userMessage by viewModel.userMessage.collectAsState()

    // Filter payments for this user
    val myPayments = remember(allPayments, user.username) {
        allPayments.filter { it.username == user.username }
    }

    val clipboardManager = LocalClipboardManager.current

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
                        icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Dashboard") },
                        label = { Text("Dashboard", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepNavy,
                            selectedTextColor = StarCyan,
                            indicatorColor = StarCyan,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = { Icon(Icons.Default.Speed, contentDescription = "Speed Meter") },
                        label = { Text("Speed Meter", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepNavy,
                            selectedTextColor = StarCyan,
                            indicatorColor = StarCyan,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "AI Support") },
                        label = { Text("AI Assistant", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepNavy,
                            selectedTextColor = StarCyan,
                            indicatorColor = StarCyan,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                    NavigationBarItem(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        icon = { Icon(Icons.Default.History, contentDescription = "History") },
                        label = { Text("Bills", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepNavy,
                            selectedTextColor = StarCyan,
                            indicatorColor = StarCyan,
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
            // Static Top Header: Brand Logo & Fixed Admin Picture
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
                            .size(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, StarCyan, RoundedCornerShape(10.dp))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_ten_star_logo),
                            contentDescription = "Ten Star Net Logo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "TEN STAR NET",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Customer: ${user.username}",
                            color = StarCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Static Admin Photo Badge
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(text = "Md Samiul Alim", color = StarGold, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text(text = "ISP Admin", color = TextMuted, fontSize = 8.sp)
                    }
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, StarGold, CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_admin_profile),
                            contentDescription = "Md Samiul Alim",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = onLogout,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Log Out",
                            tint = TextSecondary
                        )
                    }
                }
            }

            // Tab Content
            Box(modifier = Modifier.weight(1f)) {
                when (selectedTab) {
                    0 -> UserDashboardHomeTab(
                        user = user,
                        ispSettings = ispSettings,
                        onOpenPayment = { showPaymentSheet = true }
                    )
                    1 -> UserSpeedMeterTab(
                        user = user,
                        speedValue = speedValue,
                        speedStage = speedStage,
                        downloadMbps = downloadMbps,
                        uploadMbps = uploadMbps,
                        pingMs = pingMs,
                        jitterMs = jitterMs,
                        isTestingSpeed = isTestingSpeed,
                        onStartSpeedTest = { viewModel.runSpeedTest(user.speedMbps) }
                    )
                    2 -> UserAiAssistantTab(
                        user = user,
                        messages = chatMessages,
                        isThinking = isAiThinking,
                        onSendMessage = { prompt ->
                            val context = "User: ${user.fullName} (${user.username}), Package: ${user.packageName}, Bill: ৳${user.monthlyBill}, Status: ${user.billStatus}, Line: ${user.lineStatus}"
                            viewModel.sendAiMessage(prompt, context)
                        }
                    )
                    3 -> UserBillsTab(
                        user = user,
                        payments = myPayments,
                        onOpenPayment = { showPaymentSheet = true }
                    )
                }
            }
        }

        // Online Bill Payment Bottom Sheet
        if (showPaymentSheet) {
            PaymentBottomSheet(
                user = user,
                ispSettings = ispSettings,
                onDismiss = { showPaymentSheet = false },
                onSubmitPayment = { method, trxId, senderNumber ->
                    viewModel.submitBillPayment(
                        username = user.username,
                        amount = user.monthlyBill,
                        method = method,
                        trxId = trxId,
                        senderNumber = senderNumber,
                        onSuccess = {
                            showPaymentSheet = false
                        }
                    )
                }
            )
        }

        // Notification alert dialog
        userMessage?.let { msg ->
            AlertDialog(
                onDismissRequest = { viewModel.clearUserMessage() },
                containerColor = CardNavy,
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = StarCyan
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("MikroTik Router Notification", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                },
                text = {
                    Text(msg, color = TextSecondary, fontSize = 13.sp, lineHeight = 18.sp)
                },
                confirmButton = {
                    Button(
                        onClick = { viewModel.clearUserMessage() },
                        colors = ButtonDefaults.buttonColors(containerColor = StarCyan, contentColor = DeepNavy)
                    ) {
                        Text("OK", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    }
}

@Composable
fun UserDashboardHomeTab(
    user: User,
    ispSettings: com.example.data.model.IspSettings?,
    onOpenPayment: () -> Unit
) {
    val isLineActive = user.lineStatus == "ACTIVE"
    val isPaid = user.billStatus == "PAID"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Line Status Banner (Instant visual awareness)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(if (isLineActive) StatusGreenBg else StatusRedBg)
                .border(
                    width = 1.5.dp,
                    color = if (isLineActive) StatusGreen else StatusRed,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(if (isLineActive) StatusGreen.copy(alpha = 0.2f) else StatusRed.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isLineActive) Icons.Default.Wifi else Icons.Default.WifiOff,
                        contentDescription = null,
                        tint = if (isLineActive) StatusGreen else StatusRed,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isLineActive) "INTERNET LINE: ACTIVE" else "INTERNET LINE: SUSPENDED",
                        color = if (isLineActive) StatusGreen else StatusRed,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = if (isLineActive)
                            "MikroTik PPPoE authenticated. 100% full optical speed active."
                        else
                            "Bill payment required. Pay via bKash/Nagad for instant auto-activation!",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dynamic Billing & Package Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = CardNavy),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "CURRENT BILLING", color = StarCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        Text(text = user.packageName, color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isPaid) StatusGreenBg else StatusRedBg)
                            .border(1.dp, if (isPaid) StatusGreen else StatusRed, RoundedCornerShape(10.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = user.billStatus,
                            color = if (isPaid) StatusGreen else StatusRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceNavy)
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Monthly Bill Amount", color = TextSecondary, fontSize = 12.sp)
                        Text(text = "৳ ${user.monthlyBill.toInt()}", color = TextPrimary, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "Valid Until", color = TextSecondary, fontSize = 12.sp)
                        Text(text = user.expiryDate, color = StarGold, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text(text = "${user.daysRemaining} days remaining", color = TextMuted, fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Pay Bill CTA
                if (!isPaid || !isLineActive) {
                    Button(
                        onClick = onOpenPayment,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("pay_bill_action_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StarCyan,
                            contentColor = DeepNavy
                        )
                    ) {
                        Icon(imageVector = Icons.Default.CreditCard, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Pay Bill (bKash / Nagad) & Enable Line", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(StatusGreenBg.copy(alpha = 0.5f))
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = StatusGreen, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Bill Paid • Line Active until ${user.expiryDate}", color = StatusGreen, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dynamic Profile Details Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = CardNavy),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(text = "SUBSCRIBER CONNECTION DETAILS", color = StarCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(12.dp))

                ProfileRowItem(label = "Full Name", value = user.fullName)
                ProfileRowItem(label = "Phone Number", value = user.phone)
                ProfileRowItem(label = "Address", value = user.address)
                ProfileRowItem(label = "PPPoE Username", value = user.pppoeUsername)
                ProfileRowItem(label = "Assigned IP", value = user.ipAddress)
                ProfileRowItem(label = "Bandwidth Speed", value = "${user.speedMbps} Mbps Optical Fiber")
                ProfileRowItem(label = "Last Payment Date", value = user.lastPaymentDate)
            }
        }
    }
}

@Composable
fun ProfileRowItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = TextSecondary, fontSize = 12.sp)
        Text(text = value, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun UserSpeedMeterTab(
    user: User,
    speedValue: Float,
    speedStage: com.example.ui.components.SpeedTestStage,
    downloadMbps: Double,
    uploadMbps: Double,
    pingMs: Int,
    jitterMs: Int,
    isTestingSpeed: Boolean,
    onStartSpeedTest: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Speedometer Gauge Component
        SpeedometerGauge(
            currentSpeed = speedValue,
            maxSpeed = (user.speedMbps * 1.5f).coerceAtLeast(60f),
            stage = speedStage,
            downloadMbps = downloadMbps,
            uploadMbps = uploadMbps,
            pingMs = pingMs,
            jitterMs = jitterMs,
            isTesting = isTestingSpeed,
            onStartTest = onStartSpeedTest
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Optical Fiber Quality Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardNavy),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "TEN STAR NET FIBER TELEMETRY",
                    color = StarCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Allocated Profile Speed:", color = TextSecondary, fontSize = 12.sp)
                    Text(text = "${user.speedMbps} Mbps Dedicated", color = StarGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Core Gateway Server:", color = TextSecondary, fontSize = 12.sp)
                    Text(text = "Ten Star Net NOC (Dhaka)", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "RouterOS Queue Status:", color = TextSecondary, fontSize = 12.sp)
                    Text(text = "Synchronized & Optimal", color = StatusGreen, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun UserAiAssistantTab(
    user: User,
    messages: List<ChatMessage>,
    isThinking: Boolean,
    onSendMessage: (String) -> Unit
) {
    var promptInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
    ) {
        // AI Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(CardNavy)
                .border(1.dp, BorderNavy, RoundedCornerShape(14.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(StarCyan.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = StarCyan,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "Ten Star Net AI Support",
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Powered by Gemini • Resolves billing & optical line issues",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Quick Suggestion Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val suggestions = listOf(
                "Why is line disabled?",
                "How to pay bill?",
                "Red LOS light help",
                "Restart ONU"
            )
            suggestions.forEach { chipText ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceNavy)
                        .border(1.dp, BorderNavy, RoundedCornerShape(8.dp))
                        .clickable { onSendMessage(chipText) }
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                    Text(text = chipText, color = StarCyan, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Chat Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { msg ->
                val isUser = msg.sender == "USER"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 14.dp,
                                    topEnd = 14.dp,
                                    bottomStart = if (isUser) 14.dp else 2.dp,
                                    bottomEnd = if (isUser) 2.dp else 14.dp
                                )
                            )
                            .background(if (isUser) StarCyanDark else CardNavy)
                            .border(1.dp, if (isUser) StarCyan else BorderNavy, RoundedCornerShape(14.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = msg.message,
                            color = if (isUser) DeepNavy else TextPrimary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            fontWeight = if (isUser) FontWeight.Medium else FontWeight.Normal
                        )
                    }
                }
            }

            if (isThinking) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(CardNavy)
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(14.dp), color = StarCyan, strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Ten Star AI is thinking...", color = TextSecondary, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Prompt Input
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = promptInput,
                onValueChange = { promptInput = it },
                placeholder = { Text("Ask about bill, speed, or connection...", fontSize = 12.sp) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("ai_prompt_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = StarCyan,
                    unfocusedBorderColor = BorderNavy,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (promptInput.isNotBlank()) {
                        onSendMessage(promptInput)
                        promptInput = ""
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(StarCyan)
                    .testTag("send_ai_prompt_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = DeepNavy,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun UserBillsTab(
    user: User,
    payments: List<com.example.data.model.PaymentTransaction>,
    onOpenPayment: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "BILLING & PAYMENT HISTORY",
                color = StarCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Button(
                onClick = onOpenPayment,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = StarCyan, contentColor = DeepNavy),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(text = "Pay Now", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (payments.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "No online payment records yet", color = TextSecondary, fontSize = 13.sp)
                    Text(text = "Pay your bill via bKash/Nagad to see TrxID records here.", color = TextMuted, fontSize = 11.sp)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(payments) { item ->
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
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (item.paymentMethod == "bKash") BkashPink else NagadOrange)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(text = item.paymentMethod, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = "TrxID: ${item.trxId}", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                                Text(text = "৳ ${item.amount.toInt()}", color = StarCyan, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Sender: ${item.senderNumber}", color = TextSecondary, fontSize = 11.sp)
                                Text(text = "Status: ${item.status}", color = StatusGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // MikroTik Trigger Confirmation
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(SurfaceNavy)
                                    .padding(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.Default.Bolt, contentDescription = null, tint = StarGold, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "MikroTik RouterOS: ${item.mikrotikResponse}",
                                    color = TextSecondary,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentBottomSheet(
    user: User,
    ispSettings: com.example.data.model.IspSettings?,
    onDismiss: () -> Unit,
    onSubmitPayment: (method: String, trxId: String, senderNumber: String) -> Unit
) {
    var selectedMethod by remember { mutableStateOf("bKash") }
    var senderNumber by remember { mutableStateOf("") }
    var trxId by remember { mutableStateOf("") }
    var formError by remember { mutableStateOf<String?>(null) }
    val clipboardManager = LocalClipboardManager.current

    val activeBkash = ispSettings?.bkashNumber ?: "01712-345678"
    val activeNagad = ispSettings?.nagadNumber ?: "01812-987654"

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = CardNavy
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Pay Ten Star Net Monthly Bill",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Automated MikroTik line activation upon TrxID confirmation",
                color = StarCyan,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Bill Summary Banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceNavy)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = user.packageName, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Customer: ${user.username}", color = TextSecondary, fontSize = 11.sp)
                }
                Text(text = "৳ ${user.monthlyBill.toInt()}", color = StarGold, fontSize = 20.sp, fontWeight = FontWeight.Black)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "1. Select Payment Method:", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))

            // Payment method selector
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                // bKash
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (selectedMethod == "bKash") BkashPink.copy(alpha = 0.25f) else SurfaceNavy)
                        .border(1.5.dp, if (selectedMethod == "bKash") BkashPink else BorderNavy, RoundedCornerShape(12.dp))
                        .clickable { selectedMethod = "bKash" }
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "bKash Personal", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = activeBkash, color = StarCyan, fontSize = 11.sp)
                    }
                }

                // Nagad
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (selectedMethod == "Nagad") NagadOrange.copy(alpha = 0.25f) else SurfaceNavy)
                        .border(1.5.dp, if (selectedMethod == "Nagad") NagadOrange else BorderNavy, RoundedCornerShape(12.dp))
                        .clickable { selectedMethod = "Nagad" }
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Nagad Personal", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = activeNagad, color = StarGold, fontSize = 11.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Copy Number Action
            val targetNumber = if (selectedMethod == "bKash") activeBkash else activeNagad
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(SurfaceNavy)
                    .clickable {
                        clipboardManager.setText(AnnotatedString(targetNumber))
                    }
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Send Money to: $targetNumber", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = StarCyan, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Copy", color = StarCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "2. Enter Payment Details:", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = senderNumber,
                onValueChange = { senderNumber = it },
                label = { Text("Your Sender Mobile Number") },
                placeholder = { Text("01XXXXXXXXX") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("sender_number_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = StarCyan,
                    unfocusedBorderColor = BorderNavy,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = trxId,
                onValueChange = { trxId = it },
                label = { Text("Transaction ID (TrxID)") },
                placeholder = { Text("e.g. 9J8B2KL99") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("trx_id_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = StarCyan,
                    unfocusedBorderColor = BorderNavy,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )

            formError?.let { err ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = err, color = StatusRed, fontSize = 11.sp)
            }

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = {
                    if (senderNumber.isBlank() || trxId.isBlank()) {
                        formError = "Please enter your sender phone number and TrxID"
                    } else {
                        formError = null
                        onSubmitPayment(selectedMethod, trxId.trim(), senderNumber.trim())
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("confirm_bill_payment_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = StarCyan,
                    contentColor = DeepNavy
                )
            ) {
                Icon(imageVector = Icons.Default.Bolt, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Confirm Payment & Reactivate Line", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
