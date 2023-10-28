package com.beworld.pokemoninfocompose.presentation.camera.components

import android.provider.ContactsContract.Contacts.Photo
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SignalWifiBad
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.beworld.pokemoninfocompose.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotoListItem(
    photo: com.beworld.task1.data.local.photo_provider.Photo,
    imageBitmap: ImageBitmap?,
    isError: Boolean,
    convert: () -> Unit,
    onItemClick: () -> Unit,
    dialogState: MutableState<Boolean>,
    photoForRemove: MutableState<com.beworld.task1.data.local.photo_provider.Photo>,
    modifier: Modifier = Modifier
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    Row(
        modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onItemClick,
                onLongClick = { isMenuExpanded = true }
            )
    ) {
        Box(
            Modifier
                .padding(horizontal = dimensionResource(id = R.dimen.medium_padding))
                .size(96.dp)
        ) {
            if (isError) {
                Image(
                    imageVector = Icons.Filled.SignalWifiBad,
                    contentDescription = "Bad Internet connection",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp)
                )
            } else if (imageBitmap == null) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            } else {
                Image(
                    bitmap = imageBitmap,
                    contentDescription = "Photo ${photo.name}",
                    contentScale = ContentScale.Crop
                )
            }
        }
        Column(
            verticalArrangement = Arrangement
                .spacedBy(dimensionResource(id = R.dimen.low_padding)),
            modifier = Modifier
                .align(CenterVertically)
        ) {
            Text(text = photo.name)
            Text(text = "Date: ${photo.date}")
        }

        if (isMenuExpanded) {
            photoForRemove.value = photo
            RemoveMenu(
                onDismiss = { isMenuExpanded = false },
                dialogVisible = dialogState,
                expanded = isMenuExpanded,
                convert = convert
            )
        }
    }
}