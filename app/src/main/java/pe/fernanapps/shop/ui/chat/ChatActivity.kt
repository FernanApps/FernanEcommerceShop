package pe.fernanapps.shop.ui.chat

import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pe.fernanapps.shop.ChatViewModel
import pe.fernanapps.shop.databinding.ActivityChatBinding
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.ui.base.BaseActivity

@AndroidEntryPoint
class ChatActivity : BaseActivity<ActivityChatBinding>(ActivityChatBinding::inflate) {
    private val viewModel by viewModels<ChatViewModel>()

    private lateinit var messageAdapter: MessagesListAdapter

    override fun initViews() {
        with(bin) {

        }
    }

    override fun initObserves() {
        with(viewModel) {
            chats.observe(this@ChatActivity){
                when(it){
                    is DataState.Error -> {}
                    DataState.Finished -> {
                        bin.btnSend.isEnabled = true
                        bin.loading.root.isVisible = false
                        bin.rvMessages.isVisible = true

                    }
                    DataState.Loading -> {}
                    is DataState.Success -> {
                        viewModel.setChats(it.data)
                    }
                    else -> Unit
                }
            }



            userIdStatus.observe(this@ChatActivity) {
                when (it) {
                    is DataState.Error -> {

                    }

                    is DataState.Finished -> {

                    }

                    is DataState.Loading -> {}
                    is DataState.Success -> {
                        messageAdapter = MessagesListAdapter(it.data)
                        bin.rvMessages.adapter = messageAdapter
                    }
                    else -> Unit
                }
            }

            messageList.observe(this@ChatActivity) {
                messageAdapter.submitList(it)
            }

            getChats()
            subscribeToMessages()
        }
    }


    override fun initActions() {
        with(bin) {
            btnSend.setOnClickListener {
                val message = etMessage.text.toString()
                etMessage.setText("")
                if (message.isNotEmpty()) {
                    viewModel.sendMessage(message)
                }
            }
        }
    }
}