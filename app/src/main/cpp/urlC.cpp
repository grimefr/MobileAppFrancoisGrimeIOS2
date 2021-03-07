/* C++ Version */

#include <string.h>
#include <jni.h>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_mybankapplication_MainActivity_geturl(JNIEnv *env,jobject thiz)
{
    return env->NewStringUTF("https://6007f1a4309f8b0017ee5022.mockapi.io/api/m1/");
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_mybankapplication_MainActivity_getMasterKEY(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF("ciZPtRvcK1+L3W0Vdv3ib6UqHbqpoA1VpEXdbqVlI9fye9EA4mhbqCuwF/pUK4yLarSGNR3j5SwxlkwTVg7v43yFkVXVvm23RxVkF3piqFhY1jBeJg9x1Vt2oaxoS1xg7t720jxsTSDdiOnQJfkvd9pZmdJkk77DFKQYzw6uvC0=");
}