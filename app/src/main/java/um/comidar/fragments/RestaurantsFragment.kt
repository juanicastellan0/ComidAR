package um.comidar.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import um.comidar.R
import um.comidar.databinding.RestaurantItemLayoutBinding
import um.comidar.helpers.ComidarApi
import um.comidar.models.Restaurant
import java.io.IOException

class RestaurantsFragment : Fragment() {

    private lateinit var listener: OnRestaurantSelected

    companion object {
        private const val CATEGORYID = "categoryId"
        fun newInstance(categoryId: Long): RestaurantsFragment {
            val args = Bundle()
            args.putSerializable(CATEGORYID, categoryId)
            val fragment = RestaurantsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnRestaurantSelected) {
            listener = context
        } else {
            throw ClassCastException("$context must implement OnRestaurantSelected.")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fetchRestaurantsByCategoryId(arguments!!.getSerializable(CATEGORYID) as Long)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.restaurants_list_fragment, container,false)
    }

    internal inner class RestaurantRecyclerViewAdapter(
        private val restaurants: List<Restaurant>,
        context: Context
    ): RecyclerView.Adapter<RestaurantViewHolder>() {
        override fun onBindViewHolder(
            viewHolder: RestaurantViewHolder,
            position: Int
        ) {
            val restaurant = restaurants[position]
            viewHolder.setData(restaurant)
            viewHolder.itemView.setOnClickListener { listener.onRestaurantSelected(restaurant) }
        }

        private val layoutInflater = LayoutInflater.from(context)

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RestaurantViewHolder {
            val restaurantItemLayoutBinding =
                RestaurantItemLayoutBinding.inflate(layoutInflater, viewGroup, false)
            return RestaurantViewHolder(
                restaurantItemLayoutBinding.root,
                restaurantItemLayoutBinding
            )
        }

        override fun getItemCount() = restaurants.size
    }

    internal inner class RestaurantViewHolder(view: View,
                                            private val restaurantItemLayoutBinding:
                                            RestaurantItemLayoutBinding
    ) : RecyclerView.ViewHolder(view) {

        private var image: ImageView? = null

        init {
            image = itemView.findViewById(R.id.restaurantPhoto)
        }

        fun setData(restaurant: Restaurant) {
            Picasso.get().load(restaurant.imageTemporaryUrl).into(image)
            restaurantItemLayoutBinding.restaurant = restaurant
        }
    }

    interface OnRestaurantSelected {
        fun onRestaurantSelected(restaurant: Restaurant)
    }

    private fun fetchRestaurantsByCategoryId(categoryId: Long) {
        ComidarApi.getRestaurantsByCategoryId(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val restaurants = gson.fromJson(body, Array<Restaurant>::class.java)
                restaurants?.let {
                    showRestaurants(restaurants.toList())
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                print(e)
            }
        }, categoryId)
    }

    private fun showRestaurants(restaurants: List<Restaurant>) {
        Handler(Looper.getMainLooper()).post {
            kotlin.run {
                val activity = activity as Context
                val recyclerView = view?.findViewById<RecyclerView>(R.id.restaurantRecyclerView)

                recyclerView?.layoutManager = LinearLayoutManager(activity)
                recyclerView?.adapter = RestaurantRecyclerViewAdapter(restaurants, activity)

                val dividerItemDecoration = DividerItemDecoration(
                    recyclerView?.context,
                    (recyclerView?.layoutManager as LinearLayoutManager).orientation
                )
                recyclerView?.addItemDecoration(dividerItemDecoration)
            }
        }
    }
}