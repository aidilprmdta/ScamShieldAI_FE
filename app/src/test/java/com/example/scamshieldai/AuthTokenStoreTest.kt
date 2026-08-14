package com.example.scamshieldai

import com.example.scamshieldai.auth.AuthTokenStore
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AuthTokenStoreTest {

    @Before
    fun setup() {
        AuthTokenStore.persistHandler = null
        AuthTokenStore.clear()
    }

    @Test
    fun `setToken stores id and refresh token`() {
        AuthTokenStore.setToken("id_123", "refresh_456")
        assertEquals("id_123", AuthTokenStore.idToken)
        assertEquals("refresh_456", AuthTokenStore.refreshToken)
    }

    @Test
    fun `setToken without refresh keeps existing refresh`() {
        AuthTokenStore.setToken("id_1", "refresh_1")
        AuthTokenStore.setToken("id_2")
        assertEquals("id_2", AuthTokenStore.idToken)
        assertEquals("refresh_1", AuthTokenStore.refreshToken)
    }

    @Test
    fun `clear removes all tokens`() {
        AuthTokenStore.setToken("id_123", "refresh_456")
        AuthTokenStore.clear()
        assertNull(AuthTokenStore.idToken)
        assertNull(AuthTokenStore.refreshToken)
    }

    @Test
    fun `initial state is null`() {
        assertNull(AuthTokenStore.idToken)
        assertNull(AuthTokenStore.refreshToken)
    }

    @Test
    fun `restore loads tokens without requiring persist`() {
        AuthTokenStore.restore("saved_id", "saved_refresh")
        assertEquals("saved_id", AuthTokenStore.idToken)
        assertEquals("saved_refresh", AuthTokenStore.refreshToken)
    }
}
