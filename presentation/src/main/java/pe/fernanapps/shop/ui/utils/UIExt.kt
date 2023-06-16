package pe.fernanapps.shop.ui.utils

import androidx.annotation.DimenRes
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp

class UIExt {
}


// Compose Ext for Padding :V
fun Modifier.paddingRes(
    @DimenRes id: Int,
) = composed { this.padding(fixPadding(id)) }

fun Modifier.paddingRes(
    @DimenRes horizontal: Int = 0,
    @DimenRes vertical: Int = 0,
) = composed {
    this.padding(
        horizontal = fixPadding(horizontal),
        vertical = fixPadding(vertical)
    )
}

fun Modifier.paddingRes(
    @DimenRes start: Int = 0,
    @DimenRes top: Int = 0,
    @DimenRes end: Int = 0,
    @DimenRes bottom: Int = 0,
) = composed { this.padding(
    start = fixPadding(start),
    top = fixPadding(top),
    end = fixPadding(end),
    bottom = fixPadding(bottom)
) }


@Composable
fun fixPadding(@DimenRes value: Int) = if (value == 0) 0.dp else dimensionResource(value)

