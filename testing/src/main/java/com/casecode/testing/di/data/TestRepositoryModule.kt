package com.casecode.testing.di.data

import com.casecode.di.data.RepositoryModule
import com.casecode.domain.repository.BusinessRepository
import com.casecode.domain.repository.EmployeesBusinessRepository
import com.casecode.domain.repository.StoreRepository
import com.casecode.domain.repository.SubscriptionsBusinessRepository
import com.casecode.domain.repository.SubscriptionsRepository
import com.casecode.testing.repository.TestBusinessRepository
import com.casecode.testing.repository.TestEmployeesBusinessRepository
import com.casecode.testing.repository.TestStoreRepository
import com.casecode.testing.repository.TestSubscriptionsBusinessRepository
import com.casecode.testing.repository.TestSubscriptionsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
   components = [SingletonComponent::class],
   replaces = [RepositoryModule::class],
              )
interface TestRepositoryModule
{
   @Singleton
   @Binds
   fun bindTestBusinessRepo(testBusinessRepository: TestBusinessRepository): BusinessRepository
   
   @Singleton
   @Binds
   fun bindTestEmployeesBusinessRepo(testEmployeesBusinessRepository: TestEmployeesBusinessRepository): EmployeesBusinessRepository
   
   @Singleton
   @Binds
   fun bindTestStoreRepo(testStoreRepository: TestStoreRepository): StoreRepository
   
   @Singleton
   @Binds
   fun bindTestSubscriptionsBusinessRepo(testSubscriptionsBusinessRepository: TestSubscriptionsBusinessRepository): SubscriptionsBusinessRepository
   
   @Singleton
   @Binds
   fun bindTestSubscriptionsRepo(testSubscriptionsRepository: TestSubscriptionsRepository): SubscriptionsRepository
   
}

