package com.maxim.musicplayer.cope.presentation

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

interface Screen {
    fun show(fragmentManager: FragmentManager, containerId: Int, tabLayout: View)

    abstract class Add(private val fragmentClass: Class<out Fragment>) : Screen {
        override fun show(fragmentManager: FragmentManager, containerId: Int, tabLayout: View) {
            fragmentManager.beginTransaction()
                .add(containerId, fragmentClass.getDeclaredConstructor().newInstance())
                .addToBackStack(fragmentClass.simpleName)
                .commit()
            tabLayout.visibility = View.GONE
        }
    }

    abstract class AddSingleton(private val fragmentClass: Class<out Fragment>) : Screen {
        override fun show(fragmentManager: FragmentManager, containerId: Int, tabLayout: View) {
            val index = fragmentManager.backStackEntryCount - 1
            if ((index >= 0 && fragmentManager.getBackStackEntryAt(index).name != fragmentClass.simpleName) || index == -1) {
                fragmentManager.beginTransaction()
                    .add(containerId, fragmentClass.getDeclaredConstructor().newInstance())
                    .addToBackStack(fragmentClass.simpleName)
                    .commit()
            }
            tabLayout.visibility = View.GONE
        }
    }

    abstract class Replace(private val fragmentClass: Class<out Fragment>) : Screen {
        override fun show(fragmentManager: FragmentManager, containerId: Int, tabLayout: View) {
            fragmentManager.beginTransaction()
                .replace(containerId, fragmentClass.getDeclaredConstructor().newInstance())
                .commit()
            tabLayout.visibility = View.GONE
        }
    }

    abstract class Bottom(private val fragmentClass: Class<out BottomSheetDialogFragment>) :
        Screen {
        override fun show(fragmentManager: FragmentManager, containerId: Int, tabLayout: View) {
            fragmentClass.getDeclaredConstructor().newInstance()
                .show(fragmentManager, fragmentClass.simpleName)
        }
    }

    object Pop : Screen {
        override fun show(fragmentManager: FragmentManager, containerId: Int, tabLayout: View) {
            fragmentManager.popBackStack()
            tabLayout.visibility = View.VISIBLE
        }
    }
}