package es.demokt.kotlindemoproject.modules.store

import android.support.v7.widget.RecyclerView
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import es.demokt.kotlindemoproject.R
import es.demokt.kotlindemoproject.modules.home.UserStore
import kotlinx.android.synthetic.main.item_store.view.ivImage
import kotlinx.android.synthetic.main.item_store.view.ivSelected
import kotlinx.android.synthetic.main.item_store.view.tvAddress
import kotlinx.android.synthetic.main.item_store.view.tvName

/**
 * Created by aluengo on 3/4/18.
 */

class StoreAdapter() : RecyclerView.Adapter<StoreAdapter.ViewHolder>() {
  private lateinit var onItemSelected: (item: UserStore) -> Unit
  var currentStore: UserStore? = null
  private val layoutId: Int
  var data: List<UserStore> = emptyList()

  lateinit var origin: List<UserStore>

  init {
    this.data = emptyList()
    this.layoutId = R.layout.item_store
  }

  inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var tvAddress: TextView = view.tvAddress
    var tvName: TextView = view.tvName
    var ivImage: ImageView = view.ivImage

    var ivSelected: ImageView = view.ivSelected

    private var current: UserStore? = null
    fun bind(position: Int) {
      current = data[position]

      this.itemView.setOnClickListener {
        onItemSelected(current!!)
      }

      if (current!!.id == currentStore?.id) {
        ivSelected.visibility = View.VISIBLE
      } else {
        ivSelected.visibility = View.INVISIBLE
      }
      tvAddress.text = current!!.location
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

  fun setStores(result: SearchStoreModel) {

    data = result.stores!!
    origin = result.stores!!

    currentStore = result.currentStore

    notifyDataSetChanged()
  }

  fun addStores(result: SearchStoreModel) {
    data += result.stores!!
    //origin += result.stores!!

    currentStore = result.currentStore

    notifyDataSetChanged()
  }

  fun setItemSelected(onItemSelected: (item: UserStore) -> Unit) {
    this.onItemSelected = onItemSelected
  }
}
