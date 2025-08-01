package com.riveronly.wanandroid

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.riveronly.wanandroid.ui.screen.HomeScreen
import com.riveronly.wanandroid.ui.screen.MineScreen
import com.riveronly.wanandroid.ui.screen.PlazaScreen
import com.riveronly.wanandroid.ui.theme.WanAndroidTheme
import com.riveronly.wanandroid.ui.web.WebViewManager
import com.riveronly.wanandroid.utils.PermissionsHelper
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private var exitTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        enableEdgeToEdge()

        setContentView()

        //连续两次返回退到桌面
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis() - exitTime > 2000) {
                    exitTime = System.currentTimeMillis()
                    val msg = getString(R.string.back_twice_to_launcher)
                    Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                } else {
                    moveTaskToBack(true)
                }
            }
        })
        //webView预加载
        WebViewManager.prepare(applicationContext)
        //必要权限申请
        PermissionsHelper.init(this)
            .requestStoragePermission(
                onGranted = {

                },
                onDenied = {

                }
            )
    }

    override fun onDestroy() {
        super.onDestroy()
        WebViewManager.destroy()
    }

    enum class Tab(
        val title: String,
        @field:DrawableRes val iconResNormal: Int,
        @field:DrawableRes val iconResFill: Int,
        val isEnable: Boolean = true,
    ) {
        Home("首页", R.drawable.home_24px, R.drawable.home_fill_24px),
        Plaza("广场", R.drawable.dashboard_24px, R.drawable.dashboard_fill_24px),
        Mine("我的", R.drawable.face_24px, R.drawable.face_fill_24px),
    }

    private fun setContentView() {
        setContent {
            WanAndroidTheme {
                val pagerState = rememberPagerState(
                    initialPage = Tab.Home.ordinal,
                    pageCount = { Tab.entries.size }
                )
                val scope = rememberCoroutineScope()
                val currentPagerIndex = pagerState.currentPage
                val isInitList by
                rememberSaveable {
                    mutableStateOf(BooleanArray(Tab.entries.size) { index -> index == currentPagerIndex })
                }

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            Tab.entries.forEachIndexed { index, tab ->
                                if (!tab.isEnable) {
                                    return@forEachIndexed
                                }
                                val isSelected = currentPagerIndex == index
                                NavigationBarItem(
                                    label = { Text(tab.title) },
                                    selected = isSelected,
                                    onClick = {
                                        scope.launch {
                                            isInitList[index] = true
                                            pagerState.scrollToPage(index)
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            painterResource(
                                                if (isSelected) tab.iconResFill
                                                else tab.iconResNormal
                                            ),
                                            contentDescription = ""
                                        )
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    HorizontalPager(
                        state = pagerState,
                        userScrollEnabled = false,
                        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
                    ) { index ->
                        if (isInitList[index]) {
                            when (Tab.entries[index]) {
                                Tab.Home -> HomeScreen()
                                Tab.Plaza -> PlazaScreen()
                                Tab.Mine -> MineScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}
