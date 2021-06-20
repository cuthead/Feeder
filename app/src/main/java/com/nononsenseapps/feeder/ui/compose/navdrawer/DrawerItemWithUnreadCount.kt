package com.nononsenseapps.feeder.ui.compose.navdrawer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.res.stringResource
import com.nononsenseapps.feeder.R
import com.nononsenseapps.feeder.db.room.ID_ALL_FEEDS

@Immutable
sealed class DrawerItemWithUnreadCount(
    open val title: @Composable () -> String,
    open val unreadCount: Int
) : Comparable<DrawerItemWithUnreadCount> {
    val uiId: Long
        get() = when (this) {
            is DrawerFeed -> id
            is DrawerTag -> tag.hashCode().toLong()
            is DrawerTop -> ID_ALL_FEEDS
        }

    override fun compareTo(other: DrawerItemWithUnreadCount): Int = when (this) {
        is DrawerFeed -> {
            when (other) {
                is DrawerFeed -> when {
                    tag.equals(other.tag, ignoreCase = true) -> displayTitle.compareTo(
                        other.displayTitle,
                        ignoreCase = true
                    )
                    tag.isEmpty() -> 1
                    other.tag.isEmpty() -> -1
                    else -> tag.compareTo(other.tag, ignoreCase = true)
                }
                is DrawerTag -> when {
                    tag.isEmpty() -> 1
                    tag.equals(other.tag, ignoreCase = true) -> 1
                    else -> tag.compareTo(other.tag, ignoreCase = true)
                }
                is DrawerTop -> 1
            }
        }
        is DrawerTag -> {
            when (other) {
                is DrawerFeed -> when {
                    other.tag.isEmpty() -> -1
                    tag.equals(other.tag, ignoreCase = true) -> -1
                    else -> tag.compareTo(other.tag, ignoreCase = true)
                }
                is DrawerTag -> tag.compareTo(other.tag, ignoreCase = true)
                is DrawerTop -> 1
            }
        }
        is DrawerTop -> -1
    }
}

@Immutable
data class DrawerTop(
    override val title: @Composable () -> String = { stringResource(id = R.string.all_feeds) },
    override val unreadCount: Int
) : DrawerItemWithUnreadCount(title, unreadCount)

@Immutable
data class DrawerFeed(
    val id: Long,
    val tag: String,
    val displayTitle: String,
    override val unreadCount: Int
) : DrawerItemWithUnreadCount(title = { displayTitle }, unreadCount)

@Immutable
data class DrawerTag(
    val tag: String,
    override val unreadCount: Int
) : DrawerItemWithUnreadCount(title = { tag }, unreadCount)
