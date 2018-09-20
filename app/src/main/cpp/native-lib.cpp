#include <jni.h>
#include <string>
using namespace std;

extern "C"
jstring
Java_com_ignidev_nik_JsonFetcher_ui_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
//todo: check abilities of native

