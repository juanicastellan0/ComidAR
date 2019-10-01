package um.comidar.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import um.comidar.models.Category
import um.comidar.databinding.CategoryDetailsFragmentBinding

class CategoryDetailsFragment : Fragment() {
    companion object {
        private const val CATEGORYMODEL = "model"

        fun newInstance(category: Category): CategoryDetailsFragment {
            val args = Bundle()
            args.putSerializable(CATEGORYMODEL, category)
            val fragment = CategoryDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentCategoryDetailsBinding =
            CategoryDetailsFragmentBinding.inflate(inflater, container, false)
        val model = arguments!!.getSerializable(CATEGORYMODEL) as Category
        fragmentCategoryDetailsBinding.category = model
        return fragmentCategoryDetailsBinding.root
    }
}