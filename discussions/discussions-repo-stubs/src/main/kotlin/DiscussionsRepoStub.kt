package ru.music.discussions.repository.inmemory

import repo.*
import ru.music.common.repo.DbDiscussionUsersIdRequest
import ru.music.discussions.stubs.DiscStub

class DiscussionsRepoStub() : IDiscussionRepository {
    override suspend fun createDiscussion(rq: DbDiscussionRequest): DbDiscussionResponse {
        return DbDiscussionResponse(
            data = DiscStub.prepareResult { },
            isSuccess = true,
        )
    }

    override suspend fun readDiscussion(rq: DbDiscussionIdRequest): DbDiscussionResponse {
        return DbDiscussionResponse(
            data = DiscStub.prepareResult { },
            isSuccess = true,
        )
    }

    override suspend fun updateDiscussion(rq: DbDiscussionRequest): DbDiscussionResponse {
        return DbDiscussionResponse(
            data = DiscStub.prepareResult { },
            isSuccess = true,
        )
    }

    override suspend fun closeDiscussion(rq: DbDiscussionRequest): DbDiscussionResponse {
        return DbDiscussionResponse(
            data = DiscStub.prepareResult { },
            isSuccess = true,
        )
    }

    override suspend fun deleteDiscussion(rq: DbDiscussionIdRequest): DbDiscussionResponse {
        return DbDiscussionResponse(
            data = DiscStub.prepareResult { },
            isSuccess = true,
        )
    }

    override suspend fun allDiscussions(rq: DbDiscussionUsersIdRequest): DbDiscussionsResponse {
        return DbDiscussionsResponse(
            data = DiscStub.prepareDiscussionsList(null),
            isSuccess = true,
        )
    }

    override suspend fun usersDiscussions(rq: DbDiscussionUsersIdRequest): DbDiscussionsResponse {
        return DbDiscussionsResponse(
            data = DiscStub.prepareDiscussionsList(rq.usersId),
            isSuccess = true,
        )
    }
}
