package ipn.escom.meteora.data.predictions.domain

import ipn.escom.meteora.data.predictions.data.PredictionsRepository
import ipn.escom.meteora.data.predictions.data.network.response.PredictionsResponse

class PredictionsUseCase {
    private val repository = PredictionsRepository()

    suspend operator fun invoke(): PredictionsResponse {
        return repository.getPredictions()
    }
}