package com.beworld.pokemoninfocompose.presentation.faq

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.beworld.pokemoninfocompose.R
import com.beworld.pokemoninfocompose.presentation.ui.theme.PokemonInfoComposeTheme

@Composable
fun FaqScreen(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.medium_padding))
    ) {
        Text(text = stringResource(id = R.string.created_by))
        Text(
            text = stringResource(id = R.string.name),
            style = MaterialTheme.typography.displayLarge
        )
        Text(text = stringResource(id = R.string.group))
        Text(
            text = stringResource(id = R.string.created_for),
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun FaqPreview() {
    PokemonInfoComposeTheme {
        FaqScreen()
    }
}