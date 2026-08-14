package com.example.scamshieldai

import com.example.scamshieldai.auth.JwtTokens
import org.junit.Assert.*
import org.junit.Test
import java.util.Base64

class JwtTokensTest {

    private fun fakeJwt(expEpochSeconds: Long): String {
        val header = Base64.getUrlEncoder().withoutPadding()
            .encodeToString("""{"alg":"none"}""".toByteArray())
        val payload = Base64.getUrlEncoder().withoutPadding()
            .encodeToString("""{"exp":$expEpochSeconds,"sub":"uid1"}""".toByteArray())
        return "$header.$payload.sig"
    }

    @Test
    fun `expired token is detected`() {
        val token = fakeJwt(1_700_000_000L)
        assertTrue(JwtTokens.isExpired(token, skewSeconds = 0))
    }

    @Test
    fun `future token is not expired`() {
        val token = fakeJwt((System.currentTimeMillis() / 1000) + 3600)
        assertFalse(JwtTokens.isExpired(token, skewSeconds = 60))
    }

    @Test
    fun `blank or malformed token is expired`() {
        assertTrue(JwtTokens.isExpired(""))
        assertTrue(JwtTokens.isExpired("not-a-jwt"))
        assertTrue(JwtTokens.isExpired("only.two"))
    }

    @Test
    fun `expiry is parsed from payload`() {
        val token = fakeJwt(1_800_000_000L)
        assertEquals(1_800_000_000L, JwtTokens.expiryEpochSeconds(token))
    }
}
