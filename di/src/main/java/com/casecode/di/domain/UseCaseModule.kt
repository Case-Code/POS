package com.casecode.di.domain

import com.casecode.domain.repository.BusinessRepository
import com.casecode.domain.repository.EmployeesBusinessRepository
import com.casecode.domain.repository.StoreRepository
import com.casecode.domain.repository.SubscriptionsBusinessRepository
import com.casecode.domain.repository.SubscriptionsRepository
import com.casecode.domain.usecase.GetStoreUseCase
import com.casecode.domain.usecase.GetSubscriptionBusinessUseCase
import com.casecode.domain.usecase.GetSubscriptionsUseCase
import com.casecode.domain.usecase.SetBusinessUseCase
import com.casecode.domain.usecase.SetEmployeesBusinessUseCase
import com.casecode.domain.usecase.SetSubscriptionBusinessUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule
{
   
   @Provides
   fun provideSetSubscriptionBusinessUseCase(subscriptionsBusinessRepository: SubscriptionsBusinessRepository): SetSubscriptionBusinessUseCase
   {
      return SetSubscriptionBusinessUseCase(subscriptionsBusinessRepository)
   }
   
   @Provides
   fun provideGetSubscriptionBusinessUseCase(subscriptionsBusinessRepository: SubscriptionsBusinessRepository): GetSubscriptionBusinessUseCase
   {
      return GetSubscriptionBusinessUseCase(subscriptionsBusinessRepository)
   }
   
   @Provides
   fun provideSetBusinessUseCase(storeRep: BusinessRepository): SetBusinessUseCase
   {
      return SetBusinessUseCase(storeRep)
   }
   
   @Provides
   fun provideGetSubscriptionsUseCase(subscriptionsRepository: SubscriptionsRepository): GetSubscriptionsUseCase
   {
      return GetSubscriptionsUseCase(subscriptionsRepository)
   }
   
   @Provides
   fun provideSetEmployeesBusinessUseCase(employeesBusRepo: EmployeesBusinessRepository): SetEmployeesBusinessUseCase
   {
      return SetEmployeesBusinessUseCase(employeesBusRepo)
   }
   
   @Provides
   fun provideGetStoreUseCase(storeRep: StoreRepository): GetStoreUseCase
   {
      return GetStoreUseCase(storeRep)
   }
   /*    @Provides
      fun provideGetDocumentsUseCase(firestoreRepository: FirestoreRepository): GetDocuments {
          return GetDocuments(firestoreRepository)
      }
  
  
      @Provides
      fun provideAddDocumentsUseCase(firestoreRepository: FirestoreRepository): AddDocuments {
          return AddDocuments(firestoreRepository)
      }
  
      @Provides
      fun provideUpdateDocumentsUseCase(firestoreRepository: FirestoreRepository): UpdateDocument {
          return UpdateDocument(firestoreRepository)
      }
  
      @Provides
      fun provideDeleteDocumentsUseCase(firestoreRepository: FirestoreRepository): DeleteDocument {
          return DeleteDocument(firestoreRepository)
      } */
}