#include <jni.h>
#include <string>


/**
    * Note: only necessary if not configured via `gradle.properties`.
    *
    * Set to the base URL of your test backend. If you are using
    * [example-mobile-backend](https://github.com/stripe/example-mobile-backend),
    * the URL will be something like `https://stripe-example-mobile-backend.glitch.me/`.
    */
// https://example-terminal-backend-nwaa.onrender.com/"
// https://fernan-apps-shop-stripe.glitch.me/

std::string STRIPE_BACKEND_API_URL = "https://fernan-apps-shop-stripe.glitch.me/";
std::string STRIPE_PUBLISHABLE_KEY = "pk_test_51NF3BNK3fu4EtKg5VeROdo0KAnWc7d2C2keU3e3Pe09TjtYJySsBDP5JqaVpzNkFqaYfiaOYbM2iST7kEHbuoRzU00zV1XPCqm";

std::string APPWRITE_ENDPOINT_BASE = "https://8080-appwrite-integrationfor-b87w21ace9c.ws-us99.gitpod.io";

//std::string APPWRITE_ENDPOINT = APPWRITE_ENDPOINT_BASE +"/v1";
//std::string APPWRITE_PROYECT = "app_write_hackaton";
//std::string APPWRITE_DATABASE_ID = "fernan_shop_db";


std::string APPWRITE_ENDPOINT = "https://cloud.appwrite.io/v1";
std::string APPWRITE_PROYECT = "646f7da48de53f64a18a";
std::string APPWRITE_DATABASE_ID = "64739b907d3c97bfbc76";

std::string APPWRITE_CATEGORIE_ID = "categories";
std::string APPWRITE_PRODUCTS_ID = "products2";
std::string APPWRITE_OFFERS_ID = "offers";
std::string APPWRITE_CHATS_ID = "chats";

std::string APPWRITE_PAYMENTS_ID = "payments";
std::string APPWRITE_ORDER_DETAILS_ID = "order_details";
std::string APPWRITE_ORDER_ITEM_ID = "order_item";

std::string APPWRITE_CUSTOMERS_ID = "customers";

std::string APPWRITE_NOTIFICATIONS_ID = "notifications";


extern "C" JNIEXPORT jstring JNICALL
Java_pe_fernanapps_shop_ConstantsData_getAppWriteEndpoint(JNIEnv *env, jobject clazz) {
   return env->NewStringUTF(APPWRITE_ENDPOINT.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_pe_fernanapps_shop_ConstantsData_getAppWriteProyectId(JNIEnv *env, jobject clazz) {
    return env->NewStringUTF(APPWRITE_PROYECT.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_pe_fernanapps_shop_ConstantsData_getAppWriteDatabaseId(JNIEnv *env, jobject clazz) {
    return env->NewStringUTF(APPWRITE_DATABASE_ID.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_pe_fernanapps_shop_ConstantsData_getAppWriteCategoriesId(JNIEnv *env, jobject clazz) {
    return env->NewStringUTF(APPWRITE_CATEGORIE_ID.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_pe_fernanapps_shop_ConstantsData_getAppWriteProductsId(JNIEnv *env, jobject clazz) {
    return env->NewStringUTF(APPWRITE_PRODUCTS_ID.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_pe_fernanapps_shop_ConstantsData_getAppWriteOffersId(JNIEnv *env, jobject clazz) {
    return env->NewStringUTF(APPWRITE_OFFERS_ID.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_pe_fernanapps_shop_ConstantsData_getAppWriteChatsId(JNIEnv *env, jobject clazz) {
    return env->NewStringUTF(APPWRITE_CHATS_ID.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_pe_fernanapps_shop_ConstantsData_getAppWriteCustomersId(JNIEnv *env, jobject clazz) {
    return env->NewStringUTF(APPWRITE_CUSTOMERS_ID.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_pe_fernanapps_shop_ConstantsData_getAppWritePaymentsId(JNIEnv *env, jobject clazz) {
    return env->NewStringUTF(APPWRITE_PAYMENTS_ID.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_pe_fernanapps_shop_ConstantsData_getAppWriteOrderDetailsId(JNIEnv *env, jobject clazz) {
    return env->NewStringUTF(APPWRITE_ORDER_DETAILS_ID.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_pe_fernanapps_shop_ConstantsData_getAppWriteOrderItemId(JNIEnv *env, jobject clazz) {
    return env->NewStringUTF(APPWRITE_ORDER_ITEM_ID.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_pe_fernanapps_shop_ConstantsData_getAppWriteNotificationsId(JNIEnv *env, jobject clazz) {
    return env->NewStringUTF(APPWRITE_NOTIFICATIONS_ID.c_str());
}


extern "C"
JNIEXPORT jstring JNICALL
Java_pe_fernanapps_shop_ConstantsData_getStripePublishableKey(JNIEnv *env, jobject clazz) {
    return env->NewStringUTF(STRIPE_PUBLISHABLE_KEY.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_pe_fernanapps_shop_ConstantsData_getStripeBackendApiUrl(JNIEnv *env, jobject clazz) {
    return env->NewStringUTF(STRIPE_BACKEND_API_URL.c_str());
}


