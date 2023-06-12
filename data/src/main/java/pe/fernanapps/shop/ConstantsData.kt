package pe.fernanapps.shop



object ConstantsData {
   
   
	private const val nameLibrary = "services"

    init {
        System.loadLibrary(nameLibrary)
    }

    external fun getAppWriteEndpoint(): String
    external fun getAppWriteProyectId(): String
    external fun getAppWriteDatabaseId(): String

    external fun getAppWriteCategoriesId(): String
    external fun getAppWriteProductsId(): String
    external fun getAppWriteOffersId(): String

    external fun getAppWriteChatsId(): String
    external fun getAppWriteCustomersId(): String

    external fun getAppWritePaymentsId(): String
    external fun getAppWriteOrderDetailsId(): String
    external fun getAppWriteOrderItemId(): String
    external fun getAppWriteNotificationsId(): String

    /*
     * Stripe
     */
    external fun getStripeBackendApiUrl(): String
    external fun getStripePublishableKey(): String

    const val SESSION_ID = "sessionId"
    const val USER = "user"

}