import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun WeatherIcon(iconCode: String, modifier: Modifier = Modifier) {
    GlideImage(
        model = "https://openweathermap.org/img/wn/${iconCode}@2x.png",
        contentDescription = "Weather Icon",
        modifier = modifier
    )
}
