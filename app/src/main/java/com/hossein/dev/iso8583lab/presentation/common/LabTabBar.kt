package com.hossein.dev.iso8583lab.presentation.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.hossein.dev.iso8583lab.ui.theme.ISO8583LabTheme
import com.hossein.dev.iso8583lab.ui.theme.Primary

@Composable
fun LabTabBar(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    containerColor: Color,
    modifier: Modifier = Modifier
) {
    SecondaryTabRow(selectedTabIndex = selectedIndex, containerColor = containerColor, modifier = modifier) {
        tabs.forEachIndexed { index, title ->
            val selected = index == selectedIndex

            Tab(
                selected = selected,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = if (selected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondary
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun LabTabBarPreview() {
    ISO8583LabTheme {
        LabTabBar(
            tabs = listOf("Message Builder", "Message Parser"),
            selectedIndex = 0,
            onTabSelected = {},
            containerColor = Primary,
        )
    }
}