import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.key.Key.Companion.R
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.jetbrains.compose.resources.stringResource
import screen.ClustersScreen
import screen.SharedObj

object TabD: Tab {
    @Composable
    override fun Content() {
        SharedObj.apply {
            ClustersScreen(risk, peroidOfInvestment, ROI, principalAmount, currentAge)
        }
    }

    override val options: TabOptions
        @Composable
        get()  {
            val title = ""
            val icon = rememberVectorPainter(Icons.Default.Home)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }
}