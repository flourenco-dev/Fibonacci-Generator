package com.flourenco.fibonacci.core

import com.flourenco.fibonacci.core.storage.StorageHelper
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val storageHelper: StorageHelper): Repository {

}