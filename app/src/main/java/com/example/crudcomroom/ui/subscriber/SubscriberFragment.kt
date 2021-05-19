package com.example.crudcomroom.ui.subscriber

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.crudcomroom.R
import com.example.crudcomroom.data.db.AppDataBase
import com.example.crudcomroom.data.db.dao.SubscriberDAO
import com.example.crudcomroom.extension.hideKeyBoard
import com.example.crudcomroom.repository.DatabaseDataSource
import com.example.crudcomroom.repository.SubscriberRepository
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class SubscriberFragment : Fragment(R.layout.subscriber_fragment) {
    private val inputName by lazy { view?.findViewById(R.id.input_name) as TextInputEditText }
    private val inputEmail by lazy { view?.findViewById(R.id.input_email) as TextInputEditText }
    private val buttonSubscriber by lazy { view?.findViewById(R.id.button_subscriber) as Button }
    private val buttonDelete by lazy { view?.findViewById(R.id.button_delete) as Button }
    private val viewModel: SubscriberViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val subscriberDAO: SubscriberDAO =
                    AppDataBase.getInstance(requireContext()).subscriberDAO
                val repository: SubscriberRepository = DatabaseDataSource(subscriberDAO)
                return SubscriberViewModel(repository) as T
            }

        }
    }

    private val args: SubscriberFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args.subscriber?.let { subscriber ->
            buttonSubscriber.text = getString(R.string.subscriber_button_update)
            inputName.setText(subscriber.name)
            inputEmail.setText(subscriber.email)

            buttonDelete.visibility = View.VISIBLE
        }

        observeEvents()
        setListeners()
    }


    private fun observeEvents() {
        viewModel.subscriberStateEventData.observe(viewLifecycleOwner) { subscriberState ->
            when (subscriberState) {
                is SubscriberViewModel.SubscriberState.Inserted,
                is SubscriberViewModel.SubscriberState.Updated,
                is SubscriberViewModel.SubscriberState.Deleted -> {
                    clearFields()
                    hideKeyBoard()
                    requireView().requestFocus()
                    findNavController().popBackStack()
                }
            }
        }

        viewModel.messageStateEventData.observe(viewLifecycleOwner) { stringResId ->
            Snackbar.make(requireView(), stringResId, Snackbar.LENGTH_LONG).show()
        }

    }


    private fun clearFields() {
        inputName.text?.clear()
        inputEmail.text?.clear()
    }

    private fun hideKeyBoard() {
        val parentActivity = requireActivity()
        if (parentActivity is AppCompatActivity) {
            parentActivity.hideKeyBoard()
        }
    }

    private fun setListeners() {
        buttonSubscriber.setOnClickListener {
            val name = inputName.text.toString()
            val email = inputEmail.text.toString()

            viewModel.addOrUpadateSubscriber(name, email , args.subscriber?.id ?: 0)
        }

        buttonDelete.setOnClickListener {
            viewModel.removeSubscriber(args.subscriber?.id ?: 0)
        }
    }

}


