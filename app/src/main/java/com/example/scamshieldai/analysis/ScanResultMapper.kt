package com.example.scamshieldai.analysis

import com.example.scamshieldai.ui.screens.RiskLevel
import com.example.scamshieldai.ui.screens.ScanResult
import com.example.scamshieldai.network.AnalysisResult as ApiAnalysisResult

fun mapApiResultToScanResult(result: ApiAnalysisResult): ScanResult {
    val level = when (result.riskLevel.lowercase()) {
        "high" -> RiskLevel.HIGH
        "medium" -> RiskLevel.MEDIUM
        else -> RiskLevel.LOW
    }
    val flags = result.redFlags?.map { it.label } ?: emptyList()
    return ScanResult(
        type = result.type,
        riskScore = result.riskScore,
        riskLevel = level,
        inputSummary = result.inputSummary,
        flags = flags,
        explanation = result.explanation,
        recommendation = result.recommendationText ?: result.recommendation,
        relatedArticle = result.relatedEducationCategory
    )
}
