package um.comidar.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import um.comidar.R
import um.comidar.databinding.RestaurantItemLayoutBinding
import um.comidar.models.Restaurant

class RestaurantsFragment : Fragment() {

    private lateinit var imageResIds: IntArray
    private lateinit var restaurantNames: Array<String>
    private lateinit var listener: OnRestaurantSelected

    companion object {
        fun newInstance() = RestaurantsFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnRestaurantSelected) {
            listener = context
        } else {
            throw ClassCastException("$context must implement OnRestaurantSelected.")
        }

        val resources = context.resources
        restaurantNames = resources.getStringArray(R.array.categoryNames)
        val typedArray = resources.obtainTypedArray(R.array.images)
        val imageCount = restaurantNames.size
        imageResIds = IntArray(imageCount)
        for (i in 0 until imageCount) {
            imageResIds[i] = typedArray.getResourceId(i, 0)
        }
        typedArray.recycle()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(
            R.layout.restaurants_list_fragment, container,
            false)
        val activity = activity as Context
        val recyclerView = view.findViewById<RecyclerView>(R.id.restaurantRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = RestaurantRecyclerViewAdapter(activity)
        return view
    }

    internal inner class RestaurantRecyclerViewAdapter(context: Context)
        : RecyclerView.Adapter<RestaurantViewHolder>() {
        override fun onBindViewHolder(
            viewHolder: RestaurantViewHolder,
            position: Int
        ) {
            val category = Restaurant(imageResIds[position],
                            0,
                                    restaurantNames[position],
                                    "", "", "", "", "")
            viewHolder.setData(category)
            viewHolder.itemView.setOnClickListener { listener.onRestaurantSelected(category) }
        }

        private val layoutInflater = LayoutInflater.from(context)

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RestaurantViewHolder {
            val recyclerItemCategoryListBinding =
                RestaurantItemLayoutBinding.inflate(layoutInflater, viewGroup, false)
            return RestaurantViewHolder(
                recyclerItemCategoryListBinding.root,
                recyclerItemCategoryListBinding
            )
        }

        override fun getItemCount() = restaurantNames.size
    }

    internal inner class RestaurantViewHolder(view: View,
                                            private val recyclerItemCategoryListBinding:
                                            RestaurantItemLayoutBinding
    ) : RecyclerView.ViewHolder(view) {

        fun setData(restaurant: Restaurant) {
            recyclerItemCategoryListBinding.restaurant = restaurant
        }
    }

    interface OnRestaurantSelected {
        fun onRestaurantSelected(restaurant: Restaurant)
    }

}