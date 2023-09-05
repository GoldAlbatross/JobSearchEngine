package ru.practicum.android.diploma.search.ui.models

import android.util.Log
import android.view.View
import com.google.android.material.appbar.AppBarLayout
import ru.practicum.android.diploma.Logger
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.domain.models.NetworkError
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.search.ui.fragment.SearchAdapter
import ru.practicum.android.diploma.util.thisName
import javax.inject.Inject

sealed interface SearchScreenState {
    
    fun render(binding: FragmentSearchBinding)
    
    private fun refreshJobList(binding: FragmentSearchBinding, list: List<Vacancy>) {
        val adapter = (binding.recycler.adapter as SearchAdapter)
        Log.e("MyLog", "SearchScreenState, adapter list = ${adapter.currentList}")
        adapter.submitList(list) {
        
        }
        Log.e("MyLog", "SearchScreenState, adapter list = ${adapter.currentList}")
    }
    
    private fun isScrollingEnabled(binding: FragmentSearchBinding, isEnable: Boolean) {
        
        with(binding) {
            
            val searchLayoutParams: AppBarLayout.LayoutParams =
                searchContainer.layoutParams as AppBarLayout.LayoutParams
            val toolbarLayoutParams: AppBarLayout.LayoutParams =
                searchToolbar.layoutParams as AppBarLayout.LayoutParams
            val textLayoutParams: AppBarLayout.LayoutParams =
                textFabSearch.layoutParams as AppBarLayout.LayoutParams
            
            if (!isEnable) {
                searchLayoutParams.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL
                toolbarLayoutParams.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL
                textLayoutParams.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL
            } else {
                searchLayoutParams.scrollFlags =
                    AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                toolbarLayoutParams.scrollFlags =
                    AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                textLayoutParams.scrollFlags =
                    AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
            }
    
            searchContainer.layoutParams = searchLayoutParams
            searchToolbar.layoutParams = toolbarLayoutParams
            textFabSearch.layoutParams = textLayoutParams
        }
    }
    
    class Default @Inject constructor(
        private val logger: Logger,
    ) : SearchScreenState {
        
        override fun render(binding: FragmentSearchBinding) {
            logger.log(thisName, "render -> $thisName")
            super.refreshJobList(binding, emptyList())
            super.isScrollingEnabled(binding, false)
            
            with(binding) {
                textFabSearch.visibility = View.GONE
                recycler.visibility = View.GONE
                placeholderImage.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }
    }
    
    class Loading @Inject constructor(
        private val logger: Logger,
    ) : SearchScreenState {
        
        override fun render(binding: FragmentSearchBinding) {
            logger.log(thisName, "render -> $thisName")
            super.refreshJobList(binding, emptyList())
            super.isScrollingEnabled(binding, false)
            
            with(binding) {
                val context = textFabSearch.context
                
                textFabSearch.text = context.getString(R.string.loading_message)
                textFabSearch.visibility = View.VISIBLE
                recycler.visibility = View.GONE
                placeholderImage.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }
        }
    }
    
    data class Content(
        private val count: Int,
        private val jobList: List<Vacancy>,
        private val logger: Logger,
    ) : SearchScreenState {
    
        override fun render(binding: FragmentSearchBinding) {
            logger.log(thisName, "render -> $thisName")
            super.refreshJobList(binding, jobList)
            super.isScrollingEnabled(binding, true)
        
            with(binding) {
                val context = textFabSearch.context
            
                val fabText = StringBuilder()
                fabText.append(context.getString(R.string.found))
                fabText.append(" ")
                fabText.append(
                    context.resources.getQuantityString(
                        R.plurals.vacancies, count, count
                    )
                )
            
                textFabSearch.text = fabText.toString()
            
                textFabSearch.visibility = View.VISIBLE
                recycler.visibility = View.VISIBLE
                placeholderImage.visibility = View.GONE
                progressBar.visibility = View.GONE
            }
        }
    
    }
    
    data class Error(
        private val error: NetworkError,
        private val logger: Logger,
    ) : SearchScreenState {
        
        override fun render(binding: FragmentSearchBinding) {
            logger.log(thisName, "render -> $thisName")
            super.refreshJobList(binding, emptyList())
            super.isScrollingEnabled(binding, false)
            
            when (error) {
                NetworkError.SEARCH_ERROR -> showEmpty(binding)
                NetworkError.CONNECTION_ERROR -> showConnectionError(binding)
            }
        }
        
        private fun showConnectionError(binding: FragmentSearchBinding) {
            logger.log(thisName, "showConnectionError -> $thisName")
            
            with(binding) {
                val context = textFabSearch.context
                textFabSearch.text = context.getString(R.string.update)
                
                textFabSearch.visibility = View.VISIBLE
                recycler.visibility = View.GONE
                placeholderImage.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }
        }
        
        private fun showEmpty(binding: FragmentSearchBinding) {
            logger.log(thisName, "showEmpty -> $thisName")
            
            with(binding) {
                val context = textFabSearch.context
                textFabSearch.text = context.getString(R.string.empty_search_error)
                
                textFabSearch.visibility = View.VISIBLE
                recycler.visibility = View.GONE
                placeholderImage.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }
    }
}