package ipn.escom.meteora.data.predictions.data

import ipn.escom.meteora.data.predictions.data.network.PredictionsService

class PredictionsRepository {

    private val service = PredictionsService()

    suspend fun getPredictions() = service.getPredictions()
}
