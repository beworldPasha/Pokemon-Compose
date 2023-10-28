package com.beworld.pokemoninfocompose.presentation.application

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.FlipToBack
import androidx.compose.material.icons.filled.FlipToFront
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.beworld.pokemoninfocompose.presentation.camera.PhotoScreen
import com.beworld.pokemoninfocompose.presentation.faq.FaqScreen
import com.beworld.pokemoninfocompose.presentation.pokemon_detail.PokemonDetailScreen
import com.beworld.pokemoninfocompose.presentation.pokemon_list.PokemonsScreen
import com.beworld.task1.common.Constants
import kotlinx.coroutines.launch


@Composable
fun ApplicationScreen(modifier: Modifier = Modifier) {
    NavigationUi()
}

@Composable
private fun NavigationUi(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = ApplicationScreens.valueOf(
        backStackEntry?.destination?.route?.substringBefore("/")
            ?: ApplicationScreens.PokemonList.name
    )

    val isDrawer = remember {
        mutableStateOf(true)
    }

    if (isDrawer.value) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val navigationItems = listOf(
            MenuNavigationItem(
                screenType = MenuItems.Pokemons,
                icon = Icons.Filled.Home
            ),
            MenuNavigationItem(
                screenType = MenuItems.Photos,
                icon = Icons.Filled.PhotoCamera
            ),
            MenuNavigationItem(
                screenType = MenuItems.FAQ,
                icon = Icons.AutoMirrored.Filled.Help
            )
        )
        val selectedItem = MenuItems.valueOf(
            navController.currentBackStackEntry?.destination?.parent?.route
                ?: MenuItems.Pokemons.name
        )
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Text("Menu", modifier = Modifier.padding(16.dp))
                    HorizontalDivider()

                    for (item in navigationItems) {
                        NavigationDrawerItem(
                            label = { Text(text = item.screenType.name) },
                            selected = selectedItem == item.screenType,
                            onClick = {
                                navController.navigate(item.screenType.name) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true

                                    scope.launch {
                                        drawerState.apply {
                                            if (isClosed) open() else close()
                                        }
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }
            }
        ) {
            Scaffold(
                modifier = modifier,
                topBar = {
                    DrawerAppBar(
                        currentScreen = currentScreen,
                        menuClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        },
                        drawerState = isDrawer
                    )
                }
            ) { innerPadding ->
                NavigationModule(
                    controller = navController,
                    startScreen = MenuItems.Pokemons,
                    currentScreen = currentScreen,
                    padding = innerPadding
                )
            }
        }
    } else {
        Scaffold(
            modifier = modifier,
            topBar = {
                PokemonAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = currentScreen == ApplicationScreens.PokemonDetail,
                    navigateUp = { navController.navigateUp() },
                    drawerState = isDrawer
                )
            },
            bottomBar = {
                BottomAppBar(navController = navController)
            }
        ) { innerPadding ->
            NavigationModule(
                controller = navController,
                startScreen = MenuItems.Pokemons,
                currentScreen = currentScreen,
                padding = innerPadding
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DrawerAppBar(
    currentScreen: ApplicationScreens,
    drawerState: MutableState<Boolean>,
    menuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val screenTitle = remember { mutableStateOf("") }
    screenTitle.value = currentScreen.title

    TopAppBar(
        title = {
            Text(text = screenTitle.value, style = MaterialTheme.typography.displayLarge)
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        navigationIcon = {
            IconButton(onClick = menuClick) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(onClick = { drawerState.value = false }) {
                Icon(
                    imageVector = Icons.Filled.FlipToBack,
                    contentDescription = "Change Menu Type"
                )
            }
        },
        modifier = modifier
    )
}

@Composable
private fun NavigationModule(
    controller: NavHostController,
    startScreen: MenuItems,
    currentScreen: ApplicationScreens,
    padding: PaddingValues,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = controller,
        startDestination = startScreen.name,
        modifier = modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        navigation(
            route = MenuItems.Pokemons.name,
            startDestination = ApplicationScreens.PokemonList.name
        ) {
            composable(route = ApplicationScreens.PokemonList.name) {
                PokemonsScreen(navController = controller)
            }
            composable(
                route = ApplicationScreens.PokemonDetail.name + "/{${Constants.PARAM_POKEMON_NAME}}",
                arguments = listOf(
                    navArgument(Constants.PARAM_POKEMON_NAME) { type = NavType.StringType }
                ),
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(300)
                    )
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(200)
                    )
                }
            ) { backStack ->
                backStack.arguments?.getString(Constants.PARAM_POKEMON_NAME)?.let {
                    currentScreen.title = it
                }
                PokemonDetailScreen()
            }
        }
        navigation(
            route = MenuItems.Photos.name,
            startDestination = ApplicationScreens.PhotoScreen.name
        ) {
            composable(route = ApplicationScreens.PhotoScreen.name) {
                PhotoScreen(navController = controller)
            }
        }
        navigation(
            route = MenuItems.FAQ.name,
            startDestination = ApplicationScreens.FaqScreen.name
        ) {
            composable(route = ApplicationScreens.FaqScreen.name) {
                FaqScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PokemonAppBar(
    currentScreen: ApplicationScreens,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    drawerState: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    val screenTitle = remember { mutableStateOf("") }
    screenTitle.value = currentScreen.title

    TopAppBar(
        title = {
            Text(text = screenTitle.value, style = MaterialTheme.typography.displayLarge)
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = { drawerState.value = true }) {
                Icon(
                    imageVector = Icons.Filled.FlipToFront,
                    contentDescription = "Change Menu Type"
                )
            }
        },
        modifier = modifier
    )
}

@Composable
private fun BottomAppBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val selectedItem = MenuItems.valueOf(
        navController.currentBackStackEntry?.destination?.parent?.route ?: MenuItems.Pokemons.name
    )

    val navigationItems = listOf(
        MenuNavigationItem(
            screenType = MenuItems.Pokemons,
            icon = Icons.Filled.Home
        ),
        MenuNavigationItem(
            screenType = MenuItems.Photos,
            icon = Icons.Filled.PhotoCamera
        ),
        MenuNavigationItem(
            screenType = MenuItems.FAQ,
            icon = Icons.AutoMirrored.Filled.Help
        )
    )

    NavigationBar(modifier = modifier) {
        for (item in navigationItems) {
            NavigationBarItem(
                selected = selectedItem == item.screenType,
                onClick = {
                    navController.navigate(item.screenType.name) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null
                    )
                }
            )
        }
    }
}


@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun Preview() {

}