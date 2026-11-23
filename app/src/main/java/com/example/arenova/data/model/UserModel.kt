package com.example.arenova.data.model

data class UserModel(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val role: String = "member",
    val arenaPoints: Int = 250,
    val totalMatches: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toMap(): Map<String, Any> = mapOf(
        "uid" to uid,
        "fullName" to fullName,
        "email" to email,
        "role" to role,
        "arenaPoints" to arenaPoints,
        "totalMatches" to totalMatches,
        "createdAt" to createdAt
    )

    companion object {
        fun fromMap(map: Map<String, Any?>): UserModel {
            return UserModel(
                uid = map["uid"] as? String ?: "",
                fullName = map["fullName"] as? String ?: "",
                email = map["email"] as? String ?: "",
                role = map["role"] as? String ?: "member",
                arenaPoints = (map["arenaPoints"] as? Number)?.toInt() ?: 250,
                totalMatches = (map["totalMatches"] as? Number)?.toInt() ?: 0,
                createdAt = (map["createdAt"] as? Number)?.toLong() ?: System.currentTimeMillis()
            )
        }
    }
}
