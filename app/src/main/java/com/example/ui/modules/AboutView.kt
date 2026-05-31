package com.example.ui.modules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutView(
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About LifeSphere OS", color = SmoothWhite, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Return", tint = SmoothWhite)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SpaceDeepAbyss)
            )
        },
        containerColor = SpaceDeepAbyss
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // App Banner details
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SpaceDarkSteel)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🌟", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("LifeSphere OS", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = CosmicCyan, textAlign = TextAlign.Center)
                    Text("Your Life. Organized. Remembered. Evolving.", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = CosmicTeal, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Version 1.0.0", fontSize = 11.sp, color = CosmicGrey)
                }
            }

            // Overview Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SpaceDarkSteel)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = "Info", tint = CosmicCyan, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("App Overview", fontWeight = FontWeight.Bold, color = SmoothWhite, fontSize = 15.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "LifeSphere OS is a next-generation Personal Life Operating System designed to help users organize their memories, goals, habits, projects, knowledge, and daily activities in one unified platform.\n\nBuilt with a privacy-first and offline-first approach, LifeSphere OS empowers users to manage their digital life efficiently while keeping complete control over their personal data in local database systems.",
                        fontSize = 13.sp,
                        color = IceGreyText,
                        lineHeight = 18.sp
                    )
                }
            }

            // Creator Info
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SpaceDarkSteel)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Code, contentDescription = "Code creator", tint = CosmicCyan, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("About the Creator", fontWeight = FontWeight.Bold, color = SmoothWhite, fontSize = 15.sp)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Prince AR Abdur Rahman", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = CosmicCyan)
                    Text("Founder, Product Designer, and Developer", fontSize = 12.sp, color = CosmicTeal, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Prince AR Abdur Rahman is passionate about creating innovative digital products, educational technologies, productivity tools, and life-management systems that help people learn, grow, and achieve their goals.",
                        fontSize = 13.sp,
                        color = IceGreyText,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Areas of Interest:", fontWeight = FontWeight.SemiBold, color = SmoothWhite, fontSize = 12.sp)
                    BulletItem("Mobile App Development & Architecture")
                    BulletItem("Productivity Systems & Deep Work structures")
                    BulletItem("Personal Knowledge Management (PKM)")
                    BulletItem("User Experience Design & Glassmorphism Aesthetics")
                    BulletItem("Offline-First Technologies & Secured SQL databases")
                }
            }

            // Company & Studio Info
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SpaceDarkSteel)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("About the Company", fontWeight = FontWeight.Bold, color = SmoothWhite, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("NexVora Labs Ofc", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = CosmicCyan)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "NexVora Labs Ofc is a digital innovation studio focused on building modern, privacy-friendly, and user-centered applications.\n\nOur mission is to create powerful tools that simplify life, improve productivity, support education, and help users manage their digital world effectively.",
                        fontSize = 13.sp,
                        color = IceGreyText,
                        lineHeight = 18.sp
                    )
                }
            }

            // Contact Specifications
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SpaceDarkSteel)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AlternateEmail, contentDescription = "Email", tint = CosmicCyan, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Connect", fontWeight = FontWeight.Bold, color = SmoothWhite, fontSize = 15.sp)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Email Workspace Direct:", fontSize = 11.sp, color = CosmicGrey)
                    Text("prince.ar.abdur.rahman2008@gmail.com", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = CosmicCyan)
                    
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("GitHub Engine Repository:", fontSize = 11.sp, color = CosmicGrey)
                    Text("rahman2008-svg", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = CosmicCyan)
                }
            }

            // Backing and copyright ending bounds
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Copyright © NexVora Labs Ofc. All Rights Reserved.",
                color = CosmicGrey,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun BulletItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(CosmicCyan)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontSize = 12.sp, color = IceGreyText)
    }
}
