package com.devid_academy.tutocomposeoct23.ui.main

import android.widget.ImageButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.PowerSettingsNew
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.devid_academy.tutocomposeoct23.Category
import com.devid_academy.tutocomposeoct23.R
import com.devid_academy.tutocomposeoct23.Screen
import com.devid_academy.tutocomposeoct23.network.ArticleDto
import com.devid_academy.tutocomposeoct23.toast
import com.devid_academy.tutocomposeoct23.ui.register.RegisterContent
import com.devid_academy.tutocomposeoct23.ui.theme.TutoComposeOct23Theme

@Composable
fun MainScreen(
    navController: NavController,
    viewModel : MainViewModel)
{

    val articleList by viewModel.articleList.collectAsState()

    LaunchedEffect(Unit){
        viewModel.navSharedFlow.collect{
            navController.navigate(it){
                if (it == Screen.Login.route)
                    popUpTo(Screen.Main.route){
                        inclusive = true
                    }
            }
        }
    }

    val context = LocalContext.current
    LaunchedEffect(true){
        viewModel.userMessageSharedFlow.collect{
            context.toast(it)
        }
    }


    MainContent(
         articleList = articleList,

         /*listOf(ArticleDto(0,"titre","desc","https://www.planeteanimaux.com/wp-content/uploads/2020/09/races-de-petit-chien-blanc.jpg",2,"",1),
                            ArticleDto(2,"titre","desc","https://www.planeteanimaux.com/wp-content/uploads/2020/09/races-de-petit-chien-blanc.jpg",2,"",1)) */

        onCreaButtonClicked = { viewModel.navToCrea() },
        onLogoutButtonClicked = { viewModel.logoutUser()},
        onArticleClicked = {clickedArticleId, clickedArticleUserId ->
            viewModel.expandArticleOrGoToEdit(clickedArticleId, clickedArticleUserId) }
     )


    viewModel.fetchArticles()

}

@Composable
fun MainContent(
    articleList : List<ArticleDto>,
    onCreaButtonClicked : () -> Unit,
    onLogoutButtonClicked : () -> Unit,
    onArticleClicked: (Long, Long) -> Unit)
{

    val selectedCategory = remember { mutableStateOf(0) }

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize())
    {

        Row(horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth())
        {
            Icon(imageVector = Icons.Rounded.Add,
                contentDescription = "Bouton Ajouter un article",
                modifier = Modifier
                    .padding(10.dp)
                    .size(32.dp)
                    .clickable { onCreaButtonClicked.invoke() })

            Icon(imageVector = Icons.Rounded.PowerSettingsNew,
                contentDescription = "Bouton Se déconnecter",
                modifier = Modifier
                    .padding(10.dp)
                    .size(32.dp)
                    .clickable { onLogoutButtonClicked.invoke() })
        }

        LazyColumn(modifier = Modifier.weight(1f))
        {
            items(articleList)
            {article ->

                ItemArticle(article){clickedArticleId, clickedArticleUserId ->

                    onArticleClicked(clickedArticleId,clickedArticleUserId)

                    /* TODO : EXPAND AND SHOW DETAILS or GO TO EDIT */
                }
            }
        }

        Row(modifier = Modifier.padding(vertical = 12.dp))
        {

            Row(horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.weight(.25f)
                                .selectable(selected = selectedCategory.value == 0,
                                            onClick = { selectedCategory.value = 0 },
                                            role = Role.RadioButton))
            {
                RadioButton(selected = selectedCategory.value == 0, onClick = null)
                Text("Tout")
            }

            Row(horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.weight(.25f)
                                .selectable(selected = selectedCategory.value == Category.SPORT,
                                            onClick = { selectedCategory.value = Category.SPORT },
                                            role = Role.RadioButton))
            {
                RadioButton(selected = selectedCategory.value == Category.SPORT, onClick = null)
                Text("Sport")
            }

            Row(horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.weight(.25f)
                                .selectable(selected = selectedCategory.value == Category.MANGA,
                                            onClick = { selectedCategory.value = Category.MANGA },
                                            role = Role.RadioButton))
            {
                RadioButton(selected = selectedCategory.value == Category.MANGA, onClick = null)
                Text("Manga")
            }

            Row(horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.weight(.25f)
                                .selectable(selected = selectedCategory.value == Category.DIVERS,
                                            onClick = { selectedCategory.value = Category.DIVERS },
                                            role = Role.RadioButton))

            {
                RadioButton(selected = selectedCategory.value == Category.DIVERS, onClick = null)
                Text("Divers")
            }
        }
    }


}

@Composable
fun ItemArticle(article : ArticleDto, onArticleClicked : (Long, Long) -> Unit)
{
    Card(modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth()
        .clickable { onArticleClicked.invoke(article.id, article.idU) })
    {
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .background(
                    when (article.categorie) {
                        1 -> Color.Magenta
                        2 -> Color.Red
                        3 -> Color.Yellow
                        else -> Color.Black
                    }
                )
                .border(BorderStroke(2.dp, Color.Black))
                .padding(vertical = 10.dp))

            {

            AsyncImage(model = ImageRequest.Builder(LocalContext.current)
                    .data(article.urlImage)
                 // .crossfade(true)
                    .build(),
                    placeholder = painterResource(R.drawable.feedarticles_logo),
                    contentDescription = "Illustration d'article",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .size(50.dp)
                        .clip(CircleShape)
            )
            Text(text = article.titre,
                modifier = Modifier.weight(1f))
        }
    }
}



@Preview(showBackground = true)
@Composable
fun MainPreview() {
    TutoComposeOct23Theme {
      //   MainContent(listOf(ArticleDto(0,"titre","desc","https://www.planeteanimaux.com/wp-content/uploads/2020/09/races-de-petit-chien-blanc.jpg",2,"",1))){_->}
    }
}
