package com.devid_academy.tutocomposeoct23.ui.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.PowerSettingsNew
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.devid_academy.tutocomposeoct23.Category
import com.devid_academy.tutocomposeoct23.R
import com.devid_academy.tutocomposeoct23.app.Screen
import com.devid_academy.tutocomposeoct23.formatDate
import com.devid_academy.tutocomposeoct23.network.ArticleDto
import com.devid_academy.tutocomposeoct23.toast
import com.devid_academy.tutocomposeoct23.ui.CustomRadioButtonRow
import com.devid_academy.tutocomposeoct23.ui.theme.FeedArticlesComposeTheme
import com.devid_academy.tutocomposeoct23.ui.theme.MangaBackground
import com.devid_academy.tutocomposeoct23.ui.theme.MiscBackground
import com.devid_academy.tutocomposeoct23.ui.theme.SportBackground


@Composable
fun MainScreen(
    navController: NavController,
    viewModel : MainViewModel)
{

    val articleList by viewModel.articleList.collectAsState()
    val selectedCategory by viewModel.selectedCategoryStateFlow.collectAsState()

    val expandedArticleId by viewModel.expandedArticleIdStateFlow.collectAsState()

    val articleWasDeleted by viewModel.articleWasDeletedStateFlow.collectAsState()


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
        articleWasDeleted = articleWasDeleted,
        onItemSwiped = { swipedArticleId, swipedArticleUserId  ->
            viewModel.deleteArticle(swipedArticleId, swipedArticleUserId)},
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


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainContent(
    articleList : List<ArticleDto>,
    selectedCategory : Int,
    expandedArticleId : Long,
    articleWasDeleted : Boolean,
    onItemSwiped : (Long, Long) -> Unit,
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
                contentDescription = stringResource(R.string.desc_buttonlabel_maintocrea),
                modifier = Modifier
                    .padding(10.dp)
                    .size(32.dp)
                    .clickable { onCreaButtonClicked.invoke() }
            )

            Icon(imageVector = Icons.Rounded.PowerSettingsNew,
                contentDescription = stringResource(R.string.desc_buttonlabel_logOut),
                modifier = Modifier
                    .padding(10.dp)
                    .size(32.dp)
                    .clickable { onLogoutButtonClicked.invoke() }
            )
        }

        LazyColumn(modifier = Modifier.weight(1f))
        {
            items(items = articleList, key = { it.id })
            {article ->

                SwipeToDismiss(state = rememberDismissState  {
                    if(it == DismissValue.DismissedToStart)
                        onItemSwiped.invoke(article.id, article.idU)
                    articleWasDeleted },
                    directions = setOf(DismissDirection.EndToStart) ,
                    dismissThresholds = {FractionalThreshold(0.75f)},
                    background = {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .padding(50.dp, 15.dp, 15.dp, 15.dp)
                                .background(color = Color.Red))
                        { Icon(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = stringResource(R.string.desc_buttonlabel_deleteArticle))
                        }
                    }
                )
                {
                    ItemArticle(
                        article = article,
                        expanded = expandedArticleId == article.id)
                    {clickedArticleId, clickedArticleUserId ->
                        onArticleClicked(clickedArticleId,clickedArticleUserId)
                    }
                }
            }
        }

        Row(modifier = Modifier.padding(vertical = 16.dp, horizontal = 12.dp))
        {

            CustomRadioButtonRow(
                category = 0,
                labelResId = R.string.rblabel_all,
                selectedCategory = selectedCategory,
                onClick = { onCategoryChanged(0) }
            )

            CustomRadioButtonRow(
                category = Category.SPORT,
                labelResId = R.string.rblabel_sport,
                selectedCategory = selectedCategory,
                onClick = { onCategoryChanged(Category.SPORT) }
            )

            CustomRadioButtonRow(
                category = Category.MANGA,
                labelResId = R.string.rblabel_manga,
                selectedCategory = selectedCategory,
                onClick = { onCategoryChanged(Category.MANGA) }
            )

            CustomRadioButtonRow(
                category = Category.DIVERS,
                labelResId = R.string.rblabel_misc,
                selectedCategory = selectedCategory,
                onClick = { onCategoryChanged(Category.DIVERS) }
            )
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
        .clickable { onArticleClicked.invoke(article.id, article.idU) }
    )
    {
        Column(modifier = Modifier
            .background(
                when (article.categorie) {
                    1 -> SportBackground
                    2 -> MangaBackground
                    3 -> MiscBackground
                    else -> MaterialTheme.colors.background
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
                    .crossfade(true)
                    .build(),
                    placeholder = painterResource(R.drawable.feedarticles_logo),
                    error = painterResource(R.drawable.feedarticles_logo),
                    contentDescription = stringResource(R.string.detail_articleillustration),
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
                        contentDescription = stringResource(R.string.desc_buttonlabel_reducearticlesize),
                        modifier = Modifier
                            .padding(4.dp)
                            .size(24.dp)
                    )

            }

            if(expanded){

                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .fillMaxWidth())
                {

                    Text(stringResource(R.string.articledetail_date, formatDate(article.createdAt) ?: ""))

                    Text(stringResource(R.string.articledetail_cat,
                                            when (article.categorie){
                                                Category.SPORT -> stringResource(R.string.rblabel_sport)
                                                Category.MANGA -> stringResource(R.string.rblabel_manga)
                                                Category.DIVERS -> stringResource(R.string.rblabel_misc)
                                                else -> {}
                                            })
                    )
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
    FeedArticlesComposeTheme {

    }
}


