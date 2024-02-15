package com.example.chatroomapp

import android.os.Build
import android.os.Message

import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter



private val Message.timestamp: Long
    get() {
        return 0
    }

@RequiresApi(Build.VERSION_CODES.O)
private fun formatTimestamp(timestamp:Long):String {
    val messageDateTime =
        LocalDateTime.ofInstant((Instant.ofEpochMilli(timestamp)), java.time.ZoneId.systemDefault())
    val now = LocalDateTime.now()

    return when {
        isSameDay(messageDateTime, now) -> "today ${formatTime(messageDateTime)}"
        isSameDay(messageDateTime.plusDays(1), now) -> "yesterday ${formatTime(messageDateTime)}"
        else -> formatDate(messageDateTime)
    }

}

@RequiresApi(Build.VERSION_CODES.O)
private fun isSameDay(dateTime1: LocalDateTime,dateTime2: LocalDateTime):Boolean{
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return dateTime1.format(formatter) == dateTime2.format(formatter)
}
@RequiresApi(Build.VERSION_CODES.O)
private fun formatTime(dateTime: LocalDateTime):String{
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return formatter.format(dateTime)
}
@RequiresApi(Build.VERSION_CODES.O)
private fun formatDate(dateTime: LocalDateTime):String{
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return formatter.format(dateTime)
}

data class Message(
    val text: String,
    val senderFirstName: String,
    val timestamp: String
)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatMessageItem(message: Message) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = if (message.isSentByCurrentUser) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (message.isSentByCurrentUser) colorResource(id = R.color.purple_700) else Color.Gray,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
        ) {
            Text(
                text = message.text,
                color = Color.White,
                style = TextStyle(fontSize = 16.sp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = message.senderFirstName,
            style = TextStyle(
                fontSize = 12.sp,
                color = Color.Gray
            )
        )
        Text(
            text = formatTimestamp(message.timestamp), // Replace with actual timestamp logic
            style = TextStyle(
                fontSize = 12.sp,
                color = Color.Gray
            )
        )
    }
}
