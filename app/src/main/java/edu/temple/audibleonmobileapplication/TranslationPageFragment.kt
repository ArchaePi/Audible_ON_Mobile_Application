package edu.temple.audibleonmobileapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class TranslationPageFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_translation_page, container, false)
        val button = root.findViewById<Button>(R.id.back_to_home)
        val done_button = root.findViewById<Button>(R.id.done)

        button.setOnClickListener {
            val fragment = HomePageFragment()
            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, fragment)
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }

        done_button.setOnClickListener {
            val fragment = DisplayPageFragment()
            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, fragment)
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }
        // Inflate the layout for this fragment
        return root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TranslationPageFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}