package vsukharev.anytypeadapter.sample.common.lifecycle

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Delegate property for initializing activity layout views
 */
class ActivityViewBindingProperty<T: ViewBinding>(
    bindingInitializer: (LayoutInflater) -> T
) : ViewBindingProperty<T>(bindingInitializer), ReadOnlyProperty<AppCompatActivity, T> {

    override fun getValue(thisRef: AppCompatActivity, property: KProperty<*>): T {
        return binding ?: run {
            thisRef.lifecycle.let {
                it.addObserver(this)
                lifecycle = it
            }
            bindingInitializer.invoke(thisRef.layoutInflater).also { binding = it }
        }
    }
}