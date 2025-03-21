import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tourpal.ui.theme.*
import com.tourpal.ui.components.TourPalLogo

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {

        // Logo
        TourPalLogo(size = 50, text=false)
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Explore Tour Plans",
            color = White,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview
@Composable
fun TopBarPreview(){
    TourPalTheme {
        TopBar()
    }
}
