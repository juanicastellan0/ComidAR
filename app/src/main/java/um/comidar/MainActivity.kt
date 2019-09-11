package um.comidar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import um.comidar.models.Category
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import um.comidar.adapters.CategoryRecyclerViewAdapter
import um.comidar.helpers.ComidarApi
import java.io.IOException
class MainActivity : AppCompatActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

