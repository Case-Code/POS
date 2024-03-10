package com.casecode.di.domain

import com.casecode.domain.repository.BusinessRepository
import com.casecode.domain.repository.EmployeesBusinessRepository
import com.casecode.domain.repository.ImageRepository
import com.casecode.domain.repository.ItemRepository
import com.casecode.domain.repository.SignRepository
import com.casecode.domain.repository.StoreRepository
import com.casecode.domain.repository.SubscriptionsBusinessRepository
import com.casecode.domain.repository.SubscriptionsRepository
import com.casecode.domain.usecase.CompleteBusinessUseCase
import com.casecode.domain.usecase.GetStoreUseCase
import com.casecode.domain.usecase.GetSubscriptionBusinessUseCase
import com.casecode.domain.usecase.GetSubscriptionsUseCase
import com.casecode.domain.usecase.ImageUseCase
import com.casecode.domain.usecase.ItemUseCase
import com.casecode.domain.usecase.SetBusinessUseCase
import com.casecode.domain.usecase.SetEmployeesBusinessUseCase
import com.casecode.domain.usecase.SetSubscriptionBusinessUseCase
import com.casecode.domain.usecase.SignInUseCase
import com.casecode.domain.usecase.SignOutUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideSignInUseCase(signInRepository: SignRepository) = SignInUseCase(signInRepository)

    @Provides
    fun provideSignOutUseCase(signInRepository: SignRepository) = SignOutUseCase(signInRepository)

    @Provides
    fun provideSetBusinessUseCase(businessRepository: BusinessRepository) =
        SetBusinessUseCase(businessRepository)

    @Provides
    fun provideCompleteBusinessUseCase(businessRepository: BusinessRepository) =
        CompleteBusinessUseCase(businessRepository)


    @Provides
    fun provideSetSubscriptionBusinessUseCase(
        subscriptionsBusinessRepository: SubscriptionsBusinessRepository
    ) = SetSubscriptionBusinessUseCase(subscriptionsBusinessRepository)

    @Provides
    fun provideGetSubscriptionBusinessUseCase(
        subscriptionsBusinessRepository: SubscriptionsBusinessRepository
    ) = GetSubscriptionBusinessUseCase(subscriptionsBusinessRepository)

    @Provides
    fun provideGetSubscriptionsUseCase(subscriptionsRepository: SubscriptionsRepository) =
        GetSubscriptionsUseCase(subscriptionsRepository)

    @Provides
    fun provideSetEmployeesBusinessUseCase(employeesBusRepo: EmployeesBusinessRepository) =
        SetEmployeesBusinessUseCase(employeesBusRepo)

    @Provides
    fun provideGetStoreUseCase(storeRep: StoreRepository) = GetStoreUseCase(storeRep)

    @Provides
    fun provideProductUseCase(itemRepository: ItemRepository) =
        ItemUseCase(itemRepository)

    @Provides
    fun provideImageUseCase(imageRepository: ImageRepository) =
        ImageUseCase(imageRepository)
}