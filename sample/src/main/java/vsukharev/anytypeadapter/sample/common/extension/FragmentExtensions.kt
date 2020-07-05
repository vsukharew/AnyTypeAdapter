package vsukharev.anytypeadapter.sample.common.extension

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.manager.SupportRequestManagerFragment

fun FragmentManager.getFirstVisibleFragment(): Fragment? {
    return fragments.firstOrNull { !it.isHidden && it !is SupportRequestManagerFragment }
}

fun FragmentTransaction.commitShowAndHide(
    fragmentToShow: Fragment,
    fragmentToHide: Fragment
): Int {
    return show(fragmentToShow)
        .hide(fragmentToHide)
        .commit()
}