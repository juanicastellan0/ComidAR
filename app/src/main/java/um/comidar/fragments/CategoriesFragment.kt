package um.comidar.fragments

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.categories_fragment.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response

import um.comidar.R
import um.comidar.adapters.CategoryRecyclerViewAdapter
import um.comidar.helpers.ComidarApi
import um.comidar.models.Category
import java.io.IOException

class CategoriesFragment : Fragment() {

    private lateinit var listener: OnCategorySelected

    companion object {
        fun newInstance() = CategoriesFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is OnCategorySelected) {
            listener = context
        } else {
            throw ClassCastException(context.toString() + " must implement OnCategorySelected.")
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.categories_fragment, container, false)
    }

    private fun showCategories(categories: List<Category>) {
        Handler(Looper.getMainLooper()).post {
            kotlin.run {
                categoryRecyclerView.layoutManager = LinearLayoutManager(activity)
                categoryRecyclerView.adapter =
                    CategoryRecyclerViewAdapter(categories)
            }
        }
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

    interface OnCategorySelected {
        fun onCategorySelected(category: Category)
    }
}
