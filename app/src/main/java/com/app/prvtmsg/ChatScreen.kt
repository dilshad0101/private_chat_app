package com.app.prvtmsg

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    selectedChannel: String,
    onExitAttempt: () -> Unit
){
    val messageHandlerViewModel = MessageHandlerViewModel()
    messageHandlerViewModel.startListeningForMessages()
    var messageField by remember{ mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "HEHEHE",
                        color = MaterialTheme.colorScheme.onPrimary,
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
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.80f)
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

            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(17.dp)
                    .fillMaxWidth()){

                items(items = messageHandlerViewModel.fetchedMessages.value
                    .sortedBy { it.time } ){
                    ChatBox(
                        isAdmin = selectedChannel != it.channel,
                        message = it.content,
                        time = FormatDate(it.time.toDate())
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
