package repo

import ru.music.common.repo.DbDiscussionUsersIdRequest

interface IDiscussionRepository {
    suspend fun createDiscussion(rq: DbDiscussionRequest): DbDiscussionResponse
    suspend fun readDiscussion(rq: DbDiscussionIdRequest): DbDiscussionResponse
    suspend fun updateDiscussion(rq: DbDiscussionRequest): DbDiscussionResponse
    suspend fun closeDiscussion(rq: DbDiscussionIdRequest): DbDiscussionResponse
    suspend fun deleteDiscussion(rq: DbDiscussionIdRequest): DbDiscussionResponse
    suspend fun allDiscussions(rq: DbDiscussionUsersIdRequest): DbDiscussionsResponse
    suspend fun usersDiscussions(rq: DbDiscussionUsersIdRequest): DbDiscussionsResponse
    companion object {
        val NONE = object : IDiscussionRepository {
            override suspend fun createDiscussion(rq: DbDiscussionRequest): DbDiscussionResponse {
                TODO("Not yet implemented")
            }

            override suspend fun readDiscussion(rq: DbDiscussionIdRequest): DbDiscussionResponse {
                TODO("Not yet implemented")
            }

            override suspend fun updateDiscussion(rq: DbDiscussionRequest): DbDiscussionResponse {
                TODO("Not yet implemented")
            }

            override suspend fun closeDiscussion(rq: DbDiscussionIdRequest): DbDiscussionResponse {
                TODO("Not yet implemented")
            }

            override suspend fun deleteDiscussion(rq: DbDiscussionIdRequest): DbDiscussionResponse {
                TODO("Not yet implemented")
            }

            override suspend fun allDiscussions(rq: DbDiscussionUsersIdRequest): DbDiscussionsResponse {
                TODO("Not yet implemented")
            }

            override suspend fun usersDiscussions(rq: DbDiscussionUsersIdRequest): DbDiscussionsResponse {
                TODO("Not yet implemented")
            }

        }
    }
}
