package com.example.reflexionai.views.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.reflexionai.databinding.FragmentProfileBinding
import com.example.reflexionai.models.entities.UserProfile
import com.example.reflexionai.views.activities.UserLogin

class ProfileFragment : Fragment() {

    private var mBinding : FragmentProfileBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profile = UserProfile.profile
        mBinding?.txtName?.text = profile?.name
        mBinding?.txtAddress?.text = profile?.address
        mBinding?.txtEmail?.text = profile?.email
        mBinding?.txtPhoneNo?.text = profile?.phoneNo


        mBinding?.imgEdit?.setOnClickListener {
            val intent = Intent(requireActivity()   , UserLogin::class.java)
//            intent.putExtra("ChangeUser", true)
            val sharedPreferences = context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            var profileJson: String?
            profileJson = null
            val editor = sharedPreferences?.edit()
            editor?.putString("profile", null)
            editor?.apply()
            startActivity(intent)
        }

    }



}