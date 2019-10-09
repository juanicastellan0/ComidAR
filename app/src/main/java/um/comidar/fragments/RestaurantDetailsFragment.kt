package um.comidar.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.ar.core.ArCoreApk
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dish_item_layout.view.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import um.comidar.R
import um.comidar.databinding.DishItemLayoutBinding
import um.comidar.databinding.RestaurantDetailsFragmentBinding
import um.comidar.helpers.ComidarApi
import um.comidar.models.Dish
import um.comidar.models.Restaurant
import java.io.IOException

class RestaurantDetailsFragment : Fragment() {

    private lateinit var listener: OnDishSelected

    companion object {
        private const val RESTAURANTMODEL = "model"

        fun newInstance(restaurant: Restaurant): RestaurantDetailsFragment {
            val args = Bundle()
            args.putSerializable(RESTAURANTMODEL, restaurant)
            val fragment = RestaurantDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnDishSelected) {
            listener = context
        } else {
            throw ClassCastException("$context must implement OnDishSelected.")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val restaurant = arguments!!.getSerializable(RESTAURANTMODEL) as Restaurant
        fetchDishesByRestauranId(restaurant.restaurantId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val restaurantDetailsFragmentBinding =
            RestaurantDetailsFragmentBinding.inflate(inflater, container, false)
        val model = arguments!!.getSerializable(RESTAURANTMODEL) as Restaurant
        val image = restaurantDetailsFragmentBinding.restaurantPhoto
        Picasso.get().load(model.imageTemporaryUrl).into(image)
        restaurantDetailsFragmentBinding.restaurant = model

        return restaurantDetailsFragmentBinding.root
    }

    internal inner class DishRecyclerViewAdapter(
        private val dishes: List<Dish>,
        context: Context
    ): RecyclerView.Adapter<DishViewHolder>() {
        override fun onBindViewHolder(
            viewHolder: DishViewHolder,
            position: Int
        ) {
            val dish = dishes[position]
            viewHolder.setData(dish)
            viewHolder.itemView.arButton.setOnClickListener { listener.onDishSelected(dish) }
        }

        private val layoutInflater = LayoutInflater.from(context)

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): DishViewHolder {
            val dishItemLayoutBinding =
                DishItemLayoutBinding.inflate(layoutInflater, viewGroup, false)

            dishItemLayoutBinding.arButton.visibility = maybeEnableArButton()

            return DishViewHolder(
                dishItemLayoutBinding.root,
                dishItemLayoutBinding
            )
        }

        override fun getItemCount() = dishes.size

        private fun maybeEnableArButton(): Int {
            val availability = ArCoreApk.getInstance().checkAvailability(activity)
            if (availability.isTransient) {
                Handler().postDelayed({
                    @Override
                    fun run() {
                        maybeEnableArButton()
                    }
                }, 200)
            }
            return if (availability.isSupported) View.VISIBLE else View.INVISIBLE
        }
    }

    internal inner class DishViewHolder(view: View,
                                        private val dishItemLayoutBinding:
                                        DishItemLayoutBinding
    ): RecyclerView.ViewHolder(view) {
        fun setData(dish: Dish) {
            dishItemLayoutBinding.dish = dish
        }
    }

    interface OnDishSelected {
        fun onDishSelected(dish: Dish)
    }

    private fun fetchDishesByRestauranId(restaurantId: Long) {
        ComidarApi.getDishesByRestaurantId(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val dishes = gson.fromJson(body, Array<Dish>::class.java)
                dishes?.let {
                    showDishes(dishes.toList())
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                print(e)
            }
        }, restaurantId)
    }

    private fun showDishes(dishes: List<Dish>) {
        Handler(Looper.getMainLooper()).post {
            kotlin.run {
                val activity = activity as Context
                val recyclerView = view?.findViewById<RecyclerView>(R.id.dishRecyclerView)
                recyclerView?.layoutManager = LinearLayoutManager(activity)
                recyclerView?.adapter = DishRecyclerViewAdapter(dishes, activity)
            }
        }
    }
}