package um.comidar.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response

import um.comidar.R
import um.comidar.models.Category
import um.comidar.databinding.CategoryItemLayoutBinding
import um.comidar.helpers.ComidarApi
import java.io.IOException

class CategoriesFragment : Fragment() {

    private lateinit var imageResIds: IntArray
    private lateinit var categoryNames: Array<String>
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

        val resources = context.resources
        categoryNames = resources.getStringArray(R.array.categoryNames)
        val typedArray = resources.obtainTypedArray(R.array.images)
        val imageCount = categoryNames.size
        imageResIds = IntArray(imageCount)
        for (i in 0 until imageCount) {
            imageResIds[i] = typedArray.getResourceId(i, 0)
        }
        typedArray.recycle()
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

    internal inner class CategoryRecyclerViewAdapter(context: Context)
        : RecyclerView.Adapter<CategoryViewHolder>() {
        override fun onBindViewHolder(
            viewHolder: CategoryViewHolder,
            position: Int
        ) {
            val category = Category(imageResIds[position], 0, categoryNames[position])
            viewHolder.setData(category)
            viewHolder.itemView.setOnClickListener { listener.onCategorySelected(category) }
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

        override fun getItemCount() = categoryNames.size
    }

    internal inner class CategoryViewHolder(view: View,
                                            private val recyclerItemCategoryListBinding:
                                            CategoryItemLayoutBinding
    ) : RecyclerView.ViewHolder(view) {

        fun setData(category: Category) {
            recyclerItemCategoryListBinding.category = category
        }
    }

    interface OnCategorySelected {
        fun onCategorySelected(category: Category)
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
                Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
            }
        }, "category")
    }

    private fun showCategories(categories: List<Category>) {
        Handler(Looper.getMainLooper()).post {
            kotlin.run {
                val activity = activity as Context
                val recyclerView = view?.findViewById<RecyclerView>(R.id.categoryRecyclerView)
                recyclerView?.layoutManager = LinearLayoutManager(activity)
                recyclerView?.adapter = CategoryRecyclerViewAdapter(activity)
            }
        }
    }
}
