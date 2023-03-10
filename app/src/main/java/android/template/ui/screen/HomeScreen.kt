/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.template.ui.screen

import android.template.data.network.api.UserResponseItem
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import android.template.ui.theme.MyApplicationTheme
import android.template.ui.viewmodel.GitAppViewState
import android.template.ui.viewmodel.GitUsersViewModel
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: GitUsersViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getUsers()
    }

    val state = viewModel.collectAsState()

    Column(modifier = modifier) {
        HomeScreen(state)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeScreen(data: State<GitAppViewState>) {
    val users = data.value.pagingResult.collectAsLazyPagingItems()

    LazyColumn {
        stickyHeader {
            Surface(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = users.itemCount.toString(),
                    style = MaterialTheme.typography.displayLarge
                )
            }

        }
        items(users) { user ->
            if (user != null) {
                UserItem(user)
            }
        }

        users.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    // Footer loading state
                    item { LoadingView(modifier = Modifier.fillParentMaxSize()) }
                }

                loadState.append is LoadState.Loading -> {
                    // Denotes a full screen loader.
                    item { LoadingItem() }
                }

                loadState.refresh is LoadState.Error -> {
                    // Denotes an error which will be shown in the footer.

                    val e = users.loadState.refresh as LoadState.Error
                    item {
                        ErrorItem(
                            message = e.error.localizedMessage!!,
                            modifier = Modifier.fillParentMaxSize(),
                            onClickRetry = { retry() }
                        )
                    }
                }

                loadState.append is LoadState.Error -> {
                    val e = users.loadState.append as LoadState.Error
                    item {
                        ErrorItem(
                            message = e.error.localizedMessage!!,
                            onClickRetry = { retry() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingItem() {
    CircularProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Composable
fun ErrorItem(
    message: String,
    modifier: Modifier = Modifier,
    onClickRetry: () -> Unit
) {
    Column(modifier = modifier) {
        Text(text = "Error : $message")
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { onClickRetry() }) {
            Text(text = "Retry")
        }
    }
}

@Composable
fun LoadingView(modifier: Modifier) {
    CircularProgressIndicator(modifier)
}

@Composable
fun UserItem(userResponseItem: UserResponseItem) {
    Column(
        modifier = Modifier
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = userResponseItem.id.toString(), style = MaterialTheme.typography.titleMedium)
        Text(
            text = userResponseItem.name
        )
    }
}

// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        // MyModelScreen(listOf("Compose", "Room", "Kotlin"), onSave = {})
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        //    MyModelScreen(listOf("Compose", "Room", "Kotlin"), onSave = {})
    }
}
