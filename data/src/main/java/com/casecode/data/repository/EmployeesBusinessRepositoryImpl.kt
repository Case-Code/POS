package com.casecode.data.repository

import com.casecode.data.mapper.toEmployeesRequest
import com.casecode.data.utils.AppDispatchers.IO
import com.casecode.data.utils.Dispatcher
import com.casecode.domain.model.users.Employee
import com.casecode.domain.repository.AddEmployees
import com.casecode.domain.repository.EmployeesBusinessRepository
import com.casecode.domain.utils.Resource
import com.casecode.domain.utils.USERS_COLLECTION_PATH
import com.casecode.pos.data.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class EmployeesBusinessRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
) : EmployeesBusinessRepository {
    override suspend fun getEmployees(uid: String): List<Employee> {
        TODO("Not yet implemented")
    }

    override suspend fun setEmployees(employees: MutableList<Employee>, uid: String): AddEmployees {
        return withContext(ioDispatcher) {
            try {
                val resultAddEmployee = suspendCoroutine<AddEmployees> { continuation ->
                    Timber.e("uid = $uid")
                    val employeesRequest = employees.toEmployeesRequest()
                    Timber.e("employeesRequest = $employeesRequest")

                    firestore.collection(USERS_COLLECTION_PATH).document(uid)
                        .update(employeesRequest as Map<String, Any>)
                        .addOnSuccessListener {
                            Timber.d("employees is added successfully")
                            continuation.resume(Resource.Success(true))
                        }.addOnFailureListener {
                            continuation.resume(Resource.error(R.string.add_employees_business_failure))

                            Timber.e("employees Failure: $it")
                        }

                }
                resultAddEmployee
            } catch (e: UnknownHostException) {
                Resource.error(R.string.add_employees_business_network)

            } catch (e: Exception) {
                Timber.e("Exception while adding employees: $e")
                Resource.error(R.string.add_employees_business_failure)
            }
        }

    }
}