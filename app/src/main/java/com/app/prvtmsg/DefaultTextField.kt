package com.app.prvtmsg

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String)-> Unit,
    placeholderText: String,
    isError: Boolean,
    errorLabelText: String = "Required Field",
    isPassword : Boolean = false,
    isNumberKeyboard:Boolean = true,
    isChatKeyboard: Boolean = false,
    onSend: () -> Unit = {},
    isReadOnly: Boolean = false,
    trailingIcon: @Composable() (() -> Unit)? = null
    ){
    TextField(
        value = value,
        readOnly = isReadOnly,
        onValueChange = { onValueChange.invoke(it) },
        enabled = true,
        textStyle = MaterialTheme.typography.bodyLarge,
        shape = RoundedCornerShape(5.dp),
        isError = isError,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isNumberKeyboard) KeyboardType.Phone else KeyboardType.Text,
            imeAction = if (isChatKeyboard) ImeAction.Send else ImeAction.Default
            ),
        keyboardActions = if (isChatKeyboard){
            KeyboardActions(
                onSend = { onSend.invoke() }
            )
        }else{
             KeyboardActions.Default
        },
        placeholder = {
            Text(
                text = placeholderText,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
                )
        },
        label = if (!isError) {null}  else{
        {
            Text(
                text = errorLabelText,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        } },
        trailingIcon = trailingIcon,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.CenterVertically),
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colorScheme.onSurface,
            containerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            placeholderColor = MaterialTheme.colorScheme.scrim,
            errorIndicatorColor = Color.Transparent,
        )
    )
}

