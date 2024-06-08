import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.key.Key.Companion.R
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import screen.AdjustAgeAndRiskOnClusters
import screen.ClustersScreen
import screen.SharedObj
import tiaahackathon.composeapp.generated.resources.Res
import tiaahackathon.composeapp.generated.resources.age_and_risk_screen_ic
import tiaahackathon.composeapp.generated.resources.home_ic

object TabC: Tab {
    @Composable
    override fun Content() {
        SharedObj.apply {
            Navigator(AdjustAgeAndRiskOnClusters())
        }
    }

    override val options: TabOptions
        @Composable
        get()  {
            val title = ""
            val icon = painterResource(Res.drawable.age_and_risk_screen_ic)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }
}