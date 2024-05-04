package ipn.escom.meteora.data.predictions.data.network

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import ipn.escom.meteora.data.predictions.data.network.response.DayPrediction
import ipn.escom.meteora.data.predictions.data.network.response.LocalityPrediction
import ipn.escom.meteora.data.predictions.data.network.response.MonthPrediction
import ipn.escom.meteora.data.predictions.data.network.response.Prediction
import ipn.escom.meteora.data.predictions.data.network.response.PredictionsResponse
import ipn.escom.meteora.data.predictions.data.network.response.StringPrediction
import ipn.escom.meteora.data.predictions.data.network.response.YearPrediction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class PredictionsService {

    private val database = Firebase.database.reference


    suspend fun getPredictions(): PredictionsResponse {
        val query = database.child("predictions")
        var predictionsResponse: PredictionsResponse? = null

        return withContext(Dispatchers.IO) {
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val localityPredictions = mutableListOf<LocalityPrediction>()
                    for (localitySnapshot in snapshot.children) {
                        val locality = localitySnapshot.key ?: continue
                        val years = mutableListOf<YearPrediction>()
                        for (yearSnapshot in localitySnapshot.children) {
                            val year = yearSnapshot.key?.toIntOrNull() ?: continue
                            val months = mutableListOf<MonthPrediction>()
                            for (monthSnapshot in yearSnapshot.children) {
                                val month = monthSnapshot.key?.toIntOrNull() ?: continue
                                val days = mutableListOf<DayPrediction>()
                                for (daySnapshot in monthSnapshot.children) {
                                    val day = daySnapshot.key?.toIntOrNull() ?: continue
                                    val stringPrediction =
                                        daySnapshot.getValue(StringPrediction::class.java)
                                            ?: continue
                                    val prediction = Prediction(
                                        maxt = stringPrediction.maxt.toDoubleOrNull() ?: 0.0,
                                        mint = stringPrediction.mint.toDoubleOrNull() ?: 0.0,
                                        rainfall = stringPrediction.rainfall.toDoubleOrNull() ?: 0.0
                                    )
                                    days.add(DayPrediction(day, prediction))
                                }
                                months.add(MonthPrediction(month, days))
                            }
                            years.add(YearPrediction(year, months))
                        }
                        localityPredictions.add(LocalityPrediction(locality, years))
                    }
                    predictionsResponse = PredictionsResponse(localityPredictions)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("PredictionsRepository", "Error getting predictions", error.toException())
                }
            })

            while (predictionsResponse == null) {
                delay(100) // wait until predictionsResponse is set
            }

            predictionsResponse!!
        }


    }
}
