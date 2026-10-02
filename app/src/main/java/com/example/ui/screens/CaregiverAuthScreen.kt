package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FontSizeScale
import com.example.i18n.StringsProvider
import com.example.ui.components.AccessibilityHeader
import com.example.ui.components.AccessibleButton
import com.example.ui.components.DisclaimerBanner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaregiverAuthScreen(
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    voiceEnabled: Boolean,
    onToggleVoice: () -> Unit,
    highContrast: Boolean,
    onToggleHighContrast: () -> Unit,
    fontSizeScale: FontSizeScale,
    onCycleFontSize: () -> Unit,
    onSpeakContext: () -> Unit,
    initialTabIsSignUp: Boolean = false,
    onLogin: (email: String, pass: String, onError: (String) -> Unit) -> Unit,
    onSignUp: (
        name: String,
        email: String,
        pass: String,
        state: String,
        patientCode: String,
        relationship: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) -> Unit = { _, _, _, _, _, _, _, _ -> },
    onQuickDemo: () -> Unit,
    onNavigateBack: () -> Unit,
    onSwitchToPatient: () -> Unit
) {
    var isSignUp by remember { mutableStateOf(initialTabIsSignUp) }
    val scrollState = rememberScrollState()

    // Sign In states
    var loginEmail by remember { mutableStateOf("") }
    var loginPassword by remember { mutableStateOf("") }

    // Sign Up states
    var name by remember { mutableStateOf("") }
    var signUpEmail by remember { mutableStateOf("") }
    var signUpPassword by remember { mutableStateOf("") }
    var selectedState by remember { mutableStateOf("Assam") }
    var stateMenuExpanded by remember { mutableStateOf(false) }
    var relationship by remember { mutableStateOf("Family Caregiver") }
    var initialPatientCode by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    val neStates = listOf(
        "Assam",
        "Meghalaya",
        "Manipur",
        "Mizoram",
        "Nagaland",
        "Tripura",
        "Arunachal Pradesh",
        "Sikkim"
    )

    val relationships = listOf(
        "Family Caregiver",
        "Son / Daughter",
        "Spouse / Partner",
        "Professional Nurse",
        "Doctor / Clinician",
        "Community Volunteer"
    )

    val bg = if (highContrast) Color(0xFF0F172A) else Color(0xFFF8FAFC)
    val textPrimary = if (highContrast) Color.White else Color(0xFF0F172A)

    Scaffold(
        topBar = {
            AccessibilityHeader(
                currentLanguage = currentLanguage,
                onLanguageSelected = onLanguageSelected,
                voiceEnabled = voiceEnabled,
                onToggleVoice = onToggleVoice,
                highContrast = highContrast,
                onToggleHighContrast = onToggleHighContrast,
                fontSizeScale = fontSizeScale,
                onCycleFontSize = onCycleFontSize,
                onSpeakCurrentContext = onSpeakContext,
                title = if (isSignUp) StringsProvider.get("caregiver_signup_title", currentLanguage) else StringsProvider.get("caregiver_signin_title", currentLanguage)
            )
        },
        containerColor = bg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateBack() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = StringsProvider.get("btn_back_welcome", currentLanguage),
                    tint = if (highContrast) Color(0xFFFACC15) else Color(0xFF0D5C75)
                )
                Text(
                    text = " " + StringsProvider.get("btn_back_welcome", currentLanguage),
                    fontWeight = FontWeight.SemiBold,
                    color = if (highContrast) Color(0xFFFACC15) else Color(0xFF0D5C75),
                    fontSize = (15f * fontSizeScale.scale).sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tab Selector: Sign In vs Sign Up
            TabRow(
                selectedTabIndex = if (isSignUp) 1 else 0,
                containerColor = if (highContrast) Color(0xFF1E293B) else Color(0xFFE2E8F0),
                contentColor = if (highContrast) Color(0xFFFACC15) else Color(0xFF0D5C75)
            ) {
                Tab(
                    selected = !isSignUp,
                    onClick = {
                        isSignUp = false
                        errorMessage = null
                        successMessage = null
                    },
                    text = {
                        Text(
                            text = StringsProvider.get("tab_signin", currentLanguage),
                            fontWeight = FontWeight.Bold,
                            fontSize = (15f * fontSizeScale.scale).sp
                        )
                    }
                )
                Tab(
                    selected = isSignUp,
                    onClick = {
                        isSignUp = true
                        errorMessage = null
                        successMessage = null
                    },
                    text = {
                        Text(
                            text = StringsProvider.get("tab_signup", currentLanguage),
                            fontWeight = FontWeight.Bold,
                            fontSize = (15f * fontSizeScale.scale).sp
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Shield / Secure Caregiver info banner
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (highContrast) Color(0xFF1E293B) else Color(0xFFEFF6FF),
                border = BorderStroke(
                    1.dp,
                    if (highContrast) Color(0xFF38BDF8) else Color(0xFFBFDBFE)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = if (highContrast) Color(0xFF38BDF8) else Color(0xFF1D4ED8),
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    Column {
                        Text(
                            text = StringsProvider.get("caregiver_shield_title", currentLanguage),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (highContrast) Color.White else Color(0xFF1E3A8A)
                        )
                        Text(
                            text = if (isSignUp) {
                                StringsProvider.get("caregiver_signup_desc", currentLanguage)
                            } else {
                                StringsProvider.get("caregiver_shield_desc", currentLanguage)
                            },
                            fontSize = 12.sp,
                            color = if (highContrast) Color(0xFFCBD5E1) else Color(0xFF3B82F6),
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Error Banner
            if (!errorMessage.isNullOrBlank()) {
                Surface(
                    color = if (highContrast) Color(0xFF7F1D1D) else Color(0xFFFEE2E2),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                ) {
                    Text(
                        text = errorMessage ?: "",
                        color = if (highContrast) Color(0xFFFECACA) else Color(0xFFB91C1C),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // Success Banner
            if (!successMessage.isNullOrBlank()) {
                Surface(
                    color = if (highContrast) Color(0xFF14532D) else Color(0xFFDCFCE7),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                ) {
                    Text(
                        text = successMessage ?: "",
                        color = if (highContrast) Color(0xFFBBF7D0) else Color(0xFF15803D),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            if (!isSignUp) {
                // ------------------ SIGN IN FORM ------------------
                Text(
                    text = StringsProvider.get("caregiver_signin_title", currentLanguage),
                    fontSize = (20f * fontSizeScale.scale).sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
                Text(
                    text = StringsProvider.get("caregiver_signin_desc", currentLanguage),
                    fontSize = (13f * fontSizeScale.scale).sp,
                    color = textPrimary.copy(alpha = 0.7f),
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = loginEmail,
                    onValueChange = { loginEmail = it },
                    label = { Text(StringsProvider.get("label_email", currentLanguage)) },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("input_caregiver_email"),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = loginPassword,
                    onValueChange = { loginPassword = it },
                    label = { Text(StringsProvider.get("label_password", currentLanguage)) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth().testTag("input_caregiver_pass"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(18.dp))

                AccessibleButton(
                    text = StringsProvider.get("btn_caregiver_login", currentLanguage),
                    onClick = {
                        if (loginEmail.isBlank() || loginPassword.isBlank()) {
                            errorMessage = "Please enter caregiver email and password."
                        } else {
                            onLogin(loginEmail, loginPassword) { err -> errorMessage = err }
                        }
                    },
                    isPrimary = true,
                    highContrast = highContrast,
                    fontSizeScale = fontSizeScale,
                    testTag = "btn_caregiver_submit_login"
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(onClick = { isSignUp = true; errorMessage = null }) {
                        Text(
                            text = "Don't have an account? " + StringsProvider.get("tab_signup", currentLanguage),
                            fontWeight = FontWeight.SemiBold,
                            color = if (highContrast) Color(0xFF38BDF8) else Color(0xFF0D5C75)
                        )
                    }
                }
            } else {
                // ------------------ SIGN UP FORM ------------------
                Text(
                    text = StringsProvider.get("caregiver_signup_title", currentLanguage),
                    fontSize = (20f * fontSizeScale.scale).sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
                Text(
                    text = StringsProvider.get("caregiver_signup_desc", currentLanguage),
                    fontSize = (13f * fontSizeScale.scale).sp,
                    color = textPrimary.copy(alpha = 0.7f),
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(StringsProvider.get("label_full_name", currentLanguage)) },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("input_caregiver_signup_name"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = signUpEmail,
                    onValueChange = { signUpEmail = it },
                    label = { Text(StringsProvider.get("label_email", currentLanguage)) },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("input_caregiver_signup_email"),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = signUpPassword,
                    onValueChange = { signUpPassword = it },
                    label = { Text(StringsProvider.get("label_password", currentLanguage)) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth().testTag("input_caregiver_signup_pass"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Location / State Dropdown
                ExposedDropdownMenuBox(
                    expanded = stateMenuExpanded,
                    onExpandedChange = { stateMenuExpanded = !stateMenuExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedState,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(StringsProvider.get("label_location_state", currentLanguage)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = stateMenuExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .testTag("dropdown_caregiver_state")
                    )
                    ExposedDropdownMenu(
                        expanded = stateMenuExpanded,
                        onDismissRequest = { stateMenuExpanded = false }
                    ) {
                        neStates.forEach { st ->
                            DropdownMenuItem(
                                text = { Text(st) },
                                onClick = {
                                    selectedState = st
                                    stateMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Optional Patient Code to link immediately
                OutlinedTextField(
                    value = initialPatientCode,
                    onValueChange = { initialPatientCode = it.uppercase() },
                    label = { Text(StringsProvider.get("label_patient_code_optional", currentLanguage)) },
                    placeholder = { Text("e.g. NER-6842") },
                    leadingIcon = { Icon(Icons.Default.Link, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("input_caregiver_signup_patient_code"),
                    singleLine = true
                )
                Text(
                    text = "You can link or remove multiple patients anytime from Caregiver Settings.",
                    fontSize = 11.sp,
                    color = textPrimary.copy(alpha = 0.6f),
                    modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                AccessibleButton(
                    text = StringsProvider.get("btn_create_caregiver_account", currentLanguage),
                    onClick = {
                        if (name.isBlank() || signUpEmail.isBlank() || signUpPassword.isBlank()) {
                            errorMessage = "Please enter your name, email, and password."
                        } else {
                            onSignUp(
                                name,
                                signUpEmail,
                                signUpPassword,
                                selectedState,
                                initialPatientCode,
                                relationship,
                                {
                                    successMessage = "Account created successfully! Loading your dashboard..."
                                },
                                { err ->
                                    errorMessage = err
                                }
                            )
                        }
                    },
                    isPrimary = true,
                    highContrast = highContrast,
                    fontSizeScale = fontSizeScale,
                    testTag = "btn_caregiver_submit_signup"
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(onClick = { isSignUp = false; errorMessage = null }) {
                        Text(
                            text = "Already have an account? " + StringsProvider.get("tab_signin", currentLanguage),
                            fontWeight = FontWeight.SemiBold,
                            color = if (highContrast) Color(0xFF38BDF8) else Color(0xFF0D5C75)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            AccessibleButton(
                text = StringsProvider.get("btn_quick_demo_caregiver", currentLanguage),
                onClick = onQuickDemo,
                isOutlined = true,
                emoji = "🩺",
                highContrast = highContrast,
                fontSizeScale = fontSizeScale,
                testTag = "btn_caregiver_quick_demo"
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(onClick = { onSwitchToPatient() }) {
                    Text(
                        text = StringsProvider.get("btn_switch_patient", currentLanguage),
                        fontWeight = FontWeight.SemiBold,
                        color = if (highContrast) Color(0xFF38BDF8) else Color(0xFF0D5C75)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            DisclaimerBanner(
                currentLanguage = currentLanguage,
                highContrast = highContrast,
                fontSizeScale = fontSizeScale
            )
        }
    }
}
