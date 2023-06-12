package pe.fernanapps.shop

import android.provider.Settings


object Constants {
    /*
    Custom Mask

https://github.com/StephenVinouze/ShapeView
https://github.com/fievx/view_shaper
https://github.com/arefhosseini/android-shape-imageview

     */


    const val PRIVACY_POLICY_URL = "https://github.com/FernanApps"
    const val PROFILE_IMAGE: String = "https://st.depositphotos.com/1008939/1316/i/450/depositphotos_13163725-stock-photo-young-man.jpg"
    const val SHOW_ONBOARDING = "show_onboarding"
    const val ACTION_SHOW_CART_FRAGMENT = "pe.fernanapps.shop.ACTION_SHOW_CART_FRAGMENT"
    const val INTENT_DETAILS_TO_CART_FRAGMENT = "navigateToCart"



    val IMAGES_CATEGORY = "categories_image"

    /**
     * @param First Arg = BucketId
     * @param Second Arg = FileId
     */
    val CATEGORY_FILE_FORMAT =
        "${ConstantsData.getAppWriteEndpoint()}/storage/buckets/%s/files/%s/view?project=${ConstantsData.getAppWriteProyectId()}"



}