package vsukharev.anytypeadapter.sample.common.lifecycle

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding

/**
 * wrapper above [FragmentViewBindingProperty]
 */
fun <T: ViewBinding> fragmentViewBinding(
    bindingInitializer: (LayoutInflater) -> T
): FragmentViewBindingProperty<T> = FragmentViewBindingProperty(bindingInitializer)

/**
 * wrapper above [ActivityViewBindingProperty]
 */
fun <T: ViewBinding> activityViewBinding(
    bindingInitializer: (LayoutInflater) -> T
): ActivityViewBindingProperty<T> = ActivityViewBindingProperty(bindingInitializer)