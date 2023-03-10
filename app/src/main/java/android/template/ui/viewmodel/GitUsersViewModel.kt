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

package android.template.ui.viewmodel

import android.template.data.GitRepository
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import android.template.data.network.api.UserResponseItem
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class GitAppViewState(
    val pagingResult: Flow<PagingData<UserResponseItem>>,
)
//
//enum class ScreenState {
//    HOME, DETAILS
//}

sealed class GitAppSideEffect() {
    data class NavigateToDetails(val id : String)
}

@HiltViewModel
class GitUsersViewModel @Inject constructor(
    private val gitRepository: GitRepository
) : ContainerHost<GitAppViewState, GitAppSideEffect>, ViewModel() {

    override val container =
        container<GitAppViewState, GitAppSideEffect>(GitAppViewState(pagingResult = emptyFlow()))


    suspend fun getUsers() = intent {
        val result: Flow<PagingData<UserResponseItem>> =
            gitRepository.getPaginatedUsers().cachedIn(viewModelScope)

        reduce {
            state.copy(pagingResult = result)
        }
    }
}