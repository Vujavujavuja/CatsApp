package com.vujic.rma1.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vujic.rma1.details.catDetails
import com.vujic.rma1.list.catList

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "cats") {
        catList(
            route = "cats",
            onCatSelected = {
                navController.navigate("cat/$it")
            }
        )

        catDetails(
            route = "cat/{catId}",
            arguments = listOf(
                navArgument(name = "catId") {
                    type = NavType.StringType
                    nullable = false
                }
            ),
            onClose = {
                navController.navigateUp()
            }
        )
    }
}