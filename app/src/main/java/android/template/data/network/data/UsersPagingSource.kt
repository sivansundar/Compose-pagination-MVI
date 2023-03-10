package android.template.data.network.data

import android.template.data.network.api.GithubAI
import android.template.data.network.api.UserResponseItem
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import javax.inject.Inject


class UsersPagingSource @Inject constructor(
    private val githubAI: GithubAI
) : PagingSource<Int, UserResponseItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserResponseItem> {
        return try {
            val nextPage = params.key ?: 1
            Log.d("RES" , "Data : ${params.key} : ${params.loadSize}")
            val response = getUsers()
            val nextKey = nextPage.plus(1)


            LoadResult.Page(
                data = response,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UserResponseItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private suspend fun getUsers(): List<UserResponseItem> {
        return githubAI.getUsers(since = 1, perPage = 10)
    }
}