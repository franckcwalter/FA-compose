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
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
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
import androidx.compose.ui.text.font.FontWeight
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
    val selectedCategory by viewModel.selectedCategoryStateFlow.collectAsState()

    val expandedArticleId by viewModel.expandedArticleIdStateFlow.collectAsState()


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
        selectedCategory = selectedCategory,
        expandedArticleId = expandedArticleId,
        onCreaButtonClicked = { viewModel.navToCrea() },
        onLogoutButtonClicked = { viewModel.logoutUser()},
        onArticleClicked = {clickedArticleId, clickedArticleUserId ->
            viewModel.expandArticleOrGoToEdit(clickedArticleId, clickedArticleUserId) },
        onCategoryChanged = { viewModel.changeSelectedCategoryAndFetchArticles(it) }
     )

    LaunchedEffect(Unit){
        viewModel.fetchArticles()
    }

}


@Composable
fun MainContent(
    articleList : List<ArticleDto>,
    selectedCategory : Int,
    expandedArticleId : Long,
    onCreaButtonClicked : () -> Unit,
    onLogoutButtonClicked : () -> Unit,
    onArticleClicked: (Long, Long) -> Unit,
    onCategoryChanged : (Int) -> Unit)
{

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

                ItemArticle(
                    article = article,
                    expanded = expandedArticleId == article.id
                ){clickedArticleId, clickedArticleUserId ->

                    onArticleClicked(clickedArticleId,clickedArticleUserId)

                    /* TODO : EXPAND AND SHOW DETAILS or GO TO EDIT */
                }
            }
        }

        Row(modifier = Modifier.padding(vertical = 12.dp))
        {

            Row(horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .weight(.25f)
                        .selectable(
                            selected = selectedCategory == 0,
                            onClick = { onCategoryChanged(0) },
                            role = Role.RadioButton
                        ))
            {
                RadioButton(selected = selectedCategory == 0, onClick = null)
                Text("Tout")
            }

            Row(horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .weight(.25f)
                    .selectable(
                        selected = selectedCategory == Category.SPORT,
                        onClick = { onCategoryChanged(Category.SPORT) },
                        role = Role.RadioButton
                    ))
            {
                RadioButton(selected = selectedCategory == Category.SPORT, onClick = null)
                Text("Sport")
            }

            Row(horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .weight(.25f)
                    .selectable(
                        selected = selectedCategory == Category.MANGA,
                        onClick = { onCategoryChanged(Category.MANGA) },
                        role = Role.RadioButton
                    ))
            {
                RadioButton(selected = selectedCategory == Category.MANGA, onClick = null)
                Text("Manga")
            }

            Row(horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .weight(.25f)
                    .selectable(
                        selected = selectedCategory == Category.DIVERS,
                        onClick = { onCategoryChanged(Category.DIVERS) },
                        role = Role.RadioButton
                    ))
            {
                RadioButton(selected = selectedCategory == Category.DIVERS, onClick = null)
                Text("Divers")
            }
        }
    }
}

@Composable
fun ItemArticle(
    article : ArticleDto,
    expanded : Boolean,
    onArticleClicked : (Long, Long) -> Unit)
{
    Card(modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth()
        .clickable { onArticleClicked.invoke(article.id, article.idU) })
    {
        Column(modifier = Modifier
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

            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween)
            {
                AsyncImage(model = ImageRequest.Builder(LocalContext.current)
                    .data(article.urlImage)
                    // .crossfade(true)
                    .build(),
                    placeholder = painterResource(R.drawable.feedarticles_logo),
                    error = painterResource(R.drawable.feedarticles_logo),
                    contentDescription = "Illustration d'article",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .size(if (expanded) 96.dp else 48.dp)
                        .clip(CircleShape)
                )
                Text(text = article.titre,
                    fontWeight = if (expanded) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.weight(1f))

                if (expanded)
                    Icon(imageVector = Icons.Rounded.ExpandLess,
                        contentDescription = "Réduire la taille de la carte",
                        modifier = Modifier
                            .padding(4.dp)
                            .size(24.dp))

            }

            if(expanded){

                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    .fillMaxWidth())
                {
                    Text(text = "Du " + article.createdAt)

                    Text(text = "Cat." + when (article.categorie){
                        Category.SPORT -> "Sport"
                        Category.MANGA -> "Manga"
                        Category.DIVERS -> "Divers"
                        else -> {}
                    })
                }

                Text(text = article.descriptif,
                    modifier = Modifier.padding(horizontal = 8.dp))

            }
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
