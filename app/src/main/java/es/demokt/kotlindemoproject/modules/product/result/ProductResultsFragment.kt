package es.demokt.kotlindemoproject.modules.product.result

import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.VERTICAL
import android.view.View
import es.demokt.kotlindemoproject.R
import es.demokt.kotlindemoproject.R.string
import es.demokt.kotlindemoproject.data.network.product.SearchProductByEanRequest
import es.demokt.kotlindemoproject.modules.addProduct.CurrentProduct
import es.demokt.kotlindemoproject.modules.base.BaseFragment
import es.demokt.kotlindemoproject.utils.Extras
import kotlinx.android.synthetic.main.fragment_product_results.btn_new_product

/**
 * A simple [Fragment] subclass.
 *
 */
class ProductResultsFragment : BaseFragment(), ProductResultView {

  private lateinit var adapter: ProductResultsAdapter
  private lateinit var rv: RecyclerView
  private val presenter: ProductResultPresenter by lazy { ProductResultPresenter(this) }

  override fun onCreateViewId(): Int {
    return R.layout.fragment_product_results
  }

  private lateinit var ean: String

  override fun viewCreated(view: View?) {

    CurrentProduct.product = null
    CurrentProduct.filePaths = arrayListOf()

    val data = arguments?.getString(Extras.DATA)
    if (data != null) {
      ean = data
      presenter.searchProducts(SearchProductByEanRequest(data, 0))
    } else {
      getNavigator().goBack()
    }

    configurator?.title = getString(string.results)

    rv = view!!.findViewById(R.id.productList)

    val gridManager = GridLayoutManager(context, 2, VERTICAL, false)
    gridManager.stackFromEnd = false

    adapter = ProductResultsAdapter()

    rv.setHasFixedSize(true)
    rv.layoutManager = gridManager
    rv.adapter = adapter

    adapter.setItemSelected {
      it.eanValue = ean
      getNavigator().addExtra(Extras.PRODUCT, it).addBackStack(true).navigateToAddProduct()
    }

    btn_new_product.setOnClickListener({
      getNavigator().navigateToAddProduct()
    })
  }

  override fun hasToolbarBackButton(): Boolean {
    return true
  }

  override fun setList(result: List<Product>) {
    setToolbarTitle("" + result.count() + " " + getString(string.results))
    adapter.setList(result)
  }

  override fun goToAddProduct() {
    getNavigator().navigateToAddProduct()
  }

  override fun removeExtras() {
    arguments?.remove(Extras.DATA)
  }
}
