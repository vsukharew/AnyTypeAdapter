package vsukharev.anytypeadapter.sample.common.lifecycle

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Delegate property for initializing fragment layout views
 */
class FragmentViewBindingProperty<T : ViewBinding>(
    bindingInitializer: (LayoutInflater) -> T
) : ViewBindingProperty<T>(bindingInitializer), ReadOnlyProperty<Fragment, T> {

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return binding ?: run {
            thisRef.viewLifecycleOwner.lifecycle.let {
                it.addObserver(this)
                lifecycle = it
            }
            bindingInitializer.invoke(thisRef.layoutInflater).also { binding = it }
        }
    }
}