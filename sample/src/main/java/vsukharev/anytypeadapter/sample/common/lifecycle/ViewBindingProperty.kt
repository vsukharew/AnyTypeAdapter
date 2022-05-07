package vsukharev.anytypeadapter.sample.common.lifecycle

import android.view.LayoutInflater
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding

open class ViewBindingProperty<T: ViewBinding>(
    protected val bindingInitializer: (LayoutInflater) -> T
) : DefaultLifecycleObserver {
    protected var binding: T? = null
    protected var lifecycle: Lifecycle? = null

    override fun onDestroy(owner: LifecycleOwner) {
        lifecycle?.removeObserver(this)
        lifecycle = null
        binding = null
    }
}