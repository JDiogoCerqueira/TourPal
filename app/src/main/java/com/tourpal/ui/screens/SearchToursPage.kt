package com.tourpal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tourpal.ui.theme.TourPalTheme
import com.tourpal.viewmodels.SearchViewModel
import TopBar
import androidx.compose.material3.Scaffold
import com.tourpal.ui.components.NavBar

@Composable
fun SearchToursPage(navController: NavHostController) {
    var searchText by remember { mutableStateOf("") }
    val context = LocalContext.current
    val viewModel: SearchViewModel = viewModel()

    // Load recent searches when the composable is first created
    viewModel.loadRecentSearches(context)

    // Function to add a search term to the recent searches list and navigate
    fun addRecentSearchAndNavigate(query: String) {
        if (query.isBlank()) return // Ignore empty searches
        viewModel.addRecentSearch(context, query)
        // Navigate to the tour results page with the search query
        //navController.navigate("tourResultsPage/$query")
        navController.navigate("tourPlansResultsPage/$query")
    }

    Scaffold(
        bottomBar = {
            NavBar(navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar(text = "Explore Tour Plans")
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                // Subtitle: "Where's your next?"
                Text(
                    text = "Where's your next?",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        color = Color.Black
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Search Bar
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(8.dp)),
                    placeholder = {
                        Text(
                            text = "Search",
                            color = Color.Gray
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                            tint = Color.Gray
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            addRecentSearchAndNavigate(searchText)
                            searchText = ""
                        }
                    ),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.LightGray,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                // Recently Searched Section
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Recently Searched",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (viewModel.recentSearches.isEmpty()) {
                    Text(
                        text = "No recent searches",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            color = Color.Gray
                        ),
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                } else {
                    Column {
                        viewModel.recentSearches.forEachIndexed { index, query ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(IntrinsicSize.Min),
                                verticalAlignment = CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                ) {
                                    Text(
                                        text = query,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontSize = 14.sp,
                                            color = Color.Black
                                        ),
                                        modifier = Modifier
                                            .clickable {
                                                addRecentSearchAndNavigate(query)
                                            }
                                            .padding(vertical = 12.dp)
                                    )
                                    if (index < viewModel.recentSearches.size - 1) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(horizontal = 4.dp),
                                            thickness = 0.5.dp,
                                            color = Color.LightGray
                                        )
                                    }
                                }
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Clear search",
                                    tint = Color.Gray,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clickable {
                                            viewModel.clearSearch(context, query)
                                        }
                                        .padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun SearchToursPagePreview() {
    TourPalTheme {
        SearchToursPage(navController = rememberNavController())
    }
}