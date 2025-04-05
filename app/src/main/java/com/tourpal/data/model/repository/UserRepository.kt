    package com.tourpal.data.model.repository

    import com.google.firebase.auth.FirebaseAuth
    import com.tourpal.data.model.User
    import com.tourpal.services.firestore.FirestoreService

    import java.lang.Exception

    class UserRepository(private val firestoreService: FirestoreService) {

        private val auth: FirebaseAuth = FirebaseAuth.getInstance()

        // Get user data from Firestore
        suspend fun getUser(userId: String): User? {
            return firestoreService.getUser(userId)
        }


//        // Get current user data from Firestore
//        suspend fun getCurrentUser(): User? {
//            val userId = auth.currentUser?.uid
//            return if (userId != null) {
//                getUser(userId)
//            } else {
//                null  // Return null if the user is not authenticated
//            }
//        }

        // Update user data in Firestore
        suspend fun updateUser(user: User): Boolean {
            return try {
                firestoreService.updateUser(user)
                true  // Successfully updated
            } catch (e: Exception) {
                false  // Error occurred while updating
            }
        }

        // Save user data to Firestore (if the user doesn't exist yet)
        suspend fun saveUser(user: User): Boolean {
            return try {
                firestoreService.saveUser(user)
                true  // Successfully saved
            } catch (e: Exception) {
                false  // Error occurred while saving
            }
        }

    }