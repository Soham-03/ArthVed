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
import screen.ClustersScreen
import screen.PlaygroundScreen
import screen.SharedObj
import tiaahackathon.composeapp.generated.resources.Res
import tiaahackathon.composeapp.generated.resources.home_ic
import tiaahackathon.composeapp.generated.resources.playground

object TabB: Tab {
    @Composable
    override fun Content() {
        SharedObj.apply {
            Navigator(PlaygroundScreen())
        }
    }

    override val options: TabOptions
        @Composable
        get()  {
            val title = ""
            val icon = painterResource(Res.drawable.playground)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }
}