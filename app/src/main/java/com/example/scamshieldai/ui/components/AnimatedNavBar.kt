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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scamshieldai.ui.theme.Cerulean

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
            .background(Color.White)
            .onGloballyPositioned { width = it.size.width },
        contentAlignment = Alignment.BottomStart
    ) {
        // Background with bulge (higher curve to match floating circle)
        val backgroundShape = remember(animatedIndex, items.size) {
            GenericShape { size, _ ->
                val itemWidth = size.width / items.size
                val centerCircle = (animatedIndex * itemWidth) + (itemWidth / 2f)
                val radius = 45.dp.value * density.density
                
                moveTo(0f, 0f)
                lineTo(centerCircle - radius * 1.5f, 0f)
                
                cubicTo(
                    x1 = centerCircle - radius * 0.8f, y1 = 0f,
                    x2 = centerCircle - radius * 0.9f, y2 = -radius * 0.75f,
                    x3 = centerCircle, y3 = -radius * 0.75f
                )
                cubicTo(
                    x1 = centerCircle + radius * 0.9f, y1 = -radius * 0.75f,
                    x2 = centerCircle + radius * 0.8f, y2 = 0f,
                    x3 = centerCircle + radius * 1.5f, y3 = 0f
                )
                
                lineTo(size.width, 0f)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }
        }

        // Base background (docked style)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .shadow(16.dp, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(Color.White, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
        )

        // The Bulge overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color.White, backgroundShape)
        )

        // Floating Circle Position (Higher elevation to ensure it's above text labels)
        val itemWidthFloat = if (width > 0) width.toFloat() / items.size else 0f
        val circleX = (animatedIndex * itemWidthFloat) + (itemWidthFloat / 2f)

        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = (circleX - with(density) { 30.dp.toPx() }).toInt(),
                        y = with(density) { (-64).dp.toPx() }.toInt()
                    )
                }
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
            if (items[selectedIndex].badgeCount > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-4).dp, y = 4.dp)
                        .size(18.dp)
                        .background(Color.Red, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (items[selectedIndex].badgeCount > 99) "99+" else items[selectedIndex].badgeCount.toString(),
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Icons and Labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .height(72.dp),
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
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = item.unselectedIcon,
                                    contentDescription = item.title,
                                    tint = Color.Gray.copy(alpha = 0.6f),
                                    modifier = Modifier.size(24.dp)
                                )
                                if (item.badgeCount > 0) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .offset(x = 8.dp, y = (-8).dp)
                                            .size(16.dp)
                                            .background(Color.Red, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (item.badgeCount > 99) "99+" else item.badgeCount.toString(),
                                            color = Color.White,
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
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
