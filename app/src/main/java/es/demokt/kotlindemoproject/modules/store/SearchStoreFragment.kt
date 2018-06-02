package es.demokt.kotlindemoproject.modules.store

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.View
import es.demokt.kotlindemoproject.R
import es.demokt.kotlindemoproject.data.network.store.SearchStoreByNameRequest
import es.demokt.kotlindemoproject.modules.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_search_store.pb
import kotlinx.android.synthetic.main.fragment_search_store.rvStore
import kotlinx.android.synthetic.main.fragment_search_store.searchView
import kotlinx.android.synthetic.main.fragment_search_store.tvNoData
import kotlinx.android.synthetic.main.toolbar.toolbar

open class SearchStoreFragment : BaseFragment(), StoreView {

  private val presenter: StorePresenter by lazy { StorePresenter(this) }

  private var adapter: StoreAdapter? = null
  var time: Long = 0
  var TIME_TO_PASS: Long = 1000
  val PAGE_SIZE = 20
  var isLastPage = false
  var isLoading = false
  var currentPage = 1
  var currentSearch = "A"

  private fun initViews() {
    rvStore.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
    rvStore.setHasFixedSize(false)
    val layoutManager = LinearLayoutManager(activity)
    rvStore.layoutManager = layoutManager
    adapter = StoreAdapter()
    rvStore.adapter = adapter

    adapter!!.setItemSelected {
      presenter.onItemSelected(it)
      adapter!!.currentStore = it
      Log.i(LOG_TAG, it.name)
    }

    // PAGINATION
    rvStore.addOnScrollListener(object : RecyclerView.OnScrollListener() {

      override fun onScrolled(
        recyclerView: RecyclerView?,
        dx: Int,
        dy: Int
      ) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (!isLoading && !isLastPage) {
          if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
              && firstVisibleItemPosition >= 0
              && totalItemCount >= PAGE_SIZE
          ) {
            if (canFilter()) {
              time = System.currentTimeMillis()
              currentPage += 1
              isLoading = true
              val searchText = SearchStoreByNameRequest(currentSearch, currentPage.toString())
              presenter.searchStore(searchText, true)
            }
          }
        }

      }

    })


    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String): Boolean {
        return false
      }

      override fun onQueryTextChange(newText: String): Boolean {
        if (!newText.isNullOrEmpty())
          currentSearch = newText
        val searchText = SearchStoreByNameRequest(currentSearch, "1")
        makeSearch(searchText)
        return false
      }
    })
  }

  protected fun canFilter(): Boolean {
    return System.currentTimeMillis() - time > TIME_TO_PASS
  }

  open fun makeSearch(filter: SearchStoreByNameRequest) {
    if (filter.store_name.isNotEmpty()) {
      if (canFilter()) {
        time = System.currentTimeMillis()
        presenter.searchStore(filter)
      }
    }
  }

  override fun goBack() {
    activity!!.onBackPressed()
  }

  override fun showProgress(show: Boolean) {
    pb?.visibility = if (show) View.VISIBLE else View.GONE
  }

  override fun showList(
    show: Boolean,
    totalPages: Int
  ) {
    if (tvNoData != null && rvStore != null) {
      tvNoData!!.visibility = if (!show) View.VISIBLE else View.GONE
      rvStore.visibility = if (show) View.VISIBLE else View.GONE
    }
    isLastPage = currentPage >= totalPages
  }

  override fun setList(result: SearchStoreModel) {
    currentPage = 1
    isLastPage = false
    adapter!!.setStores(result)
  }

  override fun addToList(result: SearchStoreModel) {
    isLoading = false
    adapter!!.addStores(result)
  }

  override fun onCreateViewId(): Int {
    return R.layout.fragment_search_store
  }

  override fun viewCreated(view: View?) {
    initViews()
    presenter.onViewCreated()
    showBackButton()
  }

  protected fun showBackButton() {
    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
    toolbar.navigationIcon?.setTint(resources.getColor(R.color.white))

    toolbar.setNavigationOnClickListener {
      goBack()
      targetFragment?.onResume()
    }
  }

  override fun hasToolbar(): Boolean {
    return false
  }

}