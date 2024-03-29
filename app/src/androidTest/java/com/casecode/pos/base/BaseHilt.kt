package com.casecode.pos.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.casecode.domain.usecase.GetBusinessUseCase
import com.casecode.domain.usecase.GetSubscriptionsUseCase
import com.casecode.domain.usecase.GetStoreUseCase
import com.casecode.domain.usecase.SetBusinessUseCase
import com.casecode.testing.repository.TestBusinessRepository
import com.casecode.testing.repository.TestSubscriptionsRepository
import com.casecode.testing.util.MainDispatcherRule
import org.junit.Before
import org.junit.Rule

abstract class BaseStepperTest
{
   
   // Set the main coroutines dispatcher for unit testing.
   @get:Rule
   val mainDispatcherRule = MainDispatcherRule()
   
   @Rule
   @JvmField
   val instantTaskExecutorRule = InstantTaskExecutorRule()
   
   private lateinit var testBusinessRepository: TestBusinessRepository
   private lateinit var testPlansRepository: TestSubscriptionsRepository
   
   
   private lateinit var getBusinessUseCase: GetBusinessUseCase
   lateinit var setBusinessUseCase: SetBusinessUseCase
   lateinit var getStoreUseCase: GetStoreUseCase
   lateinit var getPlanUseCase: GetSubscriptionsUseCase
   @Before
   fun setup(){
      testBusinessRepository = TestBusinessRepository()
      testPlansRepository = TestSubscriptionsRepository()
      
      getBusinessUseCase = GetBusinessUseCase(testBusinessRepository)
      setBusinessUseCase = SetBusinessUseCase(testBusinessRepository)
      getPlanUseCase = GetSubscriptionsUseCase(testPlansRepository)
      
      init()
   }
abstract fun init()
}