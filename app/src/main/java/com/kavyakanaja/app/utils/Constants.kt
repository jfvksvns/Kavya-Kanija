package com.kavyakanaja.app.utils

object Constants {

    // ── API ───────────────────────────────────────────────────────────────
    // 🔑 Your live Render backend URL
    const val BASE_URL = "https://kavya-kanaja-backend.onrender.com/"

    // ── Original modes ────────────────────────────────────────────────────
    const val MODE_FULL       = "full"
    const val MODE_WORD       = "word"

    // ── Group A — new explain modes ───────────────────────────────────────
    const val MODE_EMOTION    = "emotion"
    const val MODE_LESSON     = "lesson"
    const val MODE_SIMPLIFY   = "simplify"
    const val MODE_MODERNIZE  = "modernize"
    const val MODE_VISUALIZE  = "visualize"
    const val MODE_BACKGROUND = "background"

    // ── Languages ─────────────────────────────────────────────────────────
    const val LANG_ENGLISH = "en"
    const val LANG_KANNADA = "kn"

    // ── Story styles ──────────────────────────────────────────────────────
    const val STYLE_SIMPLE   = "simple"
    const val STYLE_DRAMATIC = "dramatic"
    const val STYLE_CHILDREN = "children"

    // ── Generate lengths ──────────────────────────────────────────────────
    const val LENGTH_SHORT  = "short"
    const val LENGTH_MEDIUM = "medium"
    const val LENGTH_LONG   = "long"

    // ── Transliterate directions ──────────────────────────────────────────
    const val DIR_KN_TO_ROMAN  = "kn_to_roman"
    const val DIR_ROMAN_TO_KN  = "roman_to_kn"

    // ── Difficulty levels ─────────────────────────────────────────────────
    const val DIFF_EASY   = "easy"
    const val DIFF_MEDIUM = "medium"
    const val DIFF_HARD   = "hard"

    // ── Navigation routes ─────────────────────────────────────────────────
    const val ROUTE_HOME       = "home"
    const val ROUTE_DETAIL     = "poem_detail/{poemId}/{language}"
    const val ROUTE_FAVORITES  = "favorites/{language}"
    const val ROUTE_HISTORY    = "history/{language}"
    const val ROUTE_POET       = "poet/{poetName}/{language}"
    const val ROUTE_QUIZ       = "quiz/{poemId}/{language}"
    const val ROUTE_GENERATE   = "generate/{language}"
    const val ROUTE_STORY      = "story/{poemId}/{language}"

    // ── DataStore ─────────────────────────────────────────────────────────
    const val PREF_LANGUAGE = "selected_language"

    // ── DB ────────────────────────────────────────────────────────────────
    const val DB_NAME = "kavya_kanaja_db"
}