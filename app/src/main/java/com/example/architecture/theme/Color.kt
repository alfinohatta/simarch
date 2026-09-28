package com.example.architecture.theme

import androidx.compose.ui.graphics.Color

// ============================================================================
// Core Architecture Functional Colors (Standardized Flow Mapping)
// ============================================================================
val PowerFlowColor = Color(0xFFF97316)       // Vibrant Orange — Power Rails (+12V/+5V/+3.3V)
val DataFlowColor = Color(0xFF0D9488)        // Deep Teal — Memory Data Bus & DMA Transfers
val ControlFlowColor = Color(0xFF2563EB)     // Royal Blue — Control Signals & IRQ Interrupts
val ActiveComponent = Color(0xFF10B981)      // Emerald Green — Active Component Glow & Status
val InactiveComponent = Color(0xFF94A3B8)    // Slate Gray — Dormant Component State

// ============================================================================
// Light Theme Palette (Clean, Professional, Commercial Standard)
// ============================================================================
val LightAppBackground = Color(0xFFF8FAFC)   // Slate 50 — Crisp Off-White Background
val LightSurfaceColor = Color(0xFFFFFFFF)    // Pure White — Elevated Cards & Surfaces
val LightSurfaceTonal = Color(0xFFF1F5F9)    // Slate 100 — Tonal Sub-Cards
val LightTextPrimary = Color(0xFF0F172A)     // Slate 900 — Deep Dark Primary Text
val LightTextSecondary = Color(0xFF475569)   // Slate 600 — Balanced Subtitles & Meta
val LightBorderColor = Color(0xFFE2E8F0)     // Slate 200 — Subtle Card Borders

// ============================================================================
// Dark Theme Palette (Premium Navy & Deep Slate)
// ============================================================================
val DarkAppBackground = Color(0xFF0F172A)    // Slate 900 — Deep Navy Background
val DarkSurfaceColor = Color(0xFF1E293B)     // Slate 800 — Tonal Card Surfaces
val DarkSurfaceTonal = Color(0xFF334155)     // Slate 700 — Tonal Sub-Cards
val DarkTextPrimary = Color(0xFFF8FAFC)      // Slate 50 — Bright White Primary Text
val DarkTextSecondary = Color(0xFF94A3B8)    // Slate 400 — Muted Subtitles
val DarkBorderColor = Color(0xFF334155)      // Slate 700 — Subtle Borders

// Default Standard Aliases
val AppBackground = LightAppBackground
val SurfaceColor = LightSurfaceColor
val TextPrimary = LightTextPrimary
val TextSecondary = LightTextSecondary
