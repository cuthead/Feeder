package com.nononsenseapps.feeder.ui.compose.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.google.accompanist.coil.rememberCoilPainter
import com.nononsenseapps.feeder.db.room.ID_UNSET
import com.nononsenseapps.feeder.model.PreviewItem
import com.nononsenseapps.feeder.ui.compose.minimumTouchSize
import com.nononsenseapps.feeder.ui.compose.theme.FeedListItemDateStyle
import com.nononsenseapps.feeder.ui.compose.theme.FeedListItemFeedTitleStyle
import com.nononsenseapps.feeder.ui.compose.theme.FeedListItemStyle
import com.nononsenseapps.feeder.ui.compose.theme.FeedListItemTitleStyle
import com.nononsenseapps.feeder.ui.compose.theme.FeederTheme
import com.nononsenseapps.feeder.ui.compose.theme.keyline1Padding
import org.kodein.di.compose.instance
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import java.util.*

private val shortDateTimeFormat: DateTimeFormatter =
    DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.getDefault())

@Composable
fun FeedItemPreview(item: FeedListItem, onItemClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clickable {
                onItemClick()
            }
            .padding(
                start = keyline1Padding,
                end = if (item.imageUrl?.isNotBlank() != true) keyline1Padding else 0.dp
            )
            .height(IntrinsicSize.Min)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .weight(weight = 1.0f, fill = true)
                .requiredHeightIn(min = minimumTouchSize)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = item.feedTitle,
                        style = FeedListItemFeedTitleStyle(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(weight = 1.0f, fill = true)
                            .padding(top = 2.dp)
                    )
                    Text(
                        text = item.pubDate?.toLocalDate()?.format(shortDateTimeFormat)
                            ?: "",
                        style = FeedListItemDateStyle(),
                        maxLines = 1,
                        modifier = Modifier
                            .padding(top = 2.dp, start = 4.dp)
                    )
                }
            }

            val alpha = if (item.unread) {
                ContentAlpha.high
            } else {
                ContentAlpha.disabled
            }
            CompositionLocalProvider(LocalContentAlpha provides alpha) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            FeedListItemTitleStyle()
                        ) {
                            append(item.title)
                        }
                        append(" — ${item.snippet}…")
                    },
                    style = FeedListItemStyle(),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 5,
                    modifier = Modifier
                        .padding(bottom = 2.dp)
                )
            }

        }

        item.imageUrl?.let { imageUrl ->
            val imageLoader: ImageLoader by instance()

            Image(
                painter = rememberCoilPainter(
                    request = imageUrl,
                    imageLoader = imageLoader,
                    shouldRefetchOnSizeChange = { _, _ -> false },
                ),
                contentScale = ContentScale.Crop,
                contentDescription = "Thumbnail for the article",
                modifier = Modifier
                    .width(64.dp)
                    .fillMaxHeight()
                    .padding(start = 4.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun preview() {
    FeedItemPreview(
        item = FeedListItem(
            title = "title",
            snippet = "snippet which is quite long as you might expect from a snipper of a story. It keeps going and going and going and going and going and going and going and going and going and going and going and going and going and going and going and going and going and going and going and going and going and going and going and going and going and going and going and going and going and going and going and going and going and going and snowing",
            feedTitle = "Super Duper Feed One two three hup di too dasf",
            pubDate = null,
            unread = true,
            imageUrl = null,
            id = ID_UNSET
        )
    ) {

    }
}

@Immutable
data class FeedListItem(
    val id: Long,
    val title: String,
    val snippet: String,
    val feedTitle: String,
    val unread: Boolean,
    val pubDate: ZonedDateTime?,
    val imageUrl: String?,
)
