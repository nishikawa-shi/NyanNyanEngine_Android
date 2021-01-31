//
// Created by Tetsuya Nishikawa on 8/2/20.
//

#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_ntetz_android_nyannyanengine_1android_model_config_TwitterConfig_getApiSecretFromJniDev(
        JNIEnv *env,
        jobject /* this */) {
    std::string value = "jkl456MNO";
    return env->NewStringUTF(value.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_ntetz_android_nyannyanengine_1android_model_config_TwitterConfig_getConsumerKeyFromJniDev(
        JNIEnv *env,
        jobject /* this */) {
    std::string value = "abcde123GHI";
    return env->NewStringUTF(value.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_ntetz_android_nyannyanengine_1android_model_config_TwitterConfig_getApiSecretFromJniPrd(
        JNIEnv *env,
        jobject /* this */) {
    std::string value = "jkl456MNO";
    return env->NewStringUTF(value.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_ntetz_android_nyannyanengine_1android_model_config_TwitterConfig_getConsumerKeyFromJniPrd(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "abcde123GHI";
    return env->NewStringUTF(hello.c_str());
}
