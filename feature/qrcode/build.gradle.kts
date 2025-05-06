plugins {
    alias(libs.plugins.local.android.compose.library)
}

android {
    namespace = "top.chengdongqing.weui.feature.qrcode"
}

dependencies {
    implementation(libs.androidx.navigation.compose)
    implementation(libs.accompanist.permissions)
    implementation(libs.mlkit.barcode.scanning)
    implementation(libs.bundles.camerax)
    implementation(libs.zxing.core)

    implementation(project(":core:data:model"))
    implementation(project(":core:ui:theme"))
    implementation(project(":core:ui:components"))
    implementation(project(":core:utils"))
}