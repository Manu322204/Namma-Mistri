package com.example.namma_mastri

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Models
data class Worker(
    val id: Long,
    val name: String,
    val work: String,
    val wage: String,
    var present: Boolean = false
)

data class ProgressPhoto(
    val id: Long,
    val title: String,
    val date: String,
    val imageRes: Int
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                var loggedIn by remember { mutableStateOf(false) }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF8F9FA)
                ) {
                    Crossfade(targetState = loggedIn, label = "auth") { isLogged ->
                        if (isLogged) {
                            MainDashboard()
                        } else {
                            LoginScreen { loggedIn = true }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF1565C0), Color(0xFF0D47A1))
                )
            )
            .imePadding(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            modifier = Modifier.fillMaxWidth().padding(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp).verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    modifier = Modifier.size(90.dp),
                    shape = CircleShape,
                    color = Color(0xFFE3F2FD)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier.padding(16.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Text(text = "Namma Mistri", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color(0xFF1565C0))
                Text(text = "Professional Site Management", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)

                OutlinedTextField(
                    value = email, onValueChange = { email = it },
                    label = { Text("Admin Email") },
                    leadingIcon = { Icon(Icons.Default.Email, null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )

                OutlinedTextField(
                    value = password, onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, null) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )

                Button(
                    onClick = {
                        if (email == "admin@gmail.com" && password == "1234") onLoginSuccess()
                        else error = "Invalid Credentials"
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
                ) {
                    Text("Login to Dashboard", fontWeight = FontWeight.Bold)
                }

                if (error.isNotEmpty()) {
                    Text(text = error, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDashboard() {
    var selectedScreen by remember { mutableIntStateOf(0) }
    val workers = remember { mutableStateListOf<Worker>() }
    val progressPhotos = remember {
        mutableStateListOf(
            ProgressPhoto(1, "Foundation Layer", "10 Oct", R.drawable.ic_launcher_background),
            ProgressPhoto(2, "Pillar Casting", "22 Oct", R.drawable.ic_launcher_background),
            ProgressPhoto(3, "Brick Work Start", "05 Nov", R.drawable.ic_launcher_background)
        )
    }
    var siteProgress by remember { mutableFloatStateOf(0.45f) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "🏗 Namma Mistri", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF1565C0),
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { }) { Icon(Icons.Default.Notifications, null, tint = Color.White) }
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                NavigationBarItem(
                    selected = selectedScreen == 0,
                    onClick = { selectedScreen = 0 },
                    icon = { Icon(Icons.Default.Home, null) },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = selectedScreen == 1,
                    onClick = { selectedScreen = 1 },
                    icon = { Icon(Icons.Default.Person, null) },
                    label = { Text("Labor") }
                )
                NavigationBarItem(
                    selected = selectedScreen == 2,
                    onClick = { selectedScreen = 2 },
                    icon = { Icon(Icons.Default.Build, null) },
                    label = { Text("Estimates") }
                )
                NavigationBarItem(
                    selected = selectedScreen == 3,
                    onClick = { selectedScreen = 3 },
                    icon = { Icon(Icons.Default.PhotoLibrary, null) },
                    label = { Text("Progress") }
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            AnimatedContent(targetState = selectedScreen, label = "screen_trans") { target ->
                when (target) {
                    0 -> HomeScreen(
                        workers.size, 
                        workers.count { it.present }, 
                        siteProgress,
                        onProgressChange = { siteProgress = it },
                        onNavigate = { selectedScreen = it }
                    )
                    1 -> WorkerScreen(workers)
                    2 -> CalculatorScreen()
                    3 -> ProgressGalleryScreen(progressPhotos)
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    totalWorkers: Int, 
    presentWorkers: Int, 
    progress: Float,
    onProgressChange: (Float) -> Unit,
    onNavigate: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)) {
        Text(text = "Site Progress", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("${(progress * 100).toInt()}% Completion", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = Color(0xFF1565C0))
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(Icons.Default.Star, null, tint = Color(0xFF1565C0))
                }
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(10.dp).clip(CircleShape),
                    color = Color(0xFF1565C0),
                    trackColor = Color.White
                )
                Slider(value = progress, onValueChange = onProgressChange, modifier = Modifier.padding(top = 8.dp))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SummaryCard("Workers", "$presentWorkers/$totalWorkers", Color(0xFFBBDEFB), Modifier.weight(1f))
            SummaryCard("Site Cost", "₹45K", Color(0xFFC8E6C9), Modifier.weight(1f))
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Quick Actions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        ActionCard("Mark Attendance", "Daily labor presence", Icons.Default.CheckCircle, Color(0xFFFFF9C4)) { onNavigate(1) }
        ActionCard("Material Estimates", "Bricks and cement", Icons.Default.Build, Color(0xFFFFE0B2)) { onNavigate(2) }
        ActionCard("Site Photos", "View progress gallery", Icons.Default.PhotoLibrary, Color(0xFFD1C4E9)) { onNavigate(3) }
    }
}

@Composable
fun SummaryCard(title: String, value: String, color: Color, modifier: Modifier) {
    Card(colors = CardDefaults.cardColors(containerColor = color), shape = RoundedCornerShape(20.dp), modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = value, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(text = title, fontSize = 14.sp)
        }
    }
}

@Composable
fun ActionCard(title: String, desc: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(color = color, shape = RoundedCornerShape(12.dp)) {
                Icon(icon, null, modifier = Modifier.padding(10.dp), tint = Color.Black)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold)
                Text(text = desc, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun WorkerScreen(workers: SnapshotStateList<Worker>) {
    var name by remember { mutableStateOf("") }
    var work by remember { mutableStateOf("") }
    var wage by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Labor Management", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Enroll New Labor", fontWeight = FontWeight.Bold)
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = work, onValueChange = { work = it }, label = { Text("Role") }, modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(value = wage, onValueChange = { wage = it }, label = { Text("Wage") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                }
                Button(
                    onClick = {
                        if (name.isNotEmpty() && work.isNotEmpty() && wage.isNotEmpty()) {
                            workers.add(Worker(System.currentTimeMillis(), name, work, wage))
                            name = ""; work = ""; wage = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Add to Roster") }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(workers, key = { it.id }) { worker ->
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp), shape = RoundedCornerShape(16.dp)) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = worker.name, fontWeight = FontWeight.Bold)
                            Text(text = "${worker.work} • ₹${worker.wage}/day", fontSize = 12.sp, color = Color.Gray)
                        }
                        Switch(
                            checked = worker.present,
                            onCheckedChange = { isPresent ->
                                val idx = workers.indexOfFirst { it.id == worker.id }
                                if (idx != -1) workers[idx] = workers[idx].copy(present = isPresent)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProgressGalleryScreen(photos: List<ProgressPhoto>) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Site Progress Photos", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Button(onClick = { }, shape = RoundedCornerShape(12.dp)) {
                Icon(Icons.Default.Add, null)
                Text("Add Photo")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(photos) { photo ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column {
                        Image(
                            painter = painterResource(id = photo.imageRes),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth().height(120.dp),
                            contentScale = ContentScale.Crop
                        )
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = photo.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(text = photo.date, fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalculatorScreen() {
    var length by remember { mutableStateOf("") }
    var width by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(20.dp).verticalScroll(rememberScrollState())) {
        Text(text = "Material Estimator", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(4.dp)) {
            Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Brick Wall Parameters", fontWeight = FontWeight.Bold, color = Color(0xFF1565C0))
                OutlinedTextField(value = length, onValueChange = { length = it }, label = { Text("Length (ft)") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = width, onValueChange = { width = it }, label = { Text("Thickness (ft)") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = height, onValueChange = { height = it }, label = { Text("Height (ft)") }, modifier = Modifier.fillMaxWidth())
                
                Button(onClick = {
                    val l = length.toDoubleOrNull() ?: 0.0
                    val w = width.toDoubleOrNull() ?: 0.0
                    val h = height.toDoubleOrNull() ?: 0.0
                    val vol = l * w * h
                    val cement = vol * 0.4
                    val bricks = vol * 8
                    result = "Estimated Requirement:\n• Total Volume: ${"%.1f".format(vol)} cft\n• Cement: ${cement.toInt()} bags\n• Bricks: ${bricks.toInt()} pieces"
                }, modifier = Modifier.fillMaxWidth().height(50.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))) {
                    Text("Generate Estimate", fontWeight = FontWeight.Bold)
                }
            }
        }

        if (result.isNotEmpty()) {
            Card(modifier = Modifier.padding(top = 20.dp).fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)), shape = RoundedCornerShape(12.dp)) {
                Text(text = result, modifier = Modifier.padding(20.dp), fontWeight = FontWeight.Medium, lineHeight = 28.sp)
            }
        }
    }
}
