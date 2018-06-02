package es.demokt.kotlindemoproject.modules.product.result

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import es.demokt.kotlindemoproject.R

class ProductResultsAdapter : RecyclerView.Adapter<ProductResultsAdapter.ViewHolder>() {

  lateinit var onItemSelected: (item: Product) -> Unit
  var lists: List<Product> = arrayListOf()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val v = LayoutInflater.from(parent.context).inflate(R.layout.list_product_result, parent, false)

    return ViewHolder(v)
  }

  override fun getItemCount(): Int {
    return lists.size
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.setIsRecyclable(false)
    holder.bindData(lists[position])
  }

  fun setList(data: List<Product>) {
    this.lists = data
    notifyDataSetChanged()
  }

  fun setItemSelected(onItemSelected: (item: Product) -> Unit) {
    this.onItemSelected = onItemSelected
  }

  inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bindData(_list: Product) {
      val name = itemView.findViewById<TextView>(R.id.product_name)
      val description = itemView.findViewById<TextView>(R.id.product_description)
      val price = itemView.findViewById<TextView>(R.id.product_price)
      val img = itemView.findViewById<ImageView>(R.id.product_image)
      val cardView = itemView.findViewById<CardView>(R.id.card_view)

      name.text = _list.name
      description.text = _list.productObservation
      price.text = _list.price.toString()
      val images = _list.images
      if (images != null && images.isNotEmpty())
        Picasso.get().load(images[0]).into(img)

      // onClick product
      cardView.setOnClickListener {
        onItemSelected(_list)
      }
    }

  }
}