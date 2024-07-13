package com.example.onlineshoppistore.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.onlineshoppistore.R
import com.example.onlineshoppistore.network.Status

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AllProductScreen(
    modifier: Modifier = Modifier,
    /*  viewModel: ProductViewModel ,*/
    navController: NavController,
    backgroundColor: Color = Color.Transparent,
    backgroundColorWhileUpdate: Color = Color.LightGray,
) {
    //   val scope = rememberCoroutineScope()
    //  val value = viewModel.productsState.collectAsState().value

    var errorMessage: String = ""


    val retry = remember {
        mutableStateOf(false)
    }
    /*LaunchedEffect(retry.value) {
        viewModel.fetchProducts()
    }*/
    val productsViewModel: ProductsViewModel = hiltViewModel()

    val dataState by productsViewModel.products.collectAsState()
    // Directly infer the refreshing state from the resource status.
    val isRefreshing =
        dataState.status == Status.LOADING || dataState.status == Status.UPDATING
    val pullRefreshState = rememberPullRefreshState(isRefreshing, productsViewModel::fetchProducts)



    Box(
        modifier = Modifier
            .background(if (dataState.status == Status.UPDATING) backgroundColorWhileUpdate else backgroundColor)
            .fillMaxSize()
            .pullRefresh(state = pullRefreshState),

        ) {


        when (dataState.status) {
            Status.SUCCESS, Status.UPDATING -> {
                LazyVerticalGrid(
                    modifier = modifier,
                    columns = GridCells.Fixed(2),

                    ) {
                    item(span = {
                        // LazyGridItemSpanScope:
                        // maxLineSpan
                        GridItemSpan(maxLineSpan)
                    }) {
                        TopAppBar(
                            navigationIcon = {
                            IconButton(onClick = {navController.navigateUp() }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.arrow_left_02),
                                    contentDescription = ""
                                )
                            }
                        },
                            title = { Text(
                                text = "All Product",

                                // Heading/H4: SemiBold
                                style = TextStyle(
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight(600),
                                    color = Color(0xFF292929),
                                )
                            ) })
                    }
                    dataState.data?.let { products ->
                        items(products.size) { shoeItem ->

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize(),
                                ) {
                                    Card(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .width(168.5.dp)
                                            .height(180.dp)
                                            .clickable(onClick = {
                                                navController.navigate("details" + "?id=${products[shoeItem].id}&price=${products[shoeItem].currentPrice[0].NGN[0].toString()}")
                                            }
                                            ),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = CardDefaults.cardColors(containerColor =  Color(0x66EAEAEA))
                                    ) {
                                        AsyncImage(
                                            modifier = Modifier
                                                .aspectRatio(1f)
                                                .clip(shape = RoundedCornerShape(8.dp)),
                                            model = "https://api.timbu.cloud/images/${products[shoeItem].photos[0].url}",
                                            contentDescription = null,
                                        )

                                    }
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .offset(x = (-20).dp, y = (16).dp)
                                            .width(32.dp)
                                            .height(32.dp)
                                            .background(
                                                color = Color(0x99000000),
                                                shape = RoundedCornerShape(size = 32.dp)
                                            )

                                            .padding(
                                                start = 6.4.dp,
                                                top = 6.4.dp,
                                                end = 6.4.dp,
                                                bottom = 6.4.dp
                                            )
                                    ) {

                                        Icon(
                                            painter = painterResource(id = R.drawable.favorite_fill0_wght400_grad0_opsz24),
                                            contentDescription = "",
                                            tint = Color.White
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .padding(start = 4.dp, end = 4.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Bottom,

                                    ) {
                                    Column(
                                        modifier = Modifier.padding(start = 16.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        Text(
                                            text = "Athletic/Sportswear",
// Text/md2: Regular
                                            style = TextStyle(
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight(400),
                                                color = Color(0xFF2A2A2A),

                                                )
                                        )
                                        Text(
                                            text = products[shoeItem].name,
                                            style = MaterialTheme.typography.headlineSmall,
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1,
                                            modifier = Modifier.size(
                                                width = 124.dp,
                                                height = 32.dp
                                            )
                                        )
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp,)) {
                                            Image(
                                                painter = painterResource(id = R.drawable.star_half),
                                                contentDescription = "image description",
                                                contentScale = ContentScale.None
                                            )
                                            Text(
                                                text = "4.5 (100 sold)",
// Text/md2: Medium
                                                style = TextStyle(
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight(500),
                                                    color = Color(0xFF2A2A2A),

                                                    )
                                            )
                                        }
                                        Text(
                                            text = "₦ "+products[shoeItem].currentPrice[0].NGN[0].toString(),
                                            style =  TextStyle(
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight(600),
                                                color = Color(0xFF0072C6),

                                                ),
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1
                                        )
                                        Text(
                                            text ="₦ "+ (products[shoeItem].currentPrice[0].NGN[0].toString()
                                                .toDouble() + 2000).toString(),
                                            style = TextStyle(
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight(500),
                                                color = Color(0xFF9D9D9D),

                                                ),
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1,
                                            textDecoration = TextDecoration.LineThrough
                                        )
                                    }

                                    IconButton(
                                        /* colors = IconButtonDefaults.iconButtonColors(
                                             containerColor = Color(0x1F0072C6),
                                         ),*/
                                        onClick = {
                                            navController.navigate("details" + "?id=${products[shoeItem].id}&price=${products[shoeItem].currentPrice[0].NGN[0].toString()}")
                                        },
                                        modifier = Modifier
                                            .width(36.dp)
                                            .height(28.dp)
                                            .background(
                                                color = Color(0x1F0072C6),
                                                shape = RoundedCornerShape(size = 8.dp)
                                            )
                                            .padding(
                                                start = 12.dp,
                                                top = 8.dp,
                                                end = 12.dp,
                                                bottom = 8.dp
                                            )


                                    ) {
                                        Icon(
                                            tint = Color(0xff0072C6),
                                            painter = painterResource(id = R.drawable.shopping_basket_02),
                                            contentDescription = ""
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.padding(bottom = 16.dp))
                            }

                        }
                    }
                }

            }

            Status.ERROR -> DefaultErrorContent(
                dataState.message, Modifier.align(Alignment.Center),productsViewModel
            )

            Status.LOADING -> DefaultLoadingContent(
                Modifier.align(Alignment.Center)
            )
        }
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = modifier.align(Alignment.TopCenter)
        )


    }
}