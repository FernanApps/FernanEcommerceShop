
<h1 align="center">Fernan Shop</h1>

<p align="center">
<a href="https://github.com/FernanApps/FernanEcommerceShop" title="Go"><img src="https://img.shields.io/static/v1?label=FernanApps&message=EcommerceShop&color=blue&logo=github" alt="Github - FerMemoryGame"></a>
<br>
<a href="https://github.com/FernanApps/FernanEcommerceShop/releases/latest"><img alt="Release" src="https://img.shields.io/github/v/release/FernanApps/FernanEcommerceShop.svg?include_prereleases=&sort=semver&color=red"/></a>
  <a href="https://android-arsenal.com/api?level=21"><img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat"/></a> 
 
</p
</br>

<p align="center">
<img src="https://raw.githubusercontent.com/FernanApps/FernanEcommerceShop/master/previews/backdrop.png"/>
</p>

## **Introduction**
<p align="center">
<img src="https://raw.githubusercontent.com/FernanApps/FernanEcommerceShop/master/previews/banner.png"/>
</p>

<p align="center">  
🛒👚 FERNAN SHOP: Welcome to our e-commerce application! We present a cutting-edge platform where you can explore and purchase a wide range of fashionable clothing and accessories, all from the comfort of your home. Leveraging the power of Appwrite as our backend solution, we have seamlessly integrated user management, product catalog, and order tracking features. To ensure secure and hassle-free transactions, we have integrated Stripe payment services, providing a robust and reliable payment gateway. Join us today for an immersive shopping experience, where fashion meets convenience at your fingertips. Be part of our journey as we revolutionize the way you shop online!".
</p>


## Description of Project

The project at hand is a mobile application developed for a hackathon event, aiming to revolutionize the fashion e-commerce industry by providing a user-friendly and secure shopping experience with simplified online payments. The application was built using AppWrite, a powerful backend-as-a-service platform, and integrated with Stripe, a leading online payment gateway.

### Features

- **User Authentication:** AppWrite handles user authentication, ensuring secure access to the app's features. Users can create accounts, log in securely, and manage their profiles, guaranteeing a personalized experience.
- **Efficient Data Storage:** AppWrite efficiently stores and manages data, providing a seamless user experience. It optimizes data retrieval and storage, resulting in fast response times and minimal downtime.
- **Seamless Payment Experience:** The integration of Stripe allows users to make payments securely and without hassle. Users can add their preferred payment methods, and the app securely processes transactions, providing peace of mind.
- **Product Exploration:** Users can explore a vast selection of fashion items, including the latest trends, popular styles, and exclusive offers. The app provides intuitive search and filtering functionalities to help users discover their desired products easily.
- **Shopping Cart Functionality:** Users can add their favorite items to the cart and proceed to checkout seamlessly. The cart retains the selected items, allowing users to review and modify their choices before making a final purchase decision.
- **Secure Transactions:** The custom backend implementation ensures that payments are processed securely without compromising users' financial data. Encryption techniques and best practices are employed to protect sensitive information, maintaining the highest security standards.
- **Notifications:** Users receive timely notifications about their orders, including order confirmations, shipment updates, and delivery notifications. These notifications keep users informed and engaged throughout the shopping experience.
- **Customer Support:** The app provides a contact feature, allowing users to reach out to support for any inquiries, assistance, or feedback. The dedicated support team ensures a prompt and helpful response, enhancing customer satisfaction.

The project was developed solely by me within a two-week timeframe, showcasing my ability to rapidly develop and deploy a mobile application using AppWrite and Stripe. The backend server is hosted on Glitch, a cloud-based development platform, and additional functionalities were implemented to handle card payments through Stripe. The app's front-end design focuses on simplicity, elegance, and intuitive navigation, providing a delightful user interface.

The application's architecture enables smooth communication between the app and the payment platform, ensuring a seamless user experience and enhanced security. The integration with AppWrite and Stripe offers scalability, allowing the app to handle increased user demand and adapt to future business growth.

I implemented extensive testing and quality assurance measures to ensure the app's stability, reliability, and overall performance. Regular updates and maintenance are planned to introduce new features, address user feedback, and improve the app's functionality.

This project showcases my skills in developing a secure, efficient, and user-friendly mobile commerce application. It emphasizes the importance of a seamless payment experience and demonstrates how technology can elevate the e-commerce industry, providing a platform that both businesses and customers can trust.

By combining powerful backend services, secure payment processing, and an intuitive user interface, this mobile application sets a new standard for online fashion shopping. It offers convenience, security, and an enjoyable shopping experience, empowering users to explore the latest fashion trends and make purchases with confidence.

## Architecture

![Architecture](https://miro.medium.com/v2/resize:fit:750/format:webp/1*VhRdBj1kXY3fwXDEDxoykg.png)

### Structure
This architecture allows for a clear separation of responsibilities, makes it easier to test and maintain code, and allows layers to be independent and can be modified or replaced without affecting other layers.

*This architecture is separated in modules*
<p align="center">
  <img src="https://raw.githubusercontent.com/FernanApps/FernanEcommerceShop/master/previews/structure_main.png" alt="">
</p>


**Modules**


- **Presentation** is responsible for the user interface and user interaction. Here are the UI components and presentation logic. <br>  <br>  
![Imagen](https://raw.githubusercontent.com/FernanApps/FernanEcommerceShop/master/previews/structure_app.png) <br>
<br>  
-  **Data**   is responsible for data access and persistence. Here the repositories defined in the domain layer are implemented and frameworks and libraries are used to interact with data sources. <br> <br>  ![Imagen](https://raw.githubusercontent.com/FernanApps/FernanEcommerceShop/master/previews/structure_data.png) 
<br>

- **Domain**  is the core of the architecture and contains the main business logic of the application. Here the application-specific use cases and business rules are defined. <br><br>![Imagen](https://raw.githubusercontent.com/FernanApps/FernanEcommerceShop/master/previews/structure_domain.png)


## Build your own app?

To build your own app is super simple

#### AppWrite
Install appwrite on your own server, it can guide you from [Build your server](https://github.com/appwrite/appwrite) or you can also use [Cloud Appwrite](https://cloud.appwrite.io/console/)

1. Create a new project, and open
2. You go to integrations and add a new platform (Android App), you add the name and the name of the application package, you don't need to add the dependencies because it already has them
3. Create one API Key
4. To facilitate the creation of the database with its collections, I have created a script in Kotlin [CreateDatabase.kt](https://github.com/FernanApps/FernanEcommerceShop/blob/master/scripts/CreateDatabase.kt#L6-L105), you can run it in an Online Kotlin Compiler, Android Studio, etc, it does not need any dependencies, extra, remember to configure your keys in the script.


#### Backend for Stripe
This backend is for example only
1. Remix project of glitch [Backend Example Fernan Shop](https://glitch.com/~fernan-apps-shop-stripe)
2. Config .env file
```
	STRIPE_TEST_SECRET_KEY= YOUR KEY OF STRIPE
	IN_publishable_key= YOUR KEY OF STRIPE
```

#### Android App
1. Visit the Code Repository and clone
2. Open Android Studio
3. Config your keys in *[/data/src/main/jni/services.cpp](https://github.com/FernanApps/FernanEcommerceShop/blob/master/data/src/main/jni/services.cpp#L15-L22)*
4. Compile the application and bam **the magic was done**

#### Extra
- If you want to test with test cards you can use [Test Cards](https://stripe.com/docs/testing#cards)

---
*If you have any questions, write to me and I will gladly help you*

---

## Screens
#### Splash
<div style="display: flex;">
  <img src="https://raw.githubusercontent.com/FernanApps/FernanEcommerceShop/master/previews/splash_1.png" alt="" width="200">
</div>

#### Onboarding
<div style="display: flex;">
  <img src="https://raw.githubusercontent.com/FernanApps/FernanEcommerceShop/master/previews/onboarding_1.png" alt="" width="200">
  <img src="https://raw.githubusercontent.com/FernanApps/FernanEcommerceShop/master/previews/onboarding_2.png" alt="" width="200">
</div>

#### Login, Signup
<div style="display: flex;">
  <img src="https://raw.githubusercontent.com/FernanApps/FernanEcommerceShop/master/previews/login_or_signup_1.png" alt="" width="200">
  <img src="https://raw.githubusercontent.com/FernanApps/FernanEcommerceShop/master/previews/login_or_signup_2.png" alt="" width="200">
</div>

#### Main
<div style="display: flex;">
  <img src="https://raw.githubusercontent.com/FernanApps/FernanEcommerceShop/master/previews/main_1.png" alt="" width="200">
  <img src="https://raw.githubusercontent.com/FernanApps/FernanEcommerceShop/master/previews/main_2.png" alt="" width="200">
  <img src="https://raw.githubusercontent.com/FernanApps/FernanEcommerceShop/master/previews/main_3.png" alt="" width="200">
 
</div>

#### Cart, Payments
<div style="display: flex;">
  <img src="https://raw.githubusercontent.com/FernanApps/FernanEcommerceShop/master/previews/cart_payment_1.png" alt="" width="200">
  <img src="https://raw.githubusercontent.com/FernanApps/FernanEcommerceShop/master/previews/cart_payment_2.png" alt="" width="200">
</div>

#### Notifications
<div style="display: flex;">
  <img src="https://raw.githubusercontent.com/FernanApps/FernanEcommerceShop/master/previews/notifications_1.png" alt="" width="200">
</div>

#### Orders
<div style="display: flex;">
  <img src="https://raw.githubusercontent.com/FernanApps/FernanEcommerceShop/master/previews/orders_1.png" alt="" width="200">
</div>

#### Profile
<div style="display: flex;">
  <img src="https://raw.githubusercontent.com/FernanApps/FernanEcommerceShop/master/previews/profile_1.png" alt="" width="200">
</div>

#### Support, Chats
<div style="display: flex;">
  <img src="https://raw.githubusercontent.com/FernanApps/FernanEcommerceShop/master/previews/chat.png" alt="" width="200">
</div>

## Tech Stack
The Fernan Shop App utilizes the following technologies:
- Minimum SDK level 21  
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) based for asynchronous.  
- Jetpack  
  - Lifecycle - Observe Android lifecycles and handle UI states upon the lifecycle changes.  
  - ViewModel - Manages UI-related data holder and lifecycle aware. Allows data to survive configuration changes such as screen rotations.  
  - DataBinding - Binds UI components in your layouts to data sources in your app using a declarative format rather than programmatically.  
- Architecture  
  - MVVM Architecture (View - DataBinding - ViewModel - Model)  
- [Retrofit2](https://github.com/square/retrofit) - Construct the REST APIs.  
- [Glide](https://github.com/bumptech/glide) - Loading images from network.  
- [Material-Components](https://github.com/material-components/material-components-android) - Material design components for building ripple animation, and CardView.  
- [**Stripe**](https://dashboard.stripe.com) - _Stripe_ is a suite of APIs powering online payment processing and commerce solutions for internet businesses of all sizes. Accept payments and scale faster..

- [**Backend for Stripe**](https://glitch.com/~fernan-apps-shop-stripe) - For testing the Stripe mobile apps.


- [**Appwrite Cloud**](https://cloud.appwrite.io/) **:** Unizim relies on Appwrite as its backend service.
    - [**Authentication**](https://appwrite.io/docs/authentication)          
    - [Databases](https://appwrite.io/docs/client/databases)            
        - [Realtime](https://appwrite.io/docs/realtime)  
    - [Storage](https://appwrite.io/docs/client/storage)      
    - [Avatars](https://appwrite.io/docs/client/avatars)     




## Download
Go to the [Releases](https://github.com/FernanApps/FernanEcommerceShop/releases) to download the latest APK.

## Preview

<img src="https://raw.githubusercontent.com/FernanApps/FernanEcommerceShop/master/previews/preview.gif" height="320" />

<!---
<img src="/previews/preview.gif" width="320"/>
-->

## Tech stack & Open-source libraries
- Minimum SDK level 21
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) based for asynchronous.
- Jetpack
  - Lifecycle - Observe Android lifecycles and handle UI states upon the lifecycle changes.
  - ViewModel - Manages UI-related data holder and lifecycle aware. Allows data to survive configuration changes such as screen rotations.
  - DataBinding - Binds UI components in your layouts to data sources in your app using a declarative format rather than programmatically.
- Architecture
  - MVVM Architecture (View - DataBinding - ViewModel - Model)
- [Retrofit2](https://github.com/square/retrofit) - Construct the REST APIs.
- [Glide](https://github.com/bumptech/glide) - Loading images from network.
- [Material-Components](https://github.com/material-components/material-components-android) - Material design components for building ripple animation, and CardView.
- [Stripe](https://dashboard.stripe.com) - _Stripe_ is a suite of APIs powering online payment processing and commerce solutions for internet businesses of all sizes. Accept payments and scale faster..


## Find this repository useful? :heart:
Also, __[follow me](https://github.com/FernanApps)__ on GitHub for my next creations! 🤩

## Inspired by :heart:
Also, __[follow](https://www.behance.net/gallery/136187429/Rika-eCommerce-Mobile-App)__ 😅

# License
```xml
- The code is: `-
```
