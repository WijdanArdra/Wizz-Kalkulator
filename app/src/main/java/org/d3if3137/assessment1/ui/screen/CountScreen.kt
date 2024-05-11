package org.d3if3137.assessment1.ui.screen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3137.assessment1.R
import org.d3if3137.assessment1.database.KalkulatorDb
import org.d3if3137.assessment1.navigation.Screen
import org.d3if3137.assessment1.ui.theme.Assessment1Theme
import org.d3if3137.assessment1.ui.theme.BgClr
import org.d3if3137.assessment1.ui.theme.TABClr
import org.d3if3137.assessment1.util.ViewModelFactory

const val KEY_ID_KALKULATOR = "idKalkulator"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountScreen(navController: NavHostController, id: Long? = null) {
    val context = LocalContext.current
    val db = KalkulatorDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var judul by rememberSaveable { mutableStateOf("") }

    var masukkan by rememberSaveable { mutableStateOf("") }

    val radioOptions = listOf(
        stringResource(id = R.string.kilometer),
        stringResource(id = R.string.centimeter)
    )
    var opsi by rememberSaveable { mutableStateOf(radioOptions[0]) }

    var hasil by rememberSaveable { mutableFloatStateOf(0f) }

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        if (id == null) return@LaunchedEffect
        val data = viewModel.getKalkulator(id) ?: return@LaunchedEffect
        judul = data.judul
        masukkan = data.masukkan
        opsi = data.opsi
        hasil = data.hasil.toFloat()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (id == null)
                        Text(text = stringResource(id = R.string.hitung_aplikasi))
                    else
                        Text(text = stringResource(id = R.string.edit_data))
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = TABClr,
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
                    if (id != null) {
                        DeleteAction { showDialog = true }
                        DisplayAlertDialog(
                            openDialog = showDialog,
                            onDismissRequest = { showDialog = false }) {
                            showDialog = false
                            viewModel.delete(id)
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    ) { padding ->
        ScreenContent(
            judul = judul,
            onTitleChange = { judul = it },
            masukkan = masukkan,
            onInputChange = { masukkan = it },
            opsi = opsi,
            onOptionChange = { opsi = it },
            Modifier.padding(padding),
            navController,
            id
        )
    }
}

@Composable
fun ScreenContent(
    judul: String, onTitleChange: (String) -> Unit,
    masukkan: String, onInputChange: (String) -> Unit,
    opsi: String, onOptionChange: (String) -> Unit,
    modifier: Modifier, navController: NavHostController,
    id: Long? = null
) {

    val context = LocalContext.current
    val db = KalkulatorDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var showError by remember {
        mutableStateOf(false)
    }

    val radioOptions = listOf(
        stringResource(id = R.string.kilometer),
        stringResource(id = R.string.centimeter)
    )

    var hasil by rememberSaveable { mutableFloatStateOf(0f) }

    Column(
        modifier = modifier
            .background(BgClr)
            .padding(8.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = judul,
            onValueChange = { onTitleChange(it) },
            isError = showError,
            label = { Text(text = stringResource(id = R.string.judul_simpan)) },
            supportingText = { ErrorHint(showError) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )
        OutlinedTextField(
            value = masukkan,
            onValueChange = { onInputChange(it) },
            isError = showError,
            label = { Text(text = stringResource(id = R.string.masukkan_nilai)) },
            trailingIcon = { Text(text = "m") },
            supportingText = { ErrorHint(showError) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )

        Text(
            text = stringResource(id = R.string.satuan_panjang),
            modifier = Modifier
        )

        Row(
            modifier = Modifier
                .padding(bottom = 24.dp)
                .border(1.dp, Color.DarkGray, RoundedCornerShape(4.dp)),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            radioOptions.forEach { text ->

                OpsiPanjang(
                    label = text,
                    isSelected = opsi == text,
                    modifier = Modifier
                        .selectable(
                            selected = opsi == text,
                            onClick = { onOptionChange(text) },
                            role = Role.RadioButton
                        )
                        .padding(8.dp)
                )
            }
        }

        Button(
            modifier = Modifier.size(width = 150.dp, height = 50.dp),
            onClick = {
                if (judul == "" || masukkan == "" || masukkan == "0") {
                    showError = true
                    Toast.makeText(context, R.string.input_invalid, Toast.LENGTH_LONG).show()
                    return@Button
                }

                if (masukkan.toFloat() != 0f) {
                    hasil = when (opsi) {
                        radioOptions[0] -> convertKilometer(masukkan.toFloat())
                        radioOptions[1] -> convertCentimeter(masukkan.toFloat())
                        else -> masukkan.toFloat()
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
            Text(text = stringResource(id = R.string.nilai_masukkan, masukkan))
            Text(
                text = stringResource(id = R.string.opsi, opsi),
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = stringResource(id = R.string.hasil, hasil)
            )
            Row {
                Button(
                    onClick = {
                        if (id == null) {
                            viewModel.insert(judul, masukkan, opsi, hasil.toString())
                        } else {
                            viewModel.update(id, judul, masukkan, opsi, hasil.toString())
                        }
                        navController.popBackStack()
                    },
                    modifier = Modifier.padding(top = 8.dp),
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.simpan)
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Button(
                    onClick = {
                        shareData(
                            context = context,
                            message = context.getString(
                                R.string.bagikan_template,
                                masukkan, opsi, hasil
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
}

@Composable
fun DeleteAction(delete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.lainnya),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(id = R.string.hapus))
                },
                onClick = {
                    expanded = false
                    delete()
                }
            )
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
fun ErrorHint(isError: Boolean) {
    LocalContext.current

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
fun CountScreenPreview() {
    Assessment1Theme {
        CountScreen(rememberNavController())
    }
}