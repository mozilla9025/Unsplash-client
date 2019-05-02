-keep class com.google.android.material.** { *; }
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**
-dontwarn androidx.**
-keep class androidx.** { *; }
-keep interface androidx.** { *; }
#For design support library
-keep class android.support.design.widget.** { *; }
-keep interface android.support.design.widget.** { *; }

-keepattributes Signature
-keepattributes Annotation
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**
-keeppackagenames org.jsoup.nodes

-dontwarn retrofit2.Platform$Java8

