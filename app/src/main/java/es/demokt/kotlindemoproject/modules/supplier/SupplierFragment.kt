package es.demokt.kotlindemoproject.modules.store

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.View
import android.widget.TextView
import es.demokt.kotlindemoproject.R
import es.demokt.kotlindemoproject.data.network.supplier.SearchSupplierRequest
import es.demokt.kotlindemoproject.modules.supplier.SearchSupplierModel
import es.demokt.kotlindemoproject.utils.Extras
import kotlinx.android.synthetic.main.fragment_search_store.btnAdd
import kotlinx.android.synthetic.main.fragment_search_store.rvStore
import kotlinx.android.synthetic.main.fragment_search_store.searchView
import kotlinx.android.synthetic.main.fragment_search_store.toolbar

class SupplierFragment : SearchStoreFragment(), SupplierView {
  private val presenter: SupplierPresenter by lazy { SupplierPresenter(this) }

  private var adapter: SupplierAdapter? = null
  override fun viewCreated(view: View?) {
    initViews()
    presenter.init(arguments)
    showBackButton()
  }

  private fun initViews() {
    val title = toolbar.findViewById(R.id.tvToolbarTitle) as TextView
    title.text = getString(R.string.suppliers)
    btnAdd.visibility = View.VISIBLE

    btnAdd.setOnClickListener {
      /*val fragmentManager = getBaseActivity().supportFragmentManager
      val fragmentC = AddSupplierFragment()
      val trans = fragmentManager!!.beginTransaction()

      trans.addToBackStack(null)
      trans.replace(R.id.container, fragmentC, AddSupplierFragment::javaClass.name)
          .commit()

      toolbar.visibility = View.GONE*/
      getNavigator().clearTarget().navigateToAddSupplier()
    }

    rvStore.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
    rvStore.setHasFixedSize(false)
    val layoutManager = LinearLayoutManager(activity)
    rvStore.layoutManager = layoutManager
    adapter = SupplierAdapter()
    rvStore.adapter = adapter
    adapter!!.setItemSelected {
      //presenter.onItemSelected(it)
      adapter!!.currentSupplier = it
      Log.i(LOG_TAG, it.name)
      goToProduct(it)
    }

    searchView.setOnCloseListener {
      adapter!!.data = adapter!!.origin.toList()
      adapter!!.notifyDataSetChanged()
      false
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
              val searchText = SearchSupplierRequest(currentSearch, currentPage.toString())
              presenter.search(searchText, true)
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
        currentSearch = newText
        val searchText = SearchSupplierRequest(currentSearch, "1")
        makeSearch(searchText)
        return false
      }
    })
  }

  private fun goToProduct(it: SupplierModel) {
    goBack()
    targetFragment!!.onActivityResult(
        targetRequestCode,
        Activity.RESULT_OK,
        Intent().putExtra(Extras.SUPPLIER, it)
    )

  }

  fun makeSearch(filter: SearchSupplierRequest) {
    if (filter.supplier_name.isNotEmpty()) {
      if (canFilter()) {
        time = System.currentTimeMillis()
        presenter.search(filter)
      }
    }
  }

  override fun setList(result: SearchSupplierModel) {
    currentPage = 1
    isLastPage = false
    adapter!!.setData(result)
  }

  override fun addToList(result: SearchSupplierModel) {
    isLoading = false
    adapter!!.addData(result)
  }

}