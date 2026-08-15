package com.example.scamshieldai.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scamshieldai.ui.theme.Cerulean
import com.example.scamshieldai.ui.theme.PrussianBlue

@Composable
fun AnimatedNavBar(
    items: List<NavigationItemData>,
    currentRoute: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedIndex = items.indexOfFirst { it.route == currentRoute }.coerceAtLeast(0)
    val animatedIndex by animateFloatAsState(
        targetValue = selectedIndex.toFloat(),
        animationSpec = spring(dampingRatio = 0.75f, stiffness = Spring.StiffnessLow),
        label = "indexAnimation"
    )

    var width by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(100.dp)
            .onGloballyPositioned { width = it.size.width },
        contentAlignment = Alignment.BottomCenter
    ) {
        // Background with curve
        val backgroundShape = remember(animatedIndex, items.size) {
            GenericShape { size, _ ->
                val itemWidth = size.width / items.size
                val centerCircle = (animatedIndex * itemWidth) + (itemWidth / 2f)
                val radius = 45.dp.value * density.density
                
                moveTo(0f, 0f)
                // Left part
                lineTo(centerCircle - radius * 1.5f, 0f)
                
                // Curve up (Bulge)
                cubicTo(
                    x1 = centerCircle - radius * 0.8f, y1 = 0f,
                    x2 = centerCircle - radius * 0.8f, y2 = -radius * 0.8f,
                    x3 = centerCircle, y3 = -radius * 0.8f
                )
                cubicTo(
                    x1 = centerCircle + radius * 0.8f, y1 = -radius * 0.8f,
                    x2 = centerCircle + radius * 0.8f, y2 = 0f,
                    x3 = centerCircle + radius * 1.5f, y3 = 0f
                )
                
                // Right part
                lineTo(size.width, 0f)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .shadow(12.dp, RoundedCornerShape(24.dp))
                .background(Color.White, RoundedCornerShape(24.dp))
        )

        // Floating Circle
        val itemWidthDp = with(density) { (width / items.size).toDp() }
        val circleOffset by animateFloatAsState(
            targetValue = (selectedIndex * (width / items.size).toFloat()) + (width / items.size / 2f),
            animationSpec = spring(dampingRatio = 0.75f, stiffness = Spring.StiffnessLow),
            label = "circleOffset"
        )

        Box(
            modifier = Modifier
                .offset(
                    x = with(density) { (circleOffset).toDp() } - 30.dp,
                    y = (-40).dp
                )
                .size(60.dp)
                .shadow(8.dp, CircleShape)
                .background(Cerulean, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = items[selectedIndex].selectedIcon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }

        // Icons and Labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = index == selectedIndex
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onItemClick(item.route) },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.offset(y = if (isSelected) 15.dp else 0.dp)
                    ) {
                        if (!isSelected) {
                            Icon(
                                imageVector = item.unselectedIcon,
                                contentDescription = item.title,
                                tint = Color.Gray.copy(alpha = 0.6f),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.title,
                            color = if (isSelected) Cerulean else Color.Gray.copy(alpha = 0.6f),
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}

// Reuse or copy NavigationItemData if needed, but it's already in FloatingNavBar.kt
// For now I'll use the one from FloatingNavBar.kt
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.scamshieldai.ui.components.NavigationItemData
