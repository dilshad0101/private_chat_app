package com.app.prvtmsg

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ChatBox(
    isAdmin:Boolean,
    message: String,
    time: String
){
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(
            start = if (isAdmin) 0.dp else 30.dp,
            end =  if (isAdmin) 30.dp else 0.dp
        ),
        horizontalAlignment = if (isAdmin) Alignment.Start else Alignment.End

    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.background(
                if (isAdmin) MaterialTheme.colorScheme.secondary
                else MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(10.dp)
            )
        ){
            Text(
                text = message,
                color = if (isAdmin) MaterialTheme.colorScheme.onSecondary
                else MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyLarge,//body medium if too big
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(10.dp))
        }
        Text(
            text = time,
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .wrapContentWidth(
                    if (isAdmin) Alignment.Start else Alignment.End
                )
        )
    }
}