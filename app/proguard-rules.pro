# Add project specific ProGuard rules here.
<<<<<<< HEAD
# You can find common rules for libraries on their official websites.

# Keep all model classes (entities) that are used by Room and Firestore
-keep class com.mobiverse.nebula.data.entity.** { *; }

# Keep the names of fields in data classes, as they are used for serialization
-keepnames class com.mobiverse.nebula.data.entity.**

# Firebase and Google Play services
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

# WorkManager
-keepclassmembers class * extends androidx.work.Worker { 
    public <init>(android.content.Context, androidx.work.WorkerParameters); 
}

# Timber logging library (it should automatically be removed in release builds)

# General rule for keeping annotation classes
-keep @interface kotlin.Metadata

# Keep setters and getters for data classes used by Firestore
-keepclassmembers,allowobfuscation class * {
    @com.google.firebase.database.PropertyName <fields>;
}

# Keep any classes that are used by reflection (like custom Views, etc.)
# -keep public class com.mypackage.MyClass
=======
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# ===============================
# Z-AI Choice Screen Specific Rules
# ===============================

# Keep all classes in the main package
-keep class com.zai.choicescreen.** { *; }

# Keep BroadcastReceiver classes
-keep class * extends android.content.BroadcastReceiver {
    public <init>(...);
    public void onReceive(android.content.Context, android.content.Intent);
}

# Keep Service classes
-keep class * extends android.app.Service {
    public <init>(...);
    public android.os.IBinder onBind(android.content.Intent);
}

# Keep Activity classes
-keep class * extends androidx.appcompat.app.AppCompatActivity {
    public <init>(...);
}

# Keep Fragment classes
-keep class * extends androidx.fragment.app.Fragment {
    public <init>(...);
}

# Keep Application class
-keep class * extends android.app.Application {
    public <init>(...);
    public void onCreate();
}

# ===============================
# Android Framework Rules
# ===============================

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep enum classes
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep Parcelable implementations
-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# Keep Serializable classes
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Keep View constructors
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# Keep onClick methods
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

# ===============================
# AndroidX and Material Design Rules
# ===============================

# AndroidX
-keep class androidx.** { *; }
-dontwarn androidx.**

# Material Design Components
-keep class com.google.android.material.** { *; }
-dontwarn com.google.android.material.**

# ConstraintLayout
-keep class androidx.constraintlayout.** { *; }
-dontwarn androidx.constraintlayout.**

# Lifecycle components
-keep class androidx.lifecycle.** { *; }
-dontwarn androidx.lifecycle.**

# Navigation components
-keep class androidx.navigation.** { *; }
-dontwarn androidx.navigation.**

# Work Manager
-keep class androidx.work.** { *; }
-dontwarn androidx.work.**

# Room Database
-keep class androidx.room.** { *; }
-dontwarn androidx.room.**

# ===============================
# Third-party Library Rules
# ===============================

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

# ===============================
# Reflection and Annotation Rules
# ===============================

# Keep annotations
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod

# Keep generic signatures
-keepattributes Signature

# Keep source file names and line numbers for better crash reports
-keepattributes SourceFile,LineNumberTable

# ===============================
# Optimization Rules
# ===============================

# Enable optimization
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-dontpreverify

# Remove logging in release builds
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

# Remove debug code
-assumenosideeffects class java.lang.System {
    public static void out.println(...);
    public static void err.println(...);
}

# ===============================
# Security Rules
# ===============================

# Obfuscate class names but keep important ones readable
-keepnames class com.zai.choicescreen.MainActivity
-keepnames class com.zai.choicescreen.ChoiceActionReceiver
-keepnames class com.zai.choicescreen.BootReceiver

# Remove unused resources
-dontwarn **

# ===============================
# Custom Model Classes
# ===============================

# Keep model classes (if any)
-keep class com.zai.choicescreen.model.** { *; }
-keep class com.zai.choicescreen.data.** { *; }

# Keep API response classes
-keep class com.zai.choicescreen.api.** { *; }

# ===============================
# Debugging Rules (Comment out for release)
# ===============================

# Uncomment for debugging ProGuard issues
# -printmapping mapping.txt
# -printseeds seeds.txt
# -printusage usage.txt
# -printconfiguration configuration.txt

# ===============================
# Final Rules
# ===============================

# Don't warn about missing classes
-dontwarn **

# Keep everything in the support library
-keep class android.support.** { *; }
-dontwarn android.support.**

# Keep Google Play Services
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

# Firebase (if used)
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**
>>>>>>> 55bd7b5c92be6666b58530779ed811bdc8a815e6
