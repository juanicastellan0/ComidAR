package um.comidar

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import um.comidar.fragments.CategoriesFragment
import um.comidar.fragments.CategoryDetailsFragment
import um.comidar.models.Category

class MainActivity : FragmentActivity(), CategoriesFragment.OnCategorySelected {

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
        val detailsFragment = CategoryDetailsFragment.newInstance(category)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainLayout, detailsFragment, "categoryDetails")
            .addToBackStack(null)
            .commit()
    }
}

