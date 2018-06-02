package es.demokt.kotlindemoproject.modules.store

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import es.demokt.kotlindemoproject.R
import es.demokt.kotlindemoproject.modules.supplier.SearchSupplierModel
import kotlinx.android.synthetic.main.item_store.view.ivImage
import kotlinx.android.synthetic.main.item_store.view.ivSelected
import kotlinx.android.synthetic.main.item_store.view.tvAddress
import kotlinx.android.synthetic.main.item_store.view.tvName

/**
 * Created by aluengo on 3/4/18.
 */

class SupplierAdapter() : RecyclerView.Adapter<SupplierAdapter.ViewHolder>() {
  private lateinit var onItemSelected: (item: SupplierModel) -> Unit
  var currentSupplier: SupplierModel? = null
  private val layoutId: Int
  var data: List<SupplierModel> = emptyList()

  lateinit var origin: List<SupplierModel>

  init {
    this.data = emptyList()
    this.layoutId = R.layout.item_store
  }

  inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var tvAddress: TextView = view.tvAddress
    var tvName: TextView = view.tvName
    var ivImage: ImageView = view.ivImage

    var ivSelected: ImageView = view.ivSelected

    private var current: SupplierModel? = null
    fun bind(position: Int) {
      current = data[position]

      this.itemView.setOnClickListener {
        onItemSelected(current!!)
      }

      if (current!!.id == currentSupplier?.id) {
        ivSelected.visibility = View.VISIBLE
      } else {
        ivSelected.visibility = View.INVISIBLE
      }
      tvAddress.text = current!!.name
      tvName.text = current!!.name
    }

  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    val v = LayoutInflater.from(parent.context)
        .inflate(layoutId, parent, false)

    return ViewHolder(v)
  }

  override fun onBindViewHolder(
    holder: ViewHolder,
    position: Int
  ) {
    holder.bind(position)
  }

  override fun getItemCount(): Int {
    return data.size
  }

  fun setData(result: SearchSupplierModel) {

    data = result.data!!
    origin = result.data!!.toList()

    currentSupplier = result.currentSupplier

    notifyDataSetChanged()
  }

  fun addData(result: SearchSupplierModel) {
    data += result.data!!
    //origin += result.stores!!

    currentSupplier = result.currentSupplier

    notifyDataSetChanged()
  }

  fun setItemSelected(onItemSelected: (item: SupplierModel) -> Unit) {
    this.onItemSelected = onItemSelected
  }
}
