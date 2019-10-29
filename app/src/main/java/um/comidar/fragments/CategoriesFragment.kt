package um.comidar.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response

import um.comidar.R
import um.comidar.models.Category
import um.comidar.databinding.CategoryItemLayoutBinding
import um.comidar.helpers.ComidarApi
import um.comidar.helpers.GridItemDecoration
import java.io.IOException

class CategoriesFragment : Fragment() {

    private lateinit var listener: OnCategorySelected

    companion object {
        fun newInstance() = CategoriesFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnCategorySelected) {
            listener = context
        } else {
            throw ClassCastException("$context must implement OnCategorySelected.")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fetchCategories()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.categories_list_fragment, container,false)
    }

    internal inner class CategoryRecyclerViewAdapter(
        private val categories: List<Category>,
        context: Context
    ) : RecyclerView.Adapter<CategoryViewHolder>() {
        override fun onBindViewHolder(
            viewHolder: CategoryViewHolder,
            position: Int
        ) {
            val category : Category = categories[position]
            viewHolder.setData(category)
            viewHolder.itemView.findViewById<ImageButton>(R.id.categoryImage).setOnClickListener {
                listener.onCategorySelected(category.categoryId)
            }
        }

        private val layoutInflater = LayoutInflater.from(context)

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CategoryViewHolder {
            val recyclerItemCategoryListBinding =
                CategoryItemLayoutBinding.inflate(layoutInflater, viewGroup, false)
            return CategoryViewHolder(
                recyclerItemCategoryListBinding.root,
                recyclerItemCategoryListBinding
            )
        }

        override fun getItemCount() = categories.size
    }

    internal inner class CategoryViewHolder(view: View,
                                            private val recyclerItemCategoryListBinding:
                                            CategoryItemLayoutBinding
    ) : RecyclerView.ViewHolder(view) {

        private var image: ImageView? = null

        init {
            image = itemView.findViewById(R.id.categoryImage)
        }

        fun setData(category: Category) {
            Picasso.get().load(category.imageTemporaryUrl).into(image)
            recyclerItemCategoryListBinding.category = category
        }
    }

    interface OnCategorySelected {
        fun onCategorySelected(categoryId: Long)
    }

    private fun fetchCategories() {
        ComidarApi.getList(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val categories = gson.fromJson(body, Array<Category>::class.java)
                categories?.let {
                    showCategories(categories.toList())
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                print(e)
            }
        }, "category")
    }

    private fun showCategories(categories: List<Category>) {
        Handler(Looper.getMainLooper()).post {
            kotlin.run {
                val activity = activity as Context
                val recyclerView = view?.findViewById<RecyclerView>(R.id.categoryRecyclerView)
                recyclerView?.layoutManager = GridLayoutManager(activity,2)
                recyclerView?.addItemDecoration(GridItemDecoration(10,2))
                recyclerView?.adapter = CategoryRecyclerViewAdapter(categories, activity)
            }
        }
    }
}
