package com.example.scamshieldai

import com.example.scamshieldai.network.*
import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Test

class ApiModelsTest {

    private val gson = Gson()

    @Test
    fun `AuthTokens deserializes correctly`() {
        val json = """{"idToken":"abc","refreshToken":"def","localId":"uid1","email":"a@b.com"}"""
        val tokens = gson.fromJson(json, AuthTokens::class.java)
        assertEquals("abc", tokens.idToken)
        assertEquals("def", tokens.refreshToken)
        assertEquals("uid1", tokens.localId)
        assertEquals("a@b.com", tokens.email)
    }

    @Test
    fun `AnalysisResult deserializes snake_case fields`() {
        val json = """{
            "scan_id": "s1",
            "type": "chat",
            "input_summary": "test",
            "risk_score": 75,
            "risk_level": "high",
            "explanation": "dangerous",
            "red_flags": [{"label": "flag1", "detail": "d1"}],
            "recommendation": "block",
            "recommendation_text": "Block it",
            "related_education_category": "Phishing",
            "link_reputation": null,
            "created_at": "2026-01-01"
        }"""
        val result = gson.fromJson(json, AnalysisResult::class.java)
        assertEquals("s1", result.scanId)
        assertEquals(75, result.riskScore)
        assertEquals("high", result.riskLevel)
        assertEquals(1, result.redFlags?.size)
        assertEquals("flag1", result.redFlags?.first()?.label)
        assertEquals("Phishing", result.relatedEducationCategory)
    }

    @Test
    fun `AuthRefreshRequest serializes to snake_case`() {
        val request = AuthRefreshRequest(refreshToken = "token123")
        val json = gson.toJson(request)
        assertTrue(json.contains("refresh_token"))
        assertTrue(json.contains("token123"))
    }

    @Test
    fun `RegisterFcmTokenRequest serializes correctly`() {
        val request = RegisterFcmTokenRequest(fcmToken = "fcm_abc")
        val json = gson.toJson(request)
        assertTrue(json.contains("fcm_token"))
        assertTrue(json.contains("fcm_abc"))
    }
}
