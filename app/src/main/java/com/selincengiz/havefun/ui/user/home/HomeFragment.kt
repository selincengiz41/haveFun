package com.selincengiz.havefun.ui.user.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.selincengiz.havefun.R
import com.selincengiz.havefun.common.Extensions.loadUrl
import com.selincengiz.havefun.databinding.FragmentHomeBinding
import com.selincengiz.havefun.ui.adapter.CategoryAdapter
import com.selincengiz.havefun.ui.adapter.ItemCategoryListener
import com.selincengiz.havefun.ui.user.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), ItemCategoryListener {
    @Inject
    lateinit var auth: FirebaseAuth
    private lateinit var binding : FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>()
    private val categoryAdapter by lazy { CategoryAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)
        binding.homeFunctions=this
        binding.profileLayout.background=null
        binding.categoriesRecycler.adapter=categoryAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
           profile.loadUrl(auth.currentUser?.photoUrl)
           name = auth.currentUser?.displayName
        }

        observe()
        viewModel.fireBaseCategoryLiveRead()
    }
    fun observe(){
        viewModel.categories.observe(viewLifecycleOwner){
            categoryAdapter.submitList(it)
        }
    }
    fun backClicked(){

    }

    fun logOut() {
        auth.signOut()
        findNavController().navigate(HomeFragmentDirections.actionGlobalSplashFragment())
    }

    override fun onClicked(category: String) {

    }

}