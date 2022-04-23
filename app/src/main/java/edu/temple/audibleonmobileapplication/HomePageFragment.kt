package edu.temple.audibleonmobileapplication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


class HomePageFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home_page, container, false)
        val button = root.findViewById<Button>(R.id.nav_translation)
        val exit_button = root.findViewById<Button>(R.id.exit_button)

        button.setOnClickListener {
                val intent = Intent(activity, HandsActivity::class.java)
                startActivity(intent)
            //val fragment = DisplayPageFragment()
            //val fragmentManager = activity?.supportFragmentManager
            //val fragmentTransaction = fragmentManager?.beginTransaction()
            //fragmentTransaction?.replace(R.id.fragmentContainerView, fragment)
            //fragmentTransaction?.addToBackStack(null)
            //fragmentTransaction?.commit()
        }

        exit_button.setOnClickListener {
            activity?.finishAndRemoveTask();
        }
        // Inflate the layout for this fragment
        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomePageFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}