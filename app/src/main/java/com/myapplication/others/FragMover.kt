package com.myapplication.others

import androidx.fragment.app.FragmentManager
import com.myapplication.base.BaseFragment
import java.lang.Exception

object FragMover {

    fun addFrag(fragmentManager: FragmentManager, layoutId: Int, fragment: BaseFragment) {

        removeFragFromBackStack(fragmentManager)
        fragmentManager.beginTransaction()
            .add(layoutId, fragment)
            .commit()

    }

    private fun removeFragFromBackStack(manager: FragmentManager): Boolean {
        try {
            if (manager.backStackEntryCount > 0) {
                val first: FragmentManager.BackStackEntry = manager.getBackStackEntryAt(0)
                manager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun replaceFragmentByTag(
        fragmentManager: FragmentManager,
        layoutId: Int,
        fragment: BaseFragment,
        tag: String
    ) {
        fragmentManager.beginTransaction()
            .add(layoutId, fragment, tag)
            .addToBackStack(null)
            .commit()
    }

    fun replaceFrag(fragmentManager: FragmentManager, layoutId: Int, fragment: BaseFragment) {
        fragmentManager.beginTransaction()
            .replace(layoutId, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun replaceFragWithoutAddingToBackStack(
        fragmentManager: FragmentManager,
        layoutId: Int,
        fragment: BaseFragment
    ) {
        fragmentManager.beginTransaction()
            .replace(layoutId, fragment)
            .commit()
    }
}