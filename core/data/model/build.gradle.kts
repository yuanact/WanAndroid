plugins {
    alias(libs.plugins.local.android.compose.library)
    id("kotlin-parcelize")
}

android {
    namespace = "top.chengdongqing.weui.core.data.model"
}

dependencies {
    implementation(libs.amap)
}