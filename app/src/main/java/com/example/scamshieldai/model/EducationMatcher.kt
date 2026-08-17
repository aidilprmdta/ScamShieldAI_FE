package com.example.scamshieldai.model

/** Memetakan kategori hasil analisis ke konten edukasi lokal. */
object EducationMatcher {

    fun findByCategory(
        category: String?,
        contents: List<EducationContent>,
        fallbackToFeatured: Boolean = true,
    ): EducationContent? {
        val featured = contents.firstOrNull { it.id == "0" } ?: contents.firstOrNull()
        val raw = category?.trim().orEmpty()
        if (raw.isEmpty()) {
            return if (fallbackToFeatured) featured else null
        }

        contents.firstOrNull { it.category.equals(raw, ignoreCase = true) }?.let { return it }
        contents.firstOrNull { it.title.contains(raw, ignoreCase = true) }?.let { return it }

        val key = raw.lowercase()
        val matched = when {
            "phish" in key || "tautan" in key || "link" in key ->
                contents.firstOrNull { it.category.equals("Phishing", ignoreCase = true) }
            "rekayasa" in key || "sosial" in key || "mama" in key || "transfer" in key ->
                contents.firstOrNull { it.category.equals("Rekayasa Sosial", ignoreCase = true) }
            "qris" in key || "quish" in key || "qr" in key ->
                contents.firstOrNull { it.category.equals("QRIS Palsu", ignoreCase = true) }
            "investasi" in key || "bodong" in key || "undian" in key || "loker" in key ->
                featured
            "hukum" in key ->
                contents.firstOrNull { it.category.equals("Hukum", ignoreCase = true) }
            "keamanan" in key || "malware" in key ->
                contents.firstOrNull { it.category.equals("Keamanan", ignoreCase = true) }
            else -> null
        }

        return matched ?: if (fallbackToFeatured) featured else null
    }
}
