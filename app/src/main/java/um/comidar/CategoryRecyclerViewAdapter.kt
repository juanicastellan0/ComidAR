package um.comidar

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_category.view.*
import um.comidar.models.Category

class CategoryRecyclerViewAdapter(private val categories: List<Category>)
    : RecyclerView.Adapter<CategoryRecyclerViewAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.view.categoryName.text = category.name
    }

    override fun getItemCount(): Int = categories.size

    inner class CategoryViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}
