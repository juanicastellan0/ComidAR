package um.comidar

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode

class DishSceneformActivity : AppCompatActivity() {

    private var arFragment: ArFragment? = null
    private var modelRenderable: ModelRenderable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return
        }
        setContentView(R.layout.dish_sceneform_fragment)


        arFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment?
        // When you build a Renderable, Sceneform loads its resources in the background while returning
        // a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().
        val dishId: Long? = intent.getLongExtra("dish_id", 0)
        val asset = "$dishId.sfb"
        ModelRenderable.builder()
            .setSource(this, Uri.parse(asset))
            .build()
            .thenAccept { renderable -> modelRenderable = renderable }
            .exceptionally {
                val toast =
                    Toast.makeText(this, "Unable to load model renderable", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
                null
            }

        arFragment!!.setOnTapArPlaneListener { hitResult: HitResult, plane: Plane, motionEvent: MotionEvent ->
            if (modelRenderable == null) {
                return@setOnTapArPlaneListener
            }

            // Create the Anchor.
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment!!.arSceneView.scene)

            // Create the transformable model and add it to the anchor.
            val model = TransformableNode(arFragment!!.transformationSystem)
            model.setParent(anchorNode)
            model.renderable = modelRenderable
            model.select()
        }
    }

    companion object {
        private val TAG = DishSceneformActivity::class.java.simpleName
        private const val MIN_OPENGL_VERSION = 3.0

        @SuppressLint("ObsoleteSdkInt")
        fun checkIsSupportedDeviceOrFinish(activity: Activity): Boolean {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                Log.e(TAG, "Sceneform requires Android N or later")
                Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG)
                    .show()
                activity.finish()
                return false
            }
            val openGlVersionString =
                (activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
                    .deviceConfigurationInfo
                    .glEsVersion
            if (java.lang.Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
                Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later")
                Toast.makeText(
                    activity,
                    "Sceneform requires OpenGL ES 3.0 or later",
                    Toast.LENGTH_LONG
                )
                    .show()
                activity.finish()
                return false
            }
            return true
        }
    }
}