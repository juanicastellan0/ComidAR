package um.comidar.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import um.comidar.R
import um.comidar.databinding.RestaurantDetailsFragmentBinding
import um.comidar.models.Restaurant

class RestaurantDetailsFragment : Fragment() {
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val restaurantDetailsFragmentBinding =
            RestaurantDetailsFragmentBinding.inflate(inflater, container, false)
        val model = arguments!!.getSerializable(RESTAURANTMODEL) as Restaurant
        val image = restaurantDetailsFragmentBinding.root.findViewById<ImageView>(R.id.restaurantPhoto)
        Picasso.get().load(model.imageTemporaryUrl).into(image)
        restaurantDetailsFragmentBinding.restaurant = model
        return restaurantDetailsFragmentBinding.root
    }
}