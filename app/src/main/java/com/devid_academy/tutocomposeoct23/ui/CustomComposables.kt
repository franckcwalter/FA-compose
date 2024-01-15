package com.devid_academy.tutocomposeoct23.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.devid_academy.tutocomposeoct23.Category
import com.devid_academy.tutocomposeoct23.R

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelRes: Int,
    isPassword : Boolean,
    largeTexfield : Boolean,
    tallTextField : Boolean
) {
    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text(text = stringResource(labelRes),
                color = MaterialTheme.colors.primary) },
        textStyle = MaterialTheme.typography.body2,
        colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.background,
                                                textColor = MaterialTheme.colors.primary),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = if (tallTextField) Modifier.fillMaxWidth().padding(horizontal = 24.dp).height(200.dp)
                    else if (largeTexfield) Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                    else Modifier
    )
}

@Composable
fun CustomButton(
    onClick : () -> Unit,
    labelRes : Int,
    largeButton : Boolean
)
{
    Button(
        onClick = { onClick.invoke()  },
        modifier = if(largeButton) Modifier.fillMaxWidth().padding(horizontal = 24.dp) else Modifier.requiredWidth(200.dp)
    )
    { Text(text = stringResource(labelRes),
        modifier = Modifier.padding(vertical = 4.dp)) }
}


@Composable
fun CustomRadioButtonRow(
    category: Int,
    labelResId: Int,
    selectedCategory: Int,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .selectable(
                selected = selectedCategory == category,
                onClick = { onClick() },
                role = Role.RadioButton
            )
    ) {
        RadioButton(
            modifier = Modifier.padding(end = 2.dp),
            selected = selectedCategory == category,
            onClick = null,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colors.primary,
                unselectedColor = MaterialTheme.colors.primary
            )

        )
        Text(stringResource(labelResId))
    }
}


