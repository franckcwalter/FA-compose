package com.devid_academy.tutocomposeoct23.ui.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.devid_academy.tutocomposeoct23.Category
import com.devid_academy.tutocomposeoct23.R
import com.devid_academy.tutocomposeoct23.network.ArticleDto
import com.devid_academy.tutocomposeoct23.toast
import com.devid_academy.tutocomposeoct23.ui.CustomButton
import com.devid_academy.tutocomposeoct23.ui.CustomRadioButtonRow
import com.devid_academy.tutocomposeoct23.ui.CustomTextField
import com.devid_academy.tutocomposeoct23.ui.theme.FeedArticlesComposeTheme

@Composable
fun EditScreen(
    navController: NavController,
    viewModel : EditViewModel,
    articleId : Long
){

    val articleToEdit by viewModel.articleToEditStateFlow.collectAsState()

    EditContent(articleToEdit){ title, description, imageUrl, selectedCategory ->
        viewModel.updateArticle(articleId, title, description, imageUrl, selectedCategory)
    }

    LaunchedEffect(true){
        viewModel.navSharedFlow.collect{
            navController.popBackStack()
        }
    }

    val context = LocalContext.current
    LaunchedEffect(true){
        viewModel.userMessageSharedFlow.collect{
            context.toast(it)
        }
    }

    viewModel.fetchArticle(articleId)

}

@Composable
fun EditContent(
    articleToEdit : ArticleDto,
    onButtonClicked : (String, String, String, Int) -> Unit
)
{
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    val selectedCategory = remember { mutableStateOf(Category.DIVERS) }

    LaunchedEffect(articleToEdit) {

        title       = articleToEdit.titre
        description = articleToEdit.descriptif
        imageUrl    = articleToEdit.urlImage

        when(articleToEdit.categorie){
            Category.SPORT -> selectedCategory.value = Category.SPORT
            Category.MANGA -> selectedCategory.value = Category.MANGA
            Category.DIVERS -> selectedCategory.value = Category.DIVERS
        }
    }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround)
    {
        Text(text = stringResource(R.string.edit_pagetitle),
            style = MaterialTheme.typography.h1)

        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly)
        {

            CustomTextField(
                value = title,
                onValueChange = { title = it },
                labelRes = R.string.edit_et_title,
                isPassword = false,
                largeTexfield = true,
                tallTextField = false
            )

            Spacer(Modifier.height(12.dp))

            CustomTextField(
                value = description,
                onValueChange = { description = it },
                labelRes = R.string.edit_et_desc,
                isPassword = false,
                largeTexfield = true,
                tallTextField = true
            )

            Spacer(Modifier.height(12.dp))

            CustomTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                labelRes = R.string.edit_et_imageurl,
                isPassword = false,
                largeTexfield = true,
                tallTextField = false
            )

            Spacer(Modifier.height(24.dp))

            AsyncImage(model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
                placeholder = painterResource(R.drawable.feedarticles_logo),
                contentDescription = stringResource(R.string.edit_desc_articleillustration),
                contentScale = ContentScale.Fit,
                modifier = Modifier.padding(horizontal = 10.dp)
                                .size(100.dp)
            )

            Spacer(Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.Center)
            {

                CustomRadioButtonRow(
                    category = Category.SPORT,
                    labelResId = R.string.rblabel_sport,
                    selectedCategory = selectedCategory.value,
                    onClick = { selectedCategory.value = Category.SPORT }
                )

                CustomRadioButtonRow(
                    category = Category.MANGA,
                    labelResId = R.string.rblabel_manga,
                    selectedCategory = selectedCategory.value,
                    onClick = { selectedCategory.value = Category.MANGA }
                )

                CustomRadioButtonRow(
                    category = Category.DIVERS,
                    labelResId = R.string.rblabel_misc,
                    selectedCategory = selectedCategory.value,
                    onClick = { selectedCategory.value = Category.DIVERS }
                )
            }
        }

        CustomButton(onClick = { onButtonClicked.invoke(title, description, imageUrl, selectedCategory.value) },
            labelRes = R.string.edit_button_create,
            largeButton = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditPreview() {
    FeedArticlesComposeTheme {
        EditContent(ArticleDto(0, "TITRE", "DESC","",0,"",0)){_,_,_,_-> }
    }
}

