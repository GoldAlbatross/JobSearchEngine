package ru.practicum.android.diploma.filter.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.Logger
import ru.practicum.android.diploma.filter.domain.models.NetworkResponse
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.api.FilterRepository
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.Region
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import javax.inject.Inject

class FilterInteractorImpl @Inject constructor(
    private val filterRepository: FilterRepository,
    private val searchRepository: SearchRepository,
    private val logger: Logger
) : FilterInteractor {

    override fun filter() {}
    override suspend fun getCountries(): Flow<NetworkResponse<List<Country>>> {
        return searchRepository.getCountries()
    }

    override suspend fun getRegions(): Flow<NetworkResponse<List<Region>>> = flow {
        emit (NetworkResponse.Success(List(20) { Region() }))
    }

    override suspend fun getRegions(query: String): Flow<NetworkResponse<List<Region>>> {
        return searchRepository.getRegions(query)
    }
}