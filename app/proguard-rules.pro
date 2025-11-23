# Add project specific ProGuard rules here.
# You can find common rules for libraries on their official websites.

# Keep all model classes (entities) that are used by Room and Firestore
-keep class com.mobiverse.nebula.data.entity.** { *; }

# Keep the names of fields in data classes, as they are used for serialization
-keepnames class com.mobiverse.nebula.data.entity.**

# Firebase and Google Play services
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**

# WorkManager
-keepclassmembers class * extends androidx.work.Worker { 
    public <init>(android.content.Context, androidx.work.WorkerParameters); 
}

# General rule for keeping annotation classes
-keep @interface kotlin.Metadata

# Keep setters and getters for data classes used by Firestore
-keepclassmembers,allowobfuscation class * {
    @com.google.firebase.database.PropertyName <fields>;
}

# Room Database
-keep class androidx.room.** { *; }
-dontwarn androidx.room.**

# AndroidX
-keep class androidx.** { *; }
-dontwarn androidx.**
