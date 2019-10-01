package um.comidar

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import um.comidar.fragments.CategoriesFragment
import um.comidar.fragments.RestaurantDetailsFragment
import um.comidar.fragments.RestaurantsFragment
import um.comidar.models.Category
import um.comidar.models.Restaurant

class MainActivity : FragmentActivity(),
    CategoriesFragment.OnCategorySelected,
    RestaurantsFragment.OnRestaurantSelected {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.mainLayout, CategoriesFragment.newInstance(), "categoryList")
                .commit()
        }
    }

    override fun onCategorySelected(category: Category) {
        val detailsFragment = RestaurantsFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainLayout, detailsFragment, "restaurantList")
            .addToBackStack(null)
            .commit()
    }

    override fun onRestaurantSelected(restaurant: Restaurant) {
        val detailsFragment = RestaurantDetailsFragment.newInstance(restaurant)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainLayout, detailsFragment, "restaurantDetails")
            .addToBackStack(null)
            .commit()
    }
}

