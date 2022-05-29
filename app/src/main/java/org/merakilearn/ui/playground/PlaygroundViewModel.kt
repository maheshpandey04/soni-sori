package org.merakilearn.ui.playground

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.merakilearn.R
import org.merakilearn.datasource.PlaygroundRepo
import org.merakilearn.datasource.model.PlaygroundItemModel
import org.merakilearn.datasource.model.PlaygroundTypes
import org.navgurukul.commonui.platform.BaseViewModel
import org.navgurukul.commonui.platform.ViewEvents
import org.navgurukul.commonui.platform.ViewModelAction
import org.navgurukul.commonui.platform.ViewState
import org.navgurukul.playground.repo.PythonRepository
import java.io.File

class PlaygroundViewModel(
    private val repository: PlaygroundRepo,
    private val pythonRepository: PythonRepository,
) :
    BaseViewModel<PlaygroundViewEvents, PlaygroundViewState>(PlaygroundViewState()) {

    private var currentQuery: String? =null
    private lateinit var playgroundsList: MutableList<PlaygroundItemModel>

    init {
        viewModelScope.launch {
            setList()
        }
    }

    fun handle(action: PlaygroundActions){
        when(action){
            is PlaygroundActions.Query -> {
                currentQuery = action.query
                filterList()
            }
        }
    }

    private fun filterList() {
        val list = playgroundsList
        viewModelScope.launch {
            val filterList=list.filter {
                val filterQuery= currentQuery?.let{ currentQuery ->
                    if(currentQuery.isNotEmpty()){
                        val wordsToCompare = (it.name).split(" ") + it.file.name.replaceAfterLast("_", "").removeSuffix("_").split(" ")
                        wordsToCompare.find { word ->
                            word.startsWith(
                                currentQuery,
                                true
                            )
                        }!=null
                    } else {
                        true
                    }
                } ?: true

                return@filter filterQuery
            }
            updateState(filterList)
        }
    }

    private fun updateState(list: List<PlaygroundItemModel>) {
        setState {
            copy(playgroundsList=list)
        }
    }

    private suspend fun setList() {
        playgroundsList= repository.getAllPlaygrounds().toMutableList()
        val savedFiles= pythonRepository.fetchSavedFiles()

        for(file in savedFiles){
            playgroundsList.add(PlaygroundItemModel(PlaygroundTypes.PYTHON_FILE, name = "",file= file, iconResource = R.drawable.ic_saved_file))
        }

       updateState(playgroundsList)
    }

    fun selectPlayground(playgroundItemModel: PlaygroundItemModel) {
        when (playgroundItemModel.type) {
            PlaygroundTypes.TYPING_APP -> _viewEvents.setValue(PlaygroundViewEvents.OpenTypingApp)
            PlaygroundTypes.PYTHON -> _viewEvents.postValue(PlaygroundViewEvents.OpenPythonPlayground)
            PlaygroundTypes.PYTHON_FILE -> _viewEvents.setValue(PlaygroundViewEvents.OpenPythonPlaygroundWithFile(playgroundItemModel.file))
        }
    }
}

sealed class PlaygroundViewEvents : ViewEvents {
    object OpenTypingApp : PlaygroundViewEvents()
    object OpenPythonPlayground : PlaygroundViewEvents()
    class OpenPythonPlaygroundWithFile(val file: File) : PlaygroundViewEvents()
}

sealed class PlaygroundActions: ViewModelAction{
    data class Query(val query: String?):PlaygroundActions()
}

data class PlaygroundViewState(
    val playgroundsList: List<PlaygroundItemModel> = arrayListOf()
) : ViewState
