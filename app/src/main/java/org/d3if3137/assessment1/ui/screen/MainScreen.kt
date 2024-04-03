package org.d3if3137.assessment1.ui.screen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3137.assessment1.R
import org.d3if3137.assessment1.navigation.Screen
import org.d3if3137.assessment1.ui.theme.Assessment1Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.nama_aplikasi))
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Image(
                            painter = painterResource(id = R.drawable.wizz),
                            contentDescription = null,
                            modifier = Modifier.padding()
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.About.route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(R.string.tentang_aplikasi),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { padding ->
        ScreenContent(Modifier.padding(padding))
    }
}

@Composable
fun ScreenContent(modifier: Modifier) {
    var panjang by rememberSaveable { mutableStateOf("") }

    var panjangError by rememberSaveable { mutableStateOf(false) }

    val radioOptions = listOf(
        stringResource(id = R.string.kilometer),
        stringResource(id = R.string.centimeter)
    )
    var opsi by rememberSaveable { mutableStateOf(radioOptions[0]) }

    var hasil by rememberSaveable { mutableFloatStateOf(0f) }

    val context = LocalContext.current

    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.deskripsi),
            modifier = Modifier
                .padding(16.dp)
        )

        OutlinedTextField(
            value = panjang,
            onValueChange = { panjang = it },
            isError = panjangError,
            label = { Text(text = stringResource(id = R.string.masukkan_nilai)) },
            trailingIcon = { IconPicker(panjangError, unit = "m") },
            supportingText = { ErrorHint(panjangError) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )

        Text(text = stringResource(id = R.string.satuan_panjang), modifier = Modifier)

        Row(
            modifier = Modifier
                .padding(bottom = 24.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp)),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            radioOptions.forEach { text ->

                OpsiPanjang(
                    label = text,
                    isSelected = opsi == text,
                    modifier = Modifier
                        .selectable(
                            selected = opsi == text,
                            onClick = { opsi = text },
                            role = Role.RadioButton
                        )
                        .padding(8.dp)
                )
            }
        }

        Button(
            modifier = Modifier.size(width = 110.dp, height = 50.dp),
            onClick = {
                panjangError = (panjang == "" || panjang == "0")
                if (panjangError) return@Button

                if (panjang.toFloat() != 0f) {
                    hasil = when (opsi) {
                        radioOptions[0] -> convertKilometer(panjang.toFloat())
                        radioOptions[1] -> convertCentimeter(panjang.toFloat())
                        else -> panjang.toFloat()
                    }
                }
            }) {
            Text(
                text = stringResource(id = R.string.hitung),
                fontSize = 18.sp

            )
        }
        if (hasil != 0f) {
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp
            )
            Text(text = stringResource(id = R.string.nilai_masukkan, panjang))
            Text(
                text = stringResource(id = R.string.opsi, opsi),
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = stringResource(id = R.string.hasil, hasil)
            )
            Button(
                onClick = {
                    shareData(
                        context = context,
                        message = context.getString(
                            R.string.bagikan_template,
                            panjang, opsi, hasil
                        )
                    )
                },
                modifier = Modifier.padding(top = 8.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(text = stringResource(R.string.bagikan))
            }
        }
    }
}

@Composable
fun OpsiPanjang(
    label: String,
    isSelected: Boolean,
    modifier: Modifier,
) {

    Row(
        modifier = modifier
            .padding(vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null
        )
        Text(
            text = label,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun IconPicker(isError: Boolean, unit: String) {
    if (isError) {
        Icon(
            imageVector = Icons.Filled.Warning,
            contentDescription = null
        )
    } else {
        Text(text = unit)
    }
}

@Composable
fun ErrorHint(isError: Boolean) {
    if (isError) {
        Text(
            text = stringResource(R.string.input_invalid)
        )
    }
}

private fun convertKilometer(panjang: Float): Float {
    return panjang / 1000
}

private fun convertCentimeter(panjang: Float): Float {
    return panjang * 100
}

private fun shareData(context: Context, message: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ScreenPreview() {
    Assessment1Theme {
        MainScreen(rememberNavController())
    }
}