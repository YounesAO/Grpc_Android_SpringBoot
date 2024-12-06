package ma.ensa.bankgrpc.modelview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ma.ensa.bankgrpc.stubs.*


class CompteViewModel : ViewModel() {
    private val channel = ManagedChannelBuilder.forAddress("192.168.86.1", 9090)
        .usePlaintext()
        .build()

    private val stub = CompteServiceGrpc.newBlockingStub(channel)

    private val _comptes = MutableLiveData<List<Compte>>()
    val comptes: LiveData<List<Compte>> = _comptes

    private val _stats = MutableLiveData<SoldeStats>()
    val stats: LiveData<SoldeStats> = _stats

    fun loadComptes() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val request = GetAllComptesRequest.getDefaultInstance()
                val response = stub.allComptes(request)
                _comptes.postValue(response.comptesList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadStats() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val request = GetTotalSoldeRequest.getDefaultInstance()
                val response = stub.totalSolde(request)
                _stats.postValue(response.stats)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun saveCompte(solde: Float, dateCreation: String, type: TypeCompte) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val compteRequest = CompteRequest.newBuilder()
                    .setSolde(solde)
                    .setDateCreation(dateCreation)
                    .setType(type)
                    .build()

                val request = SaveCompteRequest.newBuilder()
                    .setCompte(compteRequest)
                    .build()

                stub.saveCompte(request)
                loadComptes()
                loadStats()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        channel.shutdown()
    }
}
