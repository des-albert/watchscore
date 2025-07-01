/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package org.db.watchscore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.MaterialTheme
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.TimeText
import androidx.wear.compose.material3.TimeTextDefaults
import androidx.wear.compose.material3.timeTextCurvedText
import org.db.watchscore.theme.WatchScoreTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {

    super.onCreate(savedInstanceState)

    setTheme(android.R.style.Theme_DeviceDefault)

    setContent {
      WatchScore()
    }
  }
}

@Composable
fun WatchScore() {
  WatchScoreTheme {
    val pagerState = rememberPagerState(pageCount = { 2 })

    HorizontalPager(
      state = pagerState,
      modifier = Modifier.fillMaxSize()
    ) { page ->
      when (page) {
        0 -> Score()
        1 -> Timer()
      }
    }
  }
}

@Composable
fun Score(
  modifier: Modifier = Modifier,
  playerViewModel: MainViewModel = viewModel()
) {
  val players = playerViewModel.players
  var scoreVisible by remember { mutableStateOf(true) }
  var lossLeft by remember { mutableStateOf("") }
  var placeLeft by remember { mutableStateOf("") }
  var lossRight by remember { mutableStateOf("") }
  var placeRight by remember { mutableStateOf("") }
  var winner by remember { mutableIntStateOf(0) }

  val imageMap = mapOf(
    "DB" to R.drawable.db,
    "Bo" to R.drawable.bo,
    "Steve" to R.drawable.steve
  )


  Surface(
    color = MaterialTheme.colorScheme.errorContainer,
  ) {


    Column(
      modifier = Modifier
        .fillMaxHeight()
        .background(MaterialTheme.colorScheme.errorContainer),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {

      if (scoreVisible) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Box(
            modifier = Modifier
              .height(12.dp)
              .width(40.dp)
              .background(MaterialTheme.colorScheme.errorContainer),
          )
          {
            val timeStyle = TimeTextDefaults.timeTextStyle(
              color = MaterialTheme.colorScheme.outline
            )
            TimeText(
              content = { time -> timeTextCurvedText(time, style = timeStyle)}
            )
          }
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Center
        )
        {
          for ((index, score) in players.withIndex()) {
            Column(
              modifier = modifier.padding(4.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {

              val resourceId = imageMap[score.name]
              if (resourceId != null) {

                Box(
                  modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(6.dp))
                )
                {
                  Image(
                    painter = painterResource(id = resourceId),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                  )
                }
              }
              Spacer(modifier = Modifier.height(5.dp))

              ElevatedButton(
                colors = ButtonDefaults.elevatedButtonColors(
                  containerColor = MaterialTheme.colorScheme.tertiary,
                  contentColor = MaterialTheme.colorScheme.error
                ),
                onClick = {
                  scoreVisible = false
                  winner = index
                  when (index) {
                    0 -> {
                      placeLeft = players[1].name
                      placeRight = players[2].name

                    }

                    1 -> {
                      placeLeft = players[0].name
                      placeRight = players[2].name

                    }

                    2 -> {
                      placeLeft = players[0].name
                      placeRight = players[1].name

                    }
                  }
                },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                  .height(18.dp)
                  .width(48.dp)
              ) {
                Text(
                  text = score.name,
                  fontSize = 12.sp,
                  color = MaterialTheme.colorScheme.primaryContainer
                )
              }

              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = score.total.toString(),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = score.wins.toString(),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary
              )
            }
          }
        }


      } else {

        // Score Input

        val resourceId = imageMap[players[winner].name]
        Row(
          modifier = Modifier
            .padding(16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically

        ) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(RoundedCornerShape(6.dp))
          )
          {
            if (resourceId != null) {
              Image(
                painter = painterResource(id = resourceId),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
              )
            }
          }
          Text(
            text = "   ${players[winner].name} won",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 2.dp)
          )
        }

        Spacer(modifier = Modifier.height(4.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceEvenly,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Enter scores",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 2.dp)
          )
        }
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceEvenly,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .border(
                1.dp,
                MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(6.dp)
              )
              .padding(4.dp)
              .height(20.dp)
              .width(60.dp)
          ) {

            BasicTextField(
              value = lossLeft,
              onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                  lossLeft = newValue
                }
              },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              textStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                fontSize = 16.sp
              ),
              modifier = Modifier.width(80.dp)

            )
          }
          Box(
            modifier = Modifier
              .border(
                1.dp,
                MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(6.dp)
              )
              .padding(4.dp)
              .height(20.dp)
              .width(60.dp)
          ) {
            BasicTextField(
              value = lossRight,
              onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                  lossRight = newValue
                }
              },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              textStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                fontSize = 16.sp
              ),
              modifier = Modifier.width(80.dp)
            )
          }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceEvenly,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = placeLeft,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.padding(bottom = 2.dp)
          )
          Text(
            text = placeRight,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.padding(bottom = 2.dp)
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        ElevatedButton(
          colors = ButtonDefaults.elevatedButtonColors(
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
            contentColor = MaterialTheme.colorScheme.primaryContainer,
          ),
          onClick = {

            when (winner) {
              0 -> {
                players[0] = players[0].copy(
                  total = players[0].total + lossLeft.toInt() + lossRight.toInt(),
                  wins = players[0].wins + 1
                )
                players[1] = players[1].copy(
                  total = players[1].total - lossLeft.toInt()
                )
                players[2] = players[2].copy(
                  total = players[2].total - lossRight.toInt()
                )
              }

              1 -> {
                players[1] = players[1].copy(
                  total = players[1].total + lossLeft.toInt() + lossRight.toInt(),
                  wins = players[1].wins + 1
                )
                players[0] = players[0].copy(
                  total = players[0].total - lossLeft.toInt()
                )
                players[2] = players[2].copy(
                  total = players[2].total - lossRight.toInt()
                )
              }

              2 -> {
                players[2] = players[2].copy(
                  total = players[2].total + lossLeft.toInt() + lossRight.toInt(),
                  wins = players[2].wins + 1
                )
                players[0] = players[0].copy(
                  total = players[0].total - lossLeft.toInt()
                )
                players[1] = players[1].copy(
                  total = players[1].total - lossRight.toInt()
                )
              }
            }
            scoreVisible = true
            lossLeft = ""
            lossRight = ""

          },
          contentPadding = PaddingValues(0.dp),
          modifier = Modifier
            .height(18.dp)
            .width(40.dp)
        ) {
          Text(
            text = "Save",
            fontSize = 14.sp
          )
        }
      }
    }
  }
}


@Composable
fun Timer(
  playerViewModel: MainViewModel = viewModel()
) {

  var isRunning by remember { mutableStateOf(false) }
  val context = LocalContext.current
  var duration by remember { mutableIntStateOf(1) }
  val timeLeft by playerViewModel.timeLeft.collectAsState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.errorContainer),
  ) {
    Spacer(modifier = Modifier.height(20.dp))
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceEvenly
    ) {
      ElevatedButton(
        colors = ButtonDefaults.elevatedButtonColors(
          containerColor = MaterialTheme.colorScheme.tertiary,
          contentColor = MaterialTheme.colorScheme.error,
        ),
        onClick = {
          if (isRunning) {
            playerViewModel.stopTimer()
          } else {
            playerViewModel.startTimer(120, context)
          }
          isRunning = !isRunning
          duration = 120
        })
      {
        Text(
          text = if (isRunning && duration == 120) "Cancel" else "2:00",
          fontSize = 18.sp,
          color = MaterialTheme.colorScheme.primaryContainer
        )
      }

    }
    Spacer(modifier = Modifier.height(40.dp))
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.Center
    ) {
      Text(
        text = "${playerViewModel.formatTime(timeLeft, Locale.getDefault())} ",
        fontSize = 48.sp,
        color = MaterialTheme.colorScheme.secondary
      )

    }
  }
}

@Preview
@Composable
fun WatchScorePreview() {

  Score()
}