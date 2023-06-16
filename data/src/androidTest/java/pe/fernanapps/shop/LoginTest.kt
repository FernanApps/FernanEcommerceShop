package pe.fernanapps.shop

import androidx.test.filters.SmallTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pe.fernanapps.shop.domain.DataState
import pe.fernanapps.shop.domain.usecases.login.LoginUseCase
import pe.fernanapps.shop.domain.usecases.user.GetUserLocalUseCase
import javax.inject.Inject


@SmallTest
@HiltAndroidTest
@OptIn(ExperimentalCoroutinesApi::class)
class LoginTest {


    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Before
    fun setup() {
        hiltRule.inject()
    }



    @Inject
    lateinit var loginUseCase: LoginUseCase

    @Inject
    lateinit var getUserLocalUse: GetUserLocalUseCase


    private val email = "dev.fernan.apps3@gmail.com"
    private val password = "dev.fernan.apps3@gmail.com"

    @Test
    fun login() = runTest {

        var isLogin = false

        printing("And ::::: ")
        loginUseCase.invoke(email, password).collect {
            printing(
                when (it) {
                    is DataState.Error -> it.exception
                    is DataState.Finished -> "finish"
                    is DataState.Loading -> "loading"
                    is DataState.Progress -> "progress"
                    is DataState.Success -> {
                        isLogin = it.data
                        "data :::: ${it.data}"
                    }

                }
            )
        }
        assertEquals(true, isLogin)

    }

    @Test
    fun getUserFromLocal() = runTest {
        val user = getUserLocalUse.invoke()
        assertNotNull(user)
        printing(user)
        assertEquals(email, user!!.email)

    }


    private fun printing(msg: Any?) {
        kotlin.io.println("\n")
        kotlin.io.println("------------------------------------------------------------------")
        kotlin.io.println(msg)
        kotlin.io.println("\n")
        kotlin.io.println("\n")
    }

}