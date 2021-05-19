package com.example.crudcomroom.ui.subscriberlist

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.crudcomroom.R
import com.example.crudcomroom.data.db.AppDataBase
import com.example.crudcomroom.data.db.dao.SubscriberDAO
import com.example.crudcomroom.data.db.entity.SubscriberEntity
import com.example.crudcomroom.extension.navigateWithAnimations
import com.example.crudcomroom.repository.DatabaseDataSource
import com.example.crudcomroom.repository.SubscriberRepository
import com.example.crudcomroom.ui.subscriber.SubscriberViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SubscriberListFragment : Fragment(R.layout.subscriber_list_fragment) {
    private val fabButton by lazy { view?.findViewById(R.id.fabAddSubscriber) as FloatingActionButton }
    private lateinit var recyclerView: RecyclerView
    private val viewModel: SubscriberListViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val subscriberDAO: SubscriberDAO =
                    AppDataBase.getInstance(requireContext()).subscriberDAO

                val repository: SubscriberRepository = DatabaseDataSource(subscriberDAO)
                return SubscriberListViewModel(repository) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recycler_subscribers)

        observeViewModelEvents()
        configureViewListeners()


    }


    private fun observeViewModelEvents() {
        viewModel.allSubscribersEvent.observe(viewLifecycleOwner) { allSubscribers ->
           val subscriberListAdapter = SubscriberListAdapter(allSubscribers).apply {
               onItemClick = { subscriber ->
                val directions = SubscriberListFragmentDirections
                    .actionSubscriberListFragmentToSubscriberFragment(subscriber)
                   findNavController().navigateWithAnimations(directions)
               }
           }

            with(recyclerView) {
                setHasFixedSize(true)
                adapter = subscriberListAdapter
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSubscribers()
    }

    private fun configureViewListeners() {
        fabButton.setOnClickListener {
            findNavController().navigateWithAnimations(R.id.action_subscriberListFragment_to_subscriberFragment)
        }
    }
}

