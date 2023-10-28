package com.beworld.pokemoninfocompose.presentation.camera


import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.beworld.pokemoninfocompose.R
import com.beworld.pokemoninfocompose.presentation.application.ApplicationScreens
import com.beworld.pokemoninfocompose.presentation.camera.components.PhotoListItem
import com.beworld.pokemoninfocompose.presentation.camera.components.PhotoViewer
import com.beworld.pokemoninfocompose.presentation.camera.components.RemoveDialog
import com.beworld.pokemoninfocompose.presentation.camera.components.RemoveMenu
import com.beworld.pokemoninfocompose.presentation.pokemon_list.components.DownloadMessage
import com.beworld.task1.common.Constants
import com.beworld.task1.data.local.photo_provider.Photo
import com.beworld.task1.photo_provider.PhotoProvider
import com.beworld.task1.pokemosso.Pokemosso
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RememberReturnType")
@Composable
fun PhotoScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: PhotosViewModel = hiltViewModel()
) {
    val state = viewModel.photoListStateHolder.value
    remember {
        viewModel.loadPhotoList()
    }

    val viewerState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val viewerScope = rememberCoroutineScope()
    var pickedPhoto by remember {
        mutableStateOf(
            Photo(
                "", "", ""
            )
        )
    }

    val isDialogVisible = remember {
        mutableStateOf(false)
    }
    val photoForRemove = remember {
        mutableStateOf(Photo("", "", ""))
    }

    Box(modifier.fillMaxSize()) {
        LazyColumn(
            verticalArrangement = Arrangement
                .spacedBy(dimensionResource(id = R.dimen.medium_padding))
        ) {
            itemsIndexed(state.photos) { index, photo ->
                var photoBitmap by remember {
                    mutableStateOf<ImageBitmap?>(null)
                }
                var isError by remember { mutableStateOf(false) }

                val isConvertOptions = remember {
                    mutableStateOf(false)
                }

                Pokemosso.get()
                    .load(photo.uri)
                    .resize(96, 96)
                    .get(object : Pokemosso.Callback {
                        override fun onComplete(bitmap: ImageBitmap?) {
                            photoBitmap = bitmap
                        }

                        override fun onError() {
                            isError = true
                        }
                    })

                PhotoListItem(
                    photo = photo,
                    imageBitmap = photoBitmap,
                    isError = isError,
                    onItemClick = {
                        viewerScope.launch {
                            pickedPhoto = photo
                            viewerState.show()
                        }
                    },
                    dialogState = isDialogVisible,
                    photoForRemove = photoForRemove,
                    convert = { viewModel.photoToBlackAndWhite(photo.uri, photo.name) }
                )
            }
        }
    }

    if (viewModel.loadingState.value == true) {
        DownloadMessage()
    }

    if (isDialogVisible.value) {
        RemoveDialog(
            isVisible = isDialogVisible.value,
            dismiss = { isDialogVisible.value = false },
            confirm = {
                viewModel.removePhoto(photoForRemove.value)
                isDialogVisible.value = false
            }
        )
    }

    if (viewerState.currentValue != SheetValue.Hidden) {
        PhotoViewer(state = viewerState, photo = pickedPhoto)
    }
}