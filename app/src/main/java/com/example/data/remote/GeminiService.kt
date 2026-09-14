package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun getAiHelpResponse(userPrompt: String, userContextInfo: String): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
                
                val systemPrompt = """
                    You are Ten Star Net AI Customer Support Assistant, working for Internet Service Provider 'Ten Star Net' owned and managed by Md Samiul Alim.
                    Customer Info: $userContextInfo
                    Company Policy:
                    - ISP Name: Ten Star Net
                    - Founder / Master Admin: Md Samiul Alim
                    - Payment Methods: bKash and Nagad numbers available in user billing dashboard.
                    - Instant Activation: As soon as the customer pays their bill and enters TrxID, the MikroTik RouterOS automation immediately enables their internet line.
                    - Technical Support:
                      * Red LOS light on ONU indicates optical fiber line cut or high attenuation. Ask user to check yellow fiber patch cord or contact NOC hotline.
                      * Green PON light steady indicates optical link is OK.
                      * Router restart (reboot 30s) fixes 90% of DNS cache or IP lease issues.
                    Answer kindly, concisely, professionally, and clearly. You may reply in English or Bengali (Bangla) depending on what the user asks.
                """.trimIndent()

                val jsonBody = JSONObject().apply {
                    val contentsArr = JSONArray().apply {
                        put(JSONObject().apply {
                            put("role", "user")
                            put("parts", JSONArray().apply {
                                put(JSONObject().put("text", "$systemPrompt\n\nCustomer question: $userPrompt"))
                            })
                        })
                    }
                    put("contents", contentsArr)
                }

                val request = Request.Builder()
                    .url(url)
                    .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                    .build()

                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseStr = response.body?.string() ?: ""
                    val rootJson = JSONObject(responseStr)
                    val candidates = rootJson.optJSONArray("candidates")
                    if (candidates != null && candidates.length() > 0) {
                        val firstCand = candidates.getJSONObject(0)
                        val content = firstCand.optJSONObject("content")
                        val parts = content?.optJSONArray("parts")
                        if (parts != null && parts.length() > 0) {
                            val text = parts.getJSONObject(0).optString("text")
                            if (text.isNotBlank()) return@withContext text.trim()
                        }
                    }
                } else {
                    Log.w("GeminiService", "API call failed with code: ${response.code}")
                }
            } catch (e: Exception) {
                Log.e("GeminiService", "Error invoking Gemini API", e)
            }
        }

        // Intelligent Domain-Specific Fallback ISP Knowledge Engine
        return@withContext getDomainSpecificFaq(userPrompt, userContextInfo)
    }

    private fun getDomainSpecificFaq(query: String, userContext: String): String {
        val q = query.lowercase()
        return when {
            q.contains("pay") || q.contains("bill") || q.contains("বিল") || q.contains("টাকা") || q.contains("bkash") || q.contains("nagad") -> {
                "To pay your Ten Star Net bill:\n" +
                "1. Check the active bKash or Nagad number in your Dashboard.\n" +
                "2. Send Money / Make Payment from your bKash or Nagad app.\n" +
                "3. Copy the TrxID (Transaction ID).\n" +
                "4. Tap 'Pay Bill' in the app and submit the TrxID.\n" +
                "⚡ Once submitted, our MikroTik Router automation automatically turns on your internet line within 5 seconds! If you need help, contact Md Samiul Alim directly."
            }
            q.contains("red") || q.contains("los") || q.contains("light") || q.contains("লাল") || q.contains("আলো") -> {
                "⚠️ Red LOS light on your optical ONU/Router:\n" +
                "- The red light means no optical fiber signal is reaching your ONU.\n" +
                "- Please ensure the thin yellow optical fiber cable is plugged tightly into the bottom port.\n" +
                "- Do NOT sharply bend the yellow cable.\n" +
                "- If red light continues to blink, optical fiber line may be cut outside. Contact Md Samiul Alim at Ten Star Net NOC immediately for line splicing."
            }
            q.contains("slow") || q.contains("speed") || q.contains("গতি") || q.contains("প্যাকেজ") || q.contains("test") -> {
                "📶 Slow internet troubleshooting:\n" +
                "1. Go to the 'Speed Test' tab in Ten Star Net app to measure live upload/download speed.\n" +
                "2. Turn off your router for 30 seconds, then turn it back on.\n" +
                "3. Ensure multiple devices aren't running heavy background downloads or torrents.\n" +
                "4. If you need higher bandwidth, contact Admin Md Samiul Alim to instantly upgrade your package!"
            }
            q.contains("disabled") || q.contains("inactive") || q.contains("বন্ধ") || q.contains("connect") -> {
                "Your line might be temporarily disabled due to bill due or billing cycle expiry. \n" +
                "👉 Please pay your bill via bKash/Nagad from the dashboard. Our MikroTik RouterOS automation will reactivate your PPPoE secret immediately upon payment confirmation!"
            }
            q.contains("admin") || q.contains("owner") || q.contains("contact") || q.contains("samiul") || q.contains("ফোন") -> {
                "Ten Star Net is proudly maintained by Internet Service Provider Md Samiul Alim.\n" +
                "📞 Support Hotline: 01712-345678\n" +
                "🏢 Office: Ten Star Net NOC Center\n" +
                "24/7 dedicated fiber optic broadband support."
            }
            else -> {
                "Hello! I am Ten Star Net AI Support. How can I help you today?\n" +
                "• Ask me about bill payments and bKash/Nagad TrxID\n" +
                "• Ask why ONU red LOS light is blinking\n" +
                "• Ask how to run speed test or upgrade packages\n" +
                "• You can also reach our ISP administrator Md Samiul Alim for manual assistance."
            }
        }
    }
}
