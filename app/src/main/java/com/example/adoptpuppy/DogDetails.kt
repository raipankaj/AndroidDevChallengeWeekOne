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
package com.example.adoptpuppy

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.adoptpuppy.ui.theme.PurpleTheme
import com.example.adoptpuppy.ui.theme.purple700

@Composable
fun DogDetails(
    dogImageId: Int,
    dogAge: String?,
    dogName: String?,
    dogDetails: String?,
    dogGender: String?,
    navController: NavHostController
) {

    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.5f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .clip(CutCornerShape(bottomStartPercent = 40, bottomEndPercent = 40))
                    .border(
                        4.dp,
                        purple700,
                        CutCornerShape(bottomStartPercent = 40, bottomEndPercent = 40)
                    )
                    .border(
                        2.dp,
                        PurpleTheme,
                        CutCornerShape(bottomStartPercent = 40, bottomEndPercent = 40)
                    )
            ) {
                Image(
                    painter = painterResource(id = dogImageId), contentDescription = "dog",
                    contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
                )

                IconButton(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier.padding(4.dp)
                ) {
                    Icon(
                        Icons.Default.ArrowBack, contentDescription = "Go Back",
                        tint = Color.White
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = dogAge.toString(),
                        style = TextStyle(fontWeight = FontWeight.ExtraBold, color = PurpleTheme),
                        modifier = Modifier.padding(start = 12.dp, end = 12.dp)
                    )
                    Text(text = "Age", style = TextStyle(fontSize = 12.sp, color = PurpleTheme))
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = dogGender!!,
                        style = TextStyle(fontWeight = FontWeight.ExtraBold, color = PurpleTheme),
                        modifier = Modifier.padding(start = 12.dp, end = 12.dp)
                    )
                    Text(text = "Gender", style = TextStyle(fontSize = 12.sp, color = PurpleTheme))
                }
            }
        }

        Card(
            modifier = Modifier
                .weight(1.5f)
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = dogDetails!!,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState()),
                style = TextStyle(fontWeight = FontWeight.SemiBold, fontStyle = FontStyle.Italic)
            )
        }

        Spacer(modifier = Modifier.padding(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
                .background(MaterialTheme.colors.onPrimary)
                .clip(RoundedCornerShape(topEndPercent = 40, bottomEndPercent = 40))
                .padding(12.dp)
                .border(
                    2.dp,
                    Color.Gray,
                    RoundedCornerShape(topEndPercent = 40, bottomEndPercent = 40)
                )
                .clickable {
                    Toast
                        .makeText(context, "Thanks for adopting", Toast.LENGTH_SHORT)
                        .show()
                }
        ) {
            Text(
                text = "Adopt Now",
                textAlign = TextAlign.Center,
                style = TextStyle(color = Color.White),
                modifier = Modifier
                    .weight(1.7f)
                    .padding(8.dp)
                    .background(PurpleTheme)
                    .padding(14.dp)
            )

            Icon(
                Icons.Default.ArrowForward, contentDescription = "Next",
                modifier = Modifier.weight(0.3f)
            )
        }
    }
}
