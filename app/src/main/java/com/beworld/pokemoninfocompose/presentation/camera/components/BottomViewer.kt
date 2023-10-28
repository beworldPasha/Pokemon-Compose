package com.beworld.pokemoninfocompose.presentation.camera.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material.icons.filled.SignalWifiBad
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.beworld.pokemoninfocompose.R
import com.beworld.pokemoninfocompose.presentation.pokemon_detail.components.DetailMenu
import com.beworld.task1.data.local.photo_provider.Photo
import com.beworld.task1.pokemosso.Pokemosso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoViewer(
    modifier: Modifier = Modifier,
    state: SheetState,
    photo: Photo
) {
    var photoBitmap by remember {
        mutableStateOf<ImageBitmap?>(null)
    }
    var isError by remember { mutableStateOf(false) }

    photo.let {
        Pokemosso.get()
            .load(it.uri)
            .resize(248, 440)
            .get(object : Pokemosso.Callback {
                override fun onComplete(bitmap: ImageBitmap?) {
                    photoBitmap = bitmap
                }

                override fun onError() {
                    isError = true
                }
            })
    }

    ModalBottomSheet(
        onDismissRequest = { /*TODO*/ },
        sheetState = state,
        modifier = modifier
    ) {
        Box(
            Modifier
                .height(600.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
        ) {
            if (isError) {
                Image(
                    imageVector = Icons.Filled.ImageNotSupported,
                    contentDescription = "Bad Internet connection",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp)
                        .fillMaxSize()
                )
            } else if (photoBitmap == null) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            } else {
                Image(
                    bitmap = photoBitmap!!,
                    contentDescription = "Pokemon ${photo.name}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxSize()
                )
            }

            Column(
                verticalArrangement = Arrangement
                    .spacedBy(dimensionResource(id = R.dimen.low_padding)),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(dimensionResource(id = R.dimen.medium_padding))
            ) {
                Text(text = photo.name)
                Text(text = photo.date)
            }
        }
    }
}
