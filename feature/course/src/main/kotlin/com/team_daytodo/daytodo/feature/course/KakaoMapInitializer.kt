package com.team_daytodo.daytodo.feature.course

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Base64
import android.util.Log
import com.kakao.vectormap.KakaoMapSdk
import java.security.MessageDigest

object KakaoMapInitializer {
    private const val Tag = "KakaoMapAuth"

    fun initialize(context: Context): Result<Unit> {
        val appContext = context.applicationContext
        logRuntimeAuthInfo(appContext)

        if (!isSupportedAbi()) {
            Log.w(
                Tag,
                "Kakao Maps SDK is skipped because this device ABI is unsupported. supportedAbis=${Build.SUPPORTED_ABIS.joinToString()}",
            )
            return Result.success(Unit)
        }

        return runCatching {
            if (!KakaoMapSdk.isInitialized()) {
                KakaoMapSdk.init(appContext, BuildConfig.KAKAO_NATIVE_APP_KEY)
            }
        }.onFailure { error ->
            Log.e(Tag, "Failed to initialize Kakao Maps SDK.", error)
        }
    }

    fun logMapError(context: Context, error: Throwable) {
        logRuntimeAuthInfo(context.applicationContext)
        Log.e(Tag, "Kakao map authentication or rendering failed.", error)
    }

    fun logMapStartRequested(context: Context) {
        logRuntimeAuthInfo(context.applicationContext)
        Log.d(Tag, "Kakao map start requested.")
    }

    fun logMapViewCreated(context: Context) {
        if (!BuildConfig.DEBUG) return
        Log.d(Tag, "Kakao map view created.")
    }

    fun logMapLifecycle(context: Context, event: String) {
        if (!BuildConfig.DEBUG) return
        Log.d(Tag, "Kakao map lifecycle: $event")
    }

    fun logMapReady(context: Context) {
        logRuntimeAuthInfo(context.applicationContext)
        Log.d(Tag, "Kakao map is ready.")
    }

    fun isSupportedAbi(): Boolean =
        Build.SUPPORTED_ABIS.any { abi -> abi in SupportedAbis }

    fun primaryDeviceAbi(): String =
        Build.SUPPORTED_ABIS.firstOrNull().orEmpty()

    private fun logRuntimeAuthInfo(context: Context) {
        if (!BuildConfig.DEBUG) return

        val hashKey = runCatching {
            context.calculateKakaoKeyHash()
        }.getOrElse { error ->
            Log.w(Tag, "Failed to calculate Kakao map hash key.", error)
            "unavailable"
        }

        Log.d(
            Tag,
            "package=${context.packageName}, hash=$hashKey, nativeKeyLength=${BuildConfig.KAKAO_NATIVE_APP_KEY.length}, supportedAbis=${Build.SUPPORTED_ABIS.joinToString()}",
        )
    }

    private val SupportedAbis = setOf(
        "arm64-v8a",
        "armeabi-v7a",
    )
}

@Suppress("DEPRECATION")
private fun Context.calculateKakaoKeyHash(): String {
    val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
    } else {
        packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
    }
    val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val signingInfo = packageInfo.signingInfo ?: error("No signing info found.")
        if (signingInfo.hasMultipleSigners()) {
            signingInfo.apkContentsSigners.orEmpty()
        } else {
            signingInfo.signingCertificateHistory.orEmpty()
        }
    } else {
        packageInfo.signatures.orEmpty()
    }
    val signature = signatures.firstOrNull() ?: error("No signing certificate found.")
    val digest = MessageDigest.getInstance("SHA-1").digest(signature.toByteArray())

    return Base64.encodeToString(digest, Base64.NO_WRAP)
}
