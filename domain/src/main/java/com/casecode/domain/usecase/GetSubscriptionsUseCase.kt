package com.casecode.domain.usecase

import com.casecode.domain.repository.SubscriptionsRepository
import com.casecode.domain.repository.SubscriptionsResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSubscriptionsUseCase @Inject constructor(private val subscriptionsRep: SubscriptionsRepository) {
    operator fun invoke(): Flow<SubscriptionsResource> {
        return subscriptionsRep.getSubscriptions()
    }
}

