package ipn.escom.meteora

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import ipn.escom.meteora.data.authentication.login.LoginViewModel
import ipn.escom.meteora.ui.login.Login
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    val loginViewModel = LoginViewModel()

    @Test
    fun loginScreen_displaysCorrectly() {
        composeTestRule.setContent {
            Login(loginViewModel = loginViewModel)
        }

        composeTestRule.onNodeWithTag("usernameField").assertIsDisplayed()
        composeTestRule.onNodeWithTag("passwordField").assertIsDisplayed()
        composeTestRule.onNodeWithTag("loginButton").assertIsDisplayed()
    }

    @Test
    fun loginButton_triggersLoginAction() {
        composeTestRule.setContent {
            Login(loginViewModel = loginViewModel)
        }

        composeTestRule.onNodeWithTag("usernameField").performTextInput("testuser")
        composeTestRule.onNodeWithTag("passwordField").performTextInput("password")
        composeTestRule.onNodeWithTag("loginButton").performClick()

    }
}
