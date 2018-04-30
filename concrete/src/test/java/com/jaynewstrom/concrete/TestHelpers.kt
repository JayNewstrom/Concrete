package com.jaynewstrom.concrete

import org.mockito.Mockito
import org.mockito.Mockito.mock

fun <T> kotlinAny(): T {
    Mockito.any<T>()
    return uninitialized()
}

@Suppress("UNCHECKED_CAST") // Hacks on hacks!
private fun <T> uninitialized(): T = null as T

inline fun <reified T> kotlinMock(): T {
    return mock(T::class.java)
}
