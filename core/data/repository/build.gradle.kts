plugins {
    alias(libs.plugins.local.android.compose.library)
}

android {
    namespace = "top.chengdongqing.core.data.repository"
}

dependencies {
    implementation(project(":core:data:model"))
}