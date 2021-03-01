/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.example.androiddevchallenge.data.DogInfo
import com.example.androiddevchallenge.ui.theme.CardBg
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.theme.PurpleTheme
import com.example.androiddevchallenge.ui.theme.purple700

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                val navController = rememberNavController()
                ScreenRouter(navController = navController)
            }
        }
    }
}


@Composable
fun ScreenRouter(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "Main") {
        composable(Route.Main.path) {
            Column {
                Text(
                    text = "Adopt Dog!", modifier = Modifier.padding(16.dp),
                    style = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
                )
                DogList(navController)
            }
        }

        composable(Route.Details.path,
            arguments = listOf(
                navArgument(AppConstants.Args.DOG_IMAGE_ID) { type = NavType.IntType },
                navArgument(AppConstants.Args.DOG_AGE) { type = NavType.StringType },
                navArgument(AppConstants.Args.DOG_NAME) { type = NavType.StringType },
                navArgument(AppConstants.Args.DOG_DETAILS) { type = NavType.StringType },
                navArgument(AppConstants.Args.DOG_GENDER) { type = NavType.StringType }
            )) { navBackStackEntry ->
            navBackStackEntry.arguments?.apply {
                DogDetails(
                    getInt(AppConstants.Args.DOG_IMAGE_ID),
                    getString(AppConstants.Args.DOG_AGE),
                    getString(AppConstants.Args.DOG_NAME),
                    getString(AppConstants.Args.DOG_DETAILS),
                    getString(AppConstants.Args.DOG_GENDER),
                    navController
                )
            }
        }
    }
}

@Composable
fun DogList(navController: NavHostController) {
    val dogList = dogListGenerator()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            LazyRow(modifier = Modifier.padding(12.dp)) {
                items(dogList) { dogInfo ->
                    Image(
                        painter = painterResource(id = dogInfo.image),
                        contentDescription = "Dog",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(6.dp)
                            .size(84.dp)
                            .clip(CircleShape)
                            .border(3.dp, getRandomColor(), CircleShape)
                    )
                }
            }
        }

        items(dogList) { dogInfo ->
            IndividualDog(dogInfo, navController)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun IndividualDog(dogInfo: DogInfo, navController: NavHostController) {

    var isDogDetailsVisible by remember { mutableStateOf(false) }
    val transition = updateTransition(targetState = isDogDetailsVisible)

    val thumbnailSize by transition.animateDp { detailsShown ->
        if (detailsShown) 120.dp else 80.dp
    }

    val thumbnailShape by transition.animateInt { detailsShown ->
        if (detailsShown) 10 else 50
    }

    val optionsColor by transition.animateColor { detailsScreen ->
        if (detailsScreen) purple700 else PurpleTheme
    }

    val maxLines by transition.animateInt { detailsScreen ->
        if (detailsScreen) 6 else 2
    }

    val cardRadius by transition.animateInt { detailsScreen ->
        if (detailsScreen) 10 else 20
    }

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)
        .clickable {
            isDogDetailsVisible = isDogDetailsVisible.not()
        },
        elevation = 4.dp,
        shape = RoundedCornerShape(cardRadius)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {

            DogInformation(
                thumbnailSize,
                thumbnailShape,
                optionsColor,
                dogImage = dogInfo.image,
                dogName = dogInfo.breed,
                dogAddress = dogInfo.address,
                onNextClick = {

                    navController.navigate(
                        "Details/${dogInfo.image}/${dogInfo.age}/${dogInfo.breed}/${dogInfo.details}/${dogInfo.gender}"
                    )
                }
            )
            Spacer(modifier = Modifier.padding(top = 8.dp))
            AgeGenderOption(
                optionsColor,
                isDogDetailsVisible,
                dogInfo.age,
                dogInfo.gender,
                dogInfo.likes
            )
            Spacer(modifier = Modifier.padding(top = 8.dp))

            Text(
                text = dogInfo.details,
                maxLines = maxLines,
                style = TextStyle(
                    color = Color.Black,
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic
                ),
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .clip(RoundedCornerShape(topStartPercent = 30, topEndPercent = 30))
                    .background(CardBg)
                    .padding(12.dp)
            )
        }
    }
}

@Composable
private fun DogInformation(
    thumbnailSize: Dp,
    thumbnailShape: Int,
    optionsColor: Color,
    dogImage: Int,
    dogName: String,
    dogAddress: String,
    onNextClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = dogImage),
            contentDescription = "Dog",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(thumbnailSize)
                .clip(RoundedCornerShape(thumbnailShape))
        )

        Spacer(modifier = Modifier.padding(8.dp))

        DogBreedWithAddress(dogName, dogAddress)

        IconButton(
            onClick = { onNextClick() }, modifier = Modifier
                .clip(CutCornerShape(topStartPercent = 40, bottomStartPercent = 40))
                .background(color = optionsColor)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Next",
                tint = Color.White
            )
        }
    }
}

@Composable
fun RowScope.DogBreedWithAddress(dogName: String, dogAddress: String) {
    Column(modifier = Modifier.weight(1.0f)) {
        Text(
            text = dogName,
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
        )

        Text(
            text = dogAddress,
            style = TextStyle(fontWeight = FontWeight.Light, fontSize = 14.sp)
        )
    }
}

@Composable
private fun AgeGenderOption(
    optionsColor: Color,
    isDogDetailsVisible: Boolean,
    dogAge: String,
    dogGender: String,
    likes: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IndividualOption(
            "Age: ", dogAge,
            Modifier
                .weight(1f)
                .clip(CutCornerShape(50))
                .background(color = optionsColor)
                .padding(4.dp)
        )

        Spacer(modifier = Modifier.padding(end = 16.dp))

        IndividualOption(
            "Gender: ", dogGender,
            Modifier
                .weight(1f)
                .clip(CutCornerShape(50))
                .background(color = optionsColor)
                .padding(4.dp)
        )

        Spacer(modifier = Modifier.padding(end = 16.dp))

        FavOption(isDogDetailsVisible, optionsColor, likes)
    }
}

/**
 * Individual option information which will include
 * Age: 5 Months, Gender: Female
 */
@Composable
fun IndividualOption(label: String, value: String, modifier: Modifier) {
    Row(modifier, horizontalArrangement = Arrangement.Center) {
        Text(
            text = label,
            style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        )
        Text(text = value, style = TextStyle(color = Color.White, fontSize = 12.sp))
    }
}

/**
 * Switch the heart icon when user clicks on the card, crossfade
 * between pre and post click heart icon.
 */
@Composable
fun FavOption(isDogDetailsVisible: Boolean, optionsColor: Color, likes: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .clip(CutCornerShape(topStartPercent = 50, bottomStartPercent = 50))
            .background(color = optionsColor)
            .padding(start = 8.dp, end = 8.dp)
    ) {
        Crossfade(targetState = isDogDetailsVisible) { showDetails ->
            if (showDetails) {
                Image(
                    painter = painterResource(id = R.drawable.circle_heart_shape),
                    contentDescription = "Like",
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.thumbnail_heart_shape),
                    contentDescription = "Like",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Text(
            text = "$likes",
            modifier = Modifier.padding(4.dp),
            style = TextStyle(color = Color.White, fontSize = 12.sp)
        )
    }
}

fun dogListGenerator(): List<DogInfo> {
    return listOf(
        DogInfo(
            "Golden Retriever",
            "Chandigarh",
            "2 Months",
            "Female",
            2004,
            "The Golden Retriever is a medium-large gun dog that was bred to retrieve shot waterfowl, such as ducks and upland game birds, during hunting and shooting parties",
            R.drawable.golden_retriever
        ),
        DogInfo(
            "Beagle",
            "Hyderabad",
            "3 Months",
            "Male",
            158,
            "The beagle is a breed of small hound that is similar in appearance to the much larger foxhound. The beagle is a scent hound, developed primarily for hunting hare (beagling). Possessing a great sense of smell and superior tracking instincts, the beagle is the primary breed used as detection dogs for prohibited agricultural imports and foodstuffs in quarantine around the world. The beagle is intelligent. It is a popular pet due to its size, good temper, and a lack of inherited health problems.",
            R.drawable.beagle
        ),
        DogInfo(
            "Shih Tzu",
            "Mumbai",
            "20 Days",
            "Female",
            157,
            "The Shih Tzu is a strong little dog with a small muzzle and normally have large dark brown eyes. They have a soft and long double coat. Although sometimes long, a Shih Tzu will not always have extremely lengthy hair like the Pekingese (but with short legs). Some of them have more short, curly hair. This is purely a choice made by the owners. A Shih Tzu should stand no more than 26.7 cm at the withers and with an ideal weight of 4.0 to 7.5 kg.",
            R.drawable.shih
        ),
        DogInfo(
            "Lhasa Apso",
            "Chandigarh",
            "15 Days",
            "Female",
            1005,
            "Lhasa is the capital city of Tibet, and apso is a word from the Tibetan language. There is some debate over the exact origin of the name; some claim that the word \"apso\" is an anglicized form of the Tibetan word for goatee (\"ag-tshom\", ཨག་ཚོམ་) or perhaps \"ra-pho\" (ར་ཕོ་) meaning \"billy goat\"",
            R.drawable.lhasa
        ),
        DogInfo(
            "German Shepherd",
            "Hyderabad",
            "2 Months",
            "Male",
            8004,
            "As a herding dog, German Shepherds are working dogs developed originally for herding sheep. Since that time, however, because of their strength, intelligence, trainability, and obedience, German Shepherds around the world are often the preferred breed for many types of work, including disability assistance, search-and-rescue, police and military roles and acting",
            R.drawable.g_sh
        ),
        DogInfo(
            "Husky Puppy",
            "Mumbai",
            "20 Days",
            "Male",
            4054,
            "Huskies are energetic and athletic. They usually have a thick double coat that can be gray, black, copper red, or white.he double coat generally protects huskies against harsh winters and, contrary to what most believe, they can survive in hotter climates. During the hotter climates, huskies tend to shed their undercoat regularly to cool their bodies. In addition to shedding, huskies control their eating habits based on the season; in cooler climates, they tend to eat generous amounts, causing their digestion to generate heat, whilst in warmer climates, they eat less.",
            R.drawable.husky
        ),
        DogInfo(
            "Rottweiler Puppy",
            "New Delhi",
            "15 Days",
            "Female",
            587,
            "Rottweilers are a relatively healthy, disease-free breed. As with most large breeds, hip dysplasia can be a problem. For this reason, the various Rottweiler breed clubs have had X-ray testing regimens in place for many years. Osteochondritis dissecans, a condition affecting the shoulder joints, can also be a problem due to the breed's rapid growth rate. A reputable breeder will have the hips and elbows of all breeding stock X-rayed and read by a recognised specialist, and will have the paperwork to prove it.",
            R.drawable.rott
        ),
        DogInfo(
            "Pomeranian Puppy",
            "Chennai",
            "20 Days",
            "Female",
            857,
            "The Pomeranian (often known as a Pom) is a breed of dog of the Spitz type that is named for the Pomerania region in north-west Poland and north-east Germany in Central Europe. Classed as a toy dog breed because of its small size, the Pomeranian is descended from larger Spitz-type dogs, specifically the German Spitz. It has been determined by the Fédération Cynologique Internationale to be part of the German Spitz breed; and in many countries, they are known as the Zwergspitz.",
            R.drawable.pomeranian
        ),
    )
}