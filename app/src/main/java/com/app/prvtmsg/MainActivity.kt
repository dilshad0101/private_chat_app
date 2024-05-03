package com.app.prvtmsg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import com.app.prvtmsg.ui.theme.PrivateMessagingTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try{
            val db = Firebase.firestore
            val settings = FirebaseFirestoreSettings.Builder()
                .build()
            db.firestoreSettings = settings
            FirebaseApp.initializeApp(applicationContext)
        }catch (e:Exception){
            e.printStackTrace()
        }
        setContent {
            PrivateMessagingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var isOnChatScreen by remember{ mutableStateOf(false) }
                    var selectedChannel by rememberSaveable {
                        mutableStateOf("2")
                    }
                    if (isOnChatScreen){
                        ChatScreen(selectedChannel = selectedChannel){
                            finishAffinity()
                            moveTaskToBack(true);
                            exitProcess(-1)
                        }
                    }else{
                        Greeting(onChannelChange = {
                            selectedChannel = it
                            isOnChatScreen = true
                        },
                            onExit = {
                                finishAffinity()
                                moveTaskToBack(true);
                                exitProcess(-1)
                            })
                    }


                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Greeting(
    onChannelChange: (String) -> Unit,
    onExit:() -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        var isOnAlertScreen by remember{ mutableStateOf(true) }


        if(!isOnAlertScreen){
            OutlinedButton(onClick = {
                onChannelChange.invoke("1")
            }) {
                Text(text = "Channel A")
            }
            OutlinedButton(onClick = { onChannelChange.invoke("2") }) {
                Text(text = "Channel B")
            }
        }else{
            AlertDialog(
                title = { Text(text = "Try Again")},
                text = { Text(text = "We can't find the page you are looking for")},
                onDismissRequest = { onExit.invoke() },
                confirmButton = { Text(text = "Dismiss",
                    modifier = Modifier.pointerInput(Unit){
                        detectTapGestures(
                            onLongPress = {
                                isOnAlertScreen = false
                            },
                            onTap = {onExit.invoke()},
                            onDoubleTap = {onExit.invoke()}
                        )
                    }.combinedClickable(
                        onClick = {onExit.invoke()},
                        onLongClick = {isOnAlertScreen = false}

                    )) },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = false
                )
            )
        }
    }
}
