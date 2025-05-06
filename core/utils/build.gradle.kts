plugins {
    alias(libs.plugins.local.android.compose.library)
}

android {
    namespace = "top.chengdongqing.weui.core.utils"
}

dependencies {
    implementation(libs.accompanist.permissions)
    implementation(libs.amap)

    implementation(project(":core:data:model"))
    implementation(project(":core:ui:theme"))
}