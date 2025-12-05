
# Keep all classes in your application's package
-keep class com.mobiverse.** { *; }

# Firebase SDK rules
-keep class com.google.firebase.** { *; }
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn com.google.firebase.**

# Google Play Services rules
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

# Room rules
-keep class androidx.room.RoomDatabase { *; }
-keep class androidx.room.RoomOpenHelper { *; }
-keep class androidx.room.util.TableInfo { *; }
-keepclassmembers class * extends androidx.room.RoomDatabase {
    public *; 
}
-keepclassmembers class * extends androidx.room.Entity {
    public <init>(...);
    public *;
}
-keepclassmembers class * extends androidx.room.Dao {
    *; 
}

# Keep models (data classes)
-keepclassmembers class * extends java.lang.Object {
    @androidx.room.PrimaryKey <fields>;
    @androidx.room.Embedded <fields>;
    @androidx.room.Relation <fields>;
    @androidx.room.ColumnInfo <fields>;
}

# Kotlin-specific rules
-keepattributes Signature
-keepattributes InnerClasses
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
