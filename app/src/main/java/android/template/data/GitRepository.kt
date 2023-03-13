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

package android.template.data

import kotlinx.coroutines.flow.Flow
import android.template.data.network.api.UserResponseItem
import android.template.data.network.data.UsersPagingSource
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import javax.inject.Inject

class GitRepository @Inject constructor(
    private val usersPagingSource: UsersPagingSource) {



    fun getPaginatedUsers(): Flow<PagingData<UserResponseItem>> {

        return Pager(
            config = PagingConfig(
                10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { usersPagingSource }
        ).flow
    }
}
