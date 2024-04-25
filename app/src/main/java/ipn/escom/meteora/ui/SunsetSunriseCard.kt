package ipn.escom.meteora.ui

import android.graphics.Paint
import android.graphics.Path
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ipn.escom.meteora.R
import ipn.escom.meteora.ui.theme.amber
import ipn.escom.meteora.utils.getHourOfDayFromLong
import ipn.escom.meteora.utils.getHourWithMinutesString
import ipn.escom.meteora.utils.getHoursAndMinutesDiff

@Composable
fun SunriseSunsetCardContent(
    sunriseHour: Long,
    sunsetHour: Long,
    currentTime: Long,
    modifier: Modifier = Modifier
) {
    val sunriseHourInt = getHourOfDayFromLong(sunriseHour)
    val sunsetHourInt = getHourOfDayFromLong(sunsetHour)

    ParameterCard(title = stringResource(R.string.sunrise_sunset), modifier = modifier.padding(horizontal = 16.dp)) {
        Box {
            SunriseSunsetVisualizer(
                sunriseHour = sunriseHourInt,
                sunsetHour = sunsetHourInt,
                currentHour = getHourOfDayFromLong(currentTime),
                modifier = Modifier.height(140.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp)
            ) {
                Text(
                    text = stringResource(R.string.sunrise),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = getHourWithMinutesString(sunriseHour),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
            ) {
                Text(
                    text = stringResource(R.string.sunset),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = getHourWithMinutesString(sunsetHour),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        val (dayLengthHours, dayLengthMinutes) = sunriseHour.getHoursAndMinutesDiff(sunsetHour)
        if (dayLengthHours > 0 || dayLengthMinutes > 0) {
            val dayLengthString = if (dayLengthHours > 0) {
                stringResource(
                    R.string.hours_minutes_x_y,
                    dayLengthHours,
                    dayLengthMinutes
                )
            } else {
                stringResource(R.string.minutes_x, dayLengthMinutes)
            }
            Text(
                text = stringResource(R.string.length_of_day) + dayLengthString,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        val (remainingHours, remainingMinutes) = currentTime.getHoursAndMinutesDiff(sunsetHour)
        if (remainingHours > 0 || remainingMinutes > 0) {
            val remainingDaylightString = if (remainingHours > 0) {
                stringResource(R.string.hours_minutes_x_y, remainingHours, remainingMinutes)
            } else {
                stringResource(R.string.minutes_x, dayLengthMinutes)
            }
            Text(
                text = stringResource(R.string.remaining_daylight_x) + remainingDaylightString,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun SunriseSunsetVisualizer(
    sunriseHour: Int,
    sunsetHour: Int,
    currentHour: Int,
    modifier: Modifier = Modifier
) {
    val colorPrimary = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
    val tertiaryPrimary = MaterialTheme.colorScheme.primary
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val dayStart = ((sunriseHour / 24f))
        val dayEnd = ((sunsetHour / 24f))
        val scaleX = canvasWidth / 24f
        val scaleY = canvasHeight / 2f
        var interval = (dayEnd - dayStart) / 2f
        var interval2 = (1f - dayEnd + dayStart) / 2f
        var start = dayStart - (1f - dayEnd + dayStart)
        interval *= 24f * scaleX
        interval2 *= 24f * scaleX
        start *= 24f * scaleX

        drawLine(
            color = colorPrimary,
            start = Offset(0f, canvasHeight / 2),
            end = Offset(canvasWidth, canvasHeight / 2),
            strokeWidth = 1f
        )
        drawIntoCanvas {
            val canvas = it.nativeCanvas
            val sunrisePaint = Paint().apply {
                isAntiAlias = true
                style = Paint.Style.FILL
                color = colorPrimary.toArgb()
            }
            val sunsetPaint = Paint().apply {
                isAntiAlias = true
                style = Paint.Style.FILL
                color = tertiaryPrimary.toArgb()
            }
            val futurePaint = Paint().apply {
                isAntiAlias = true
                style = Paint.Style.FILL
                color = secondaryColor.toArgb()
            }
            val path = Path().apply {
                moveTo(start, scaleY)
                rQuadTo(interval2, scaleY * ((interval2 / interval + 1f) / 2f), interval2 * 2f, 0f)
                rQuadTo(interval, -scaleY * ((interval / interval2 + 1f) / 2f), interval * 2f, 0f)
                rQuadTo(interval2, scaleY * ((interval2 / interval + 1f) / 2f), interval2 * 2f, 0f)
                rQuadTo(interval, -scaleY * ((interval / interval2 + 1f) / 2f), interval * 2f, 0f)
            }
            canvas.clipPath(path)
            canvas.drawRect(0f, 0f, scaleX * currentHour, scaleY, sunrisePaint)
            canvas.drawRect(0f, scaleY, scaleX * currentHour, canvasHeight, sunsetPaint)
            canvas.drawRect(scaleX * currentHour, 0f, canvasWidth, canvasHeight, futurePaint)
        }
    }
}

@Preview
@Composable
fun PreviewSunriseSunsetCard() {
    SunriseSunsetCardContent(
        sunriseHour = 6,
        sunsetHour = 18,
        currentTime = 12,
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
    )
}