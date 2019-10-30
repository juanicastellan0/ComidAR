package um.comidar

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_main.*
import um.comidar.fragments.CategoriesFragment
import um.comidar.fragments.RestaurantDetailsFragment
import um.comidar.fragments.RestaurantsFragment
import um.comidar.models.Dish
import um.comidar.models.Restaurant

class MainActivity : FragmentActivity(),
    CategoriesFragment.OnCategorySelected,
    RestaurantsFragment.OnRestaurantSelected,
    RestaurantDetailsFragment.OnDishSelected
{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.setLogo(R.drawable.logo)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.mainLayout, CategoriesFragment.newInstance(), "categoryList")
                .commit()
        }
    }

    override fun onCategorySelected(categoryId: Long) {
        val restaurantsFragment = RestaurantsFragment.newInstance(categoryId)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.mainLayout, restaurantsFragment, "restaurantList")
            .addToBackStack(null)
            .commit()
    }

    override fun onRestaurantSelected(restaurant: Restaurant) {
        val detailsFragment = RestaurantDetailsFragment.newInstance(restaurant)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.mainLayout, detailsFragment, "restaurantDetails")
            .addToBackStack(null)
            .commit()
    }

    override fun onDishSelected(dish_gltf_temporary_url: String) {
        val intent = Intent(this, DishSceneformActivity::class.java)
        intent.putExtra("gltf_url", dish_gltf_temporary_url)
        startActivity(intent)
    }
}

