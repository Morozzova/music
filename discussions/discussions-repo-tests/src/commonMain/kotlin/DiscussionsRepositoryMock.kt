package ru.music.discussions.repo.tests

import repo.*
import ru.music.common.repo.DbDiscussionUsersIdRequest

class DiscussionsRepositoryMock(
    private val invokeCreateDiscussion: (DbDiscussionRequest) -> DbDiscussionResponse = { DbDiscussionResponse.MOCK_SUCCESS_EMPTY },
    private val invokeReadDiscussion: (DbDiscussionIdRequest) -> DbDiscussionResponse = { DbDiscussionResponse.MOCK_SUCCESS_EMPTY },
    private val invokeUpdateDiscussion: (DbDiscussionRequest) -> DbDiscussionResponse = { DbDiscussionResponse.MOCK_SUCCESS_EMPTY },
    private val invokeCloseDiscussion: (DbDiscussionRequest) -> DbDiscussionResponse = { DbDiscussionResponse.MOCK_SUCCESS_EMPTY },
    private val invokeDeleteDiscussion: (DbDiscussionIdRequest) -> DbDiscussionResponse = { DbDiscussionResponse.MOCK_SUCCESS_EMPTY },
    private val invokeAllDiscussions: (DbDiscussionUsersIdRequest) -> DbDiscussionsResponse = { DbDiscussionsResponse.MOCK_SUCCESS_EMPTY },
    private val invokeUsersDiscussions: (DbDiscussionUsersIdRequest) -> DbDiscussionsResponse = { DbDiscussionsResponse.MOCK_SUCCESS_EMPTY },
) : IDiscussionRepository {
    override suspend fun createDiscussion(rq: DbDiscussionRequest): DbDiscussionResponse {
        return invokeCreateDiscussion(rq)
    }

    override suspend fun readDiscussion(rq: DbDiscussionIdRequest): DbDiscussionResponse {
        return invokeReadDiscussion(rq)
    }

    override suspend fun updateDiscussion(rq: DbDiscussionRequest): DbDiscussionResponse {
        return invokeUpdateDiscussion(rq)
    }

    override suspend fun closeDiscussion(rq: DbDiscussionRequest): DbDiscussionResponse {
        return invokeCloseDiscussion(rq)
    }

    override suspend fun deleteDiscussion(rq: DbDiscussionIdRequest): DbDiscussionResponse {
        return invokeDeleteDiscussion(rq)
    }

    override suspend fun allDiscussions(rq: DbDiscussionUsersIdRequest): DbDiscussionsResponse {
        return invokeAllDiscussions(rq)
    }

    override suspend fun usersDiscussions(rq: DbDiscussionUsersIdRequest): DbDiscussionsResponse {
        return invokeUsersDiscussions(rq)
    }
}
