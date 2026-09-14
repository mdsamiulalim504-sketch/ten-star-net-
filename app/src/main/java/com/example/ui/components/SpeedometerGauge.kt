package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderNavy
import com.example.ui.theme.CardNavy
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.StarCyan
import com.example.ui.theme.StarCyanDark
import com.example.ui.theme.StarGold
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.SurfaceNavy
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlin.math.cos
import kotlin.math.sin

enum class SpeedTestStage {
    IDLE, PING, DOWNLOAD, UPLOAD, FINISHED
}

@Composable
fun SpeedometerGauge(
    currentSpeed: Float,
    maxSpeed: Float = 100f,
    stage: SpeedTestStage,
    downloadMbps: Double,
    uploadMbps: Double,
    pingMs: Int,
    jitterMs: Int,
    isTesting: Boolean,
    onStartTest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedSpeed by animateFloatAsState(
        targetValue = currentSpeed,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "SpeedAnimation"
    )

    // Gauge angle spans from 140 deg to 400 deg (sweep of 260 deg)
    val startAngle = 140f
    val sweepAngle = 260f
    val progress = (animatedSpeed / maxSpeed).coerceIn(0f, 1f)
    val currentAngle = startAngle + (sweepAngle * progress)

    // Infinite breathing glow during test
    val infiniteTransition = rememberInfiniteTransition(label = "Glow")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Pulse"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(CardNavy)
            .border(1.dp, BorderNavy, RoundedCornerShape(24.dp))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.NetworkCheck,
                    contentDescription = "Speed Meter",
                    tint = StarCyan,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "LIVE INTERNET SPEED METER",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    letterSpacing = 1.sp
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceNavy)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = when (stage) {
                        SpeedTestStage.IDLE -> "READY"
                        SpeedTestStage.PING -> "TESTING PING..."
                        SpeedTestStage.DOWNLOAD -> "TESTING DOWNLOAD..."
                        SpeedTestStage.UPLOAD -> "TESTING UPLOAD..."
                        SpeedTestStage.FINISHED -> "TEST COMPLETE"
                    },
                    color = if (isTesting) StarCyan else TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Circular Gauge Canvas
        Box(
            modifier = Modifier
                .size(240.dp)
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(220.dp)) {
                val strokeWidth = 14.dp.toPx()
                val radius = (size.minDimension - strokeWidth) / 2
                val center = Offset(size.width / 2, size.height / 2)

                // Background Track
                drawArc(
                    color = BorderNavy.copy(alpha = 0.5f),
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // Active Progress Gradient Arc
                if (progress > 0.01f) {
                    drawArc(
                        brush = Brush.sweepGradient(
                            0.0f to StarCyanDark,
                            0.5f to StarCyan,
                            1.0f to StarGold
                        ),
                        startAngle = startAngle,
                        sweepAngle = sweepAngle * progress,
                        useCenter = false,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2, radius * 2),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                // Needle Tip Indicator
                val angleRad = Math.toRadians(currentAngle.toDouble())
                val needleLength = radius - 8.dp.toPx()
                val needleEnd = Offset(
                    (center.x + needleLength * cos(angleRad)).toFloat(),
                    (center.y + needleLength * sin(angleRad)).toFloat()
                )

                // Draw needle line
                drawLine(
                    brush = Brush.linearGradient(listOf(StarGold, StarCyan)),
                    start = center,
                    end = needleEnd,
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )

                // Center Pivot Dot
                drawCircle(
                    color = StarCyan,
                    radius = 8.dp.toPx(),
                    center = center
                )
                drawCircle(
                    color = DeepNavy,
                    radius = 4.dp.toPx(),
                    center = center
                )
            }

            // Center Digital Speed Display
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = String.format(java.util.Locale.US, "%.1f", animatedSpeed),
                    color = TextPrimary,
                    fontSize = 38.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.testTag("speed_meter_value")
                )
                Text(
                    text = "Mbps",
                    color = StarCyan,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (stage == SpeedTestStage.UPLOAD) "Upload" else "Download",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Metrics Grid (Download, Upload, Ping, Jitter)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceNavy)
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // Download
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ArrowDownward,
                        contentDescription = "Download",
                        tint = StarCyan,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Download", color = TextSecondary, fontSize = 11.sp)
                }
                Text(
                    text = "${String.format(java.util.Locale.US, "%.1f", downloadMbps)} Mbps",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            // Upload
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = "Upload",
                        tint = StarGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Upload", color = TextSecondary, fontSize = 11.sp)
                }
                Text(
                    text = "${String.format(java.util.Locale.US, "%.1f", uploadMbps)} Mbps",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            // Ping
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Ping", color = TextSecondary, fontSize = 11.sp)
                Text(
                    text = "$pingMs ms",
                    color = StatusGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            // Jitter
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Jitter", color = TextSecondary, fontSize = 11.sp)
                Text(
                    text = "$jitterMs ms",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Start / Retest Button
        Button(
            onClick = onStartTest,
            enabled = !isTesting,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("start_speed_test_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = StarCyan,
                contentColor = Color(0xFF070D1E),
                disabledContainerColor = BorderNavy,
                disabledContentColor = TextMuted
            )
        ) {
            if (isTesting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = StarCyan,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Testing Live Bandwidth...",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (stage == SpeedTestStage.FINISHED) "Run Speed Test Again" else "Start Live Speed Test",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
