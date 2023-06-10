package com.example.travelbook.shared

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UIText {
    data class DynamicString(
        val string: String
    ): UIText()

    class ResourceString(
        @StringRes val resID: Int,
        vararg val args: Any
    ): UIText()

    @Composable
    fun getString(): String {
        return when(this) {
            is DynamicString -> string
            is ResourceString -> stringResource(resID, *args)
        }
    }
}