package um.comidar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import um.comidar.models.Category
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import um.comidar.helpers.ComidarApi
import java.io.IOException
class MainActivity : AppCompatActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        linearLayoutManager = LinearLayoutManager(this)

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
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
            }
        }, "category")
    }

    fun showCategories(categories: List<Category>) {
        Handler(Looper.getMainLooper()).post {
            kotlin.run {
                categoryRecyclerView.layoutManager = LinearLayoutManager(this)
                categoryRecyclerView.adapter = CategoryRecyclerViewAdapter(categories)
            }
        }
    }
}

