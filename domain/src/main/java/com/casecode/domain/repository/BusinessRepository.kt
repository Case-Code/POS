package com.casecode.domain.repository

import com.casecode.domain.model.users.Business
import com.casecode.domain.utils.Resource
import javax.inject.Singleton

typealias AddBusiness = Resource<Boolean>

interface BusinessRepository
{
   
   suspend fun getBusiness(uid: String): Business
   suspend fun setBusiness(business: Business, uid: String): AddBusiness
}