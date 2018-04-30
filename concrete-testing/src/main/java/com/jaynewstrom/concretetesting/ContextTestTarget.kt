package com.jaynewstrom.concretetesting

import javax.inject.Inject

internal class ContextTestTarget {
    @Inject lateinit var injectedString: String
}
