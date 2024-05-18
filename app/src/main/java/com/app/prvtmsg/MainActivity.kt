package com.app.prvtmsg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.app.prvtmsg.ui.screens.ChatScreen
import com.app.prvtmsg.ui.screens.HomeScreen
import com.app.prvtmsg.ui.theme.PrivateMessagingTheme
import com.app.prvtmsg.util.editPreference
import com.app.prvtmsg.util.readPreference
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.random.Random
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
                    var userId by rememberSaveable {
                        mutableStateOf("")
                    }
                    val pref = readPreference(this)
                    if (pref == ""){
                        userId = Timestamp.now().toString().hashCode().toString()
                        editPreference(this,userId)
                    }else if (pref != "" && pref != null){
                        userId = pref
                    }
                    var isOnChatScreen by remember{ mutableStateOf(false) }
                    var chatId by rememberSaveable {
                        mutableStateOf("")
                    }
                    if (isOnChatScreen){
                        ChatScreen(selectedChannel = userId,chatId = chatId){
                            finishAffinity()
                            moveTaskToBack(true);
                            exitProcess(-1)
                        }
                    }else{
                        HomeScreen(onChannelChange = {
                            chatId = it
                            isOnChatScreen = true
                        })
                    }


                }
            }
        }
    }
}


