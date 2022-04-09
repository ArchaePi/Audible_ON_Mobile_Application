package edu.temple.audibleonmobileapplication

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class TestFragment1 : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        //val textView = fin
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_test1, container, false)
        val textView = root.findViewById<TextView>(R.id.test_text1)
        val button = root.findViewById<Button>(R.id.test_button1)

        button.setOnClickListener {
                val fragment = TestFragment2()
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
            TestFragment1().apply {
                arguments = Bundle().apply {
                }
            }
    }
}