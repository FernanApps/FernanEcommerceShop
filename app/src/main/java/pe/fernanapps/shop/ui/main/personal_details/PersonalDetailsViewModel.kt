package pe.fernanapps.shop.ui.main.personal_details

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pe.fernanapps.shop.domain.usecases.user.SaveUserRemoteUseCase
import javax.inject.Inject

@HiltViewModel
class PersonalDetailsViewModel @Inject constructor(
    saveUserRemoteUseCase: SaveUserRemoteUseCase
): ViewModel() {

}