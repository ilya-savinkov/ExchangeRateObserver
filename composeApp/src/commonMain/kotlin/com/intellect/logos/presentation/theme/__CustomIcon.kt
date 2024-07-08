package com.intellect.logos.presentation.theme

import androidx.compose.ui.graphics.vector.ImageVector
import com.intellect.logos.presentation.theme.customicon.Backspace
import kotlin.collections.List as ____KtList

public object CustomIcon

private var __CustomIcons: ____KtList<ImageVector>? = null

public val CustomIcon.CustomIcons: ____KtList<ImageVector>
  get() {
    if (__CustomIcons != null) {
      return __CustomIcons!!
    }
    __CustomIcons= listOf(Backspace)
    return __CustomIcons!!
  }
