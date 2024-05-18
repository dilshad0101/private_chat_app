package com.app.prvtmsg.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.prvtmsg.ui.items.DefaultTextField
import com.app.prvtmsg.util.FormatDate
import com.app.prvtmsg.MessageHandlerViewModel
import com.app.prvtmsg.ui.items.ChatBox

import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    selectedChannel: String,
    chatId:String,
    onExitAttempt: () -> Unit,
){
    val messageHandlerViewModel = MessageHandlerViewModel(chatId)
    messageHandlerViewModel.startListeningForMessages(selectedChannel)
    var messageField by remember{ mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = chatId,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onExitAttempt.invoke()
                    }) {
                        Icon(
                            (Icons.Default.KeyboardArrowLeft),
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ){paddingValues ->
        if (messageHandlerViewModel.fetchedMessages.value.isEmpty()){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }else{
            var lastPrintedTimestamp: Timestamp? = null
            val oneHourInMillis = 60 * 60 * 1000
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(17.dp)
                    .fillMaxWidth()){

                items(items = messageHandlerViewModel.fetchedMessages.value
                    .filter { it.content != "initial_text" }
                    .sortedBy { it.time })
                {

                    val sdf = SimpleDateFormat("dd MMM yyyy - hh", Locale.getDefault())

                    if (lastPrintedTimestamp == null ||
                        it.time.toDate().time - lastPrintedTimestamp!!.toDate().time >= oneHourInMillis) {
                        Text(text = sdf.format(it.time.toDate())+" O'Clock",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(0.6f),
                            modifier = Modifier.fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally)
                                .padding(20.dp)
                            )
                        lastPrintedTimestamp = it.time

                    }
                    ChatBox(
                        isAdmin = selectedChannel != it.channel,
                        message = it.content,
                        time = " User $selectedChannel"
                    )
                    Spacer(modifier = Modifier
                        .height(10.dp)
                        .fillMaxWidth())
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.BottomStart)
            ){
                DefaultTextField(
                    value = messageField,
                    onValueChange ={
                        messageField = it
                    } ,
                    placeholderText = "Message.." ,
                    isError = false,
                    isNumberKeyboard = false,
                    modifier = Modifier,
                    isChatKeyboard = true,
                    onSend = {
                        CoroutineScope(Dispatchers.IO).launch {
                            if (messageField.isNotBlank()){
                                messageHandlerViewModel.putMessage(
                                    Message(channel = selectedChannel,
                                        time = Timestamp.now(), content = messageField)
                                )

                                messageField = ""
                            }
                        }
                    }
                    )

            }
        }
    }
}
data class Message(
    val time: Timestamp,
    val content: String,
    val channel: String
)
