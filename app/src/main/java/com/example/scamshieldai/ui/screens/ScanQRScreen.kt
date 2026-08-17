package com.example.scamshieldai.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.TorchState
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.scamshieldai.ui.theme.*
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.io.IOException
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

private const val TAG = "ScanQRScreen"

private fun createQrInputImage(context: Context, uri: Uri): InputImage {
    return try {
        InputImage.fromFilePath(context, uri)
    } catch (_: Exception) {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IOException("Gagal membuka file gambar")
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()
        if (bitmap == null) throw IOException("Format gambar tidak valid")
        InputImage.fromBitmap(bitmap, 0)
    }
}

@OptIn(ExperimentalGetImage::class)
@Composable
fun ScanQRScreen(
    onBack: () -> Unit,
    onScanned: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mainExecutor = remember { ContextCompat.getMainExecutor(context) }
    val handled = remember { AtomicBoolean(false) }
    val analysisExecutor = remember { Executors.newSingleThreadExecutor() }

    DisposableEffect(Unit) {
        onDispose {
            analysisExecutor.shutdown()
        }
    }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
        )
    }
    var cameraError by remember { mutableStateOf<String?>(null) }
    var torchEnabled by remember { mutableStateOf(false) }
    var boundCamera by remember { mutableStateOf<Camera?>(null) }
    var isProcessingGallery by remember { mutableStateOf(false) }

    val barcodeOptions = remember {
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC,
                Barcode.FORMAT_DATA_MATRIX,
                Barcode.FORMAT_PDF417
            )
            .build()
    }
    val scanner = remember { BarcodeScanning.getClient(barcodeOptions) }

    // Reset state saat layar aktif kembali
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                handled.set(false)
                isProcessingGallery = false
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
        if (!granted) {
            cameraError = "Izin kamera ditolak. Aktifkan di pengaturan aplikasi atau pilih QR dari Galeri."
        } else {
            cameraError = null
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        isProcessingGallery = true
        try {
            val image = createQrInputImage(context, uri)
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    isProcessingGallery = false
                    val raw = barcodes
                        .asSequence()
                        .mapNotNull { it.rawValue?.trim() }
                        .firstOrNull { it.isNotEmpty() }

                    if (raw != null) {
                        if (handled.compareAndSet(false, true)) {
                            onScanned(raw)
                        }
                    } else {
                        Toast.makeText(context, "Tidak ada kode QR yang terdeteksi pada gambar", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    isProcessingGallery = false
                    Toast.makeText(context, "Gagal memproses gambar QR", Toast.LENGTH_SHORT).show()
                }
        } catch (e: Exception) {
            isProcessingGallery = false
            Toast.makeText(context, "Gagal membaca file gambar", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        handled.set(false)
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    fun toggleTorch() {
        val camera = boundCamera ?: return
        if (!camera.cameraInfo.hasFlashUnit()) return
        val enable = !torchEnabled
        camera.cameraControl.enableTorch(enable)
        torchEnabled = enable
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (hasCameraPermission) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx).apply {
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    }
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                    cameraProviderFuture.addListener({
                        try {
                            val cameraProvider = cameraProviderFuture.get()
                            cameraProvider.unbindAll()

                            val preview = Preview.Builder().build().also {
                                it.setSurfaceProvider(previewView.surfaceProvider)
                            }

                            val imageAnalysis = ImageAnalysis.Builder()
                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
                                .build()

                            imageAnalysis.setAnalyzer(analysisExecutor) { imageProxy ->
                                val mediaImage = imageProxy.image
                                if (mediaImage == null || handled.get()) {
                                    imageProxy.close()
                                    return@setAnalyzer
                                }

                                val image = InputImage.fromMediaImage(
                                    mediaImage,
                                    imageProxy.imageInfo.rotationDegrees
                                )
                                scanner.process(image)
                                    .addOnSuccessListener { barcodes ->
                                        if (handled.get()) return@addOnSuccessListener
                                        val raw = barcodes
                                            .asSequence()
                                            .mapNotNull { it.rawValue?.trim() }
                                            .firstOrNull { it.isNotEmpty() }
                                            ?: return@addOnSuccessListener

                                        if (handled.compareAndSet(false, true)) {
                                            mainExecutor.execute {
                                                try {
                                                    cameraProvider.unbindAll()
                                                } catch (_: Exception) {
                                                }
                                                onScanned(raw)
                                            }
                                        }
                                    }
                                    .addOnFailureListener { err ->
                                        Log.w(TAG, "QR scan error: ${err.message}")
                                    }
                                    .addOnCompleteListener {
                                        imageProxy.close()
                                    }
                            }

                            val camera = cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                CameraSelector.DEFAULT_BACK_CAMERA,
                                preview,
                                imageAnalysis
                            )
                            boundCamera = camera
                            torchEnabled = camera.cameraInfo.torchState.value == TorchState.ON
                            cameraError = null
                        } catch (e: Exception) {
                            Log.e(TAG, "Gagal mengikat kamera", e)
                            cameraError = "Kamera tidak dapat dibuka. Anda tetap dapat memilih QR dari Galeri."
                        }
                    }, mainExecutor)

                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        // Overlay & Controls
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Scan QR",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Arahkan ke kode QRIS atau tautan",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }
                IconButton(
                    onClick = { toggleTorch() },
                    enabled = boundCamera?.cameraInfo?.hasFlashUnit() == true,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = if (torchEnabled) Icons.Default.FlashOn else Icons.Default.FlashOff,
                        contentDescription = "Flash",
                        tint = Color.White
                    )
                }
            }

            // Scanner Center Box
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                when {
                    !hasCameraPermission -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(32.dp)
                        ) {
                            Text(
                                text = "Izin Kamera Diperlukan",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Izinkan akses kamera untuk memindai QR langsung, atau pilih gambar QR dari galeri.",
                                color = Color.White.copy(alpha = 0.75f),
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Button(
                                onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                                colors = ButtonDefaults.buttonColors(containerColor = Cerulean)
                            ) {
                                Text("Izinkan Kamera", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    cameraError != null -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(32.dp)
                        ) {
                            Text(
                                text = cameraError ?: "",
                                color = Color.White,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    handled.set(false)
                                    cameraError = null
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Cerulean)
                            ) {
                                Text("Coba Lagi", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    else -> {
                        // Scanner Frame with Animated Pulse/Scan line
                        Box(
                            modifier = Modifier
                                .size(260.dp)
                                .border(2.dp, Cerulean, RoundedCornerShape(24.dp))
                        ) {
                            val infiniteTransition = rememberInfiniteTransition(label = "ScanLine")
                            val offsetRatio by infiniteTransition.animateFloat(
                                initialValue = 0f,
                                targetValue = 1f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(2000, easing = LinearEasing),
                                    repeatMode = RepeatMode.Reverse
                                ),
                                label = "LaserOffset"
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(2.dp)
                                    .offset(y = (offsetRatio * 256).dp)
                                    .background(Cerulean)
                            )
                        }
                    }
                }
            }

            // Bottom Actions & Gallery Option
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(WhiteBackground.copy(alpha = 0.95f))
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                    enabled = !isProcessingGallery
                ) {
                    if (isProcessingGallery) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Memindai Gambar...", color = Color.White)
                    } else {
                        Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Pilih Gambar QR dari Galeri", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Cerulean.copy(alpha = 0.05f))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = Cerulean,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        "Scan QRIS toko atau QR link untuk memeriksa keamanannya sebelum bayar/klik.",
                        color = PrussianBlue,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }
    }
}
