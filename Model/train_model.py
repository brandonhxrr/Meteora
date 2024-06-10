from google.cloud import bigquery
from google.oauth2 import service_account
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import mean_squared_error, mean_absolute_error, r2_score
import os
from mix_predictions import mix_predictions
from combine import combine
from export_json import export_json
from firebase_upload import upload_to_firebase

if not os.path.exists("predictions"):
    os.makedirs("predictions")

if not os.path.exists("images"):
    os.makedirs("images")

if not os.path.exists("data"):
    os.makedirs("data")

project_id = "meteora-402122"
dataset_id = "conagua.weather"

json_credentials_path = 'meteora-402122-173393f4eca2.json'
credentials = service_account.Credentials.from_service_account_file(json_credentials_path)
client = bigquery.Client(credentials=credentials, project=credentials.project_id)

alcaldias = ["ALVARO OBREGON", "AZCAPOTZALCO", "BENITO JUAREZ", "COYOACAN", "CUAJIMALPA DE MORELOS", 
             "CUAUHTEMOC", "GUSTAVO A MADERO", "IZTACALCO", "IZTAPALAPA", "LA MAGDALENA CONTRERAS", 
             "MIGUEL HIDALGO", "MILPA ALTA", "TLAHUAC", "TLALPAN", "VENUSTIANO CARRANZA", "XOCHIMILCO"]

titles = {"DAY": "Días", "MONTH": "Meses", "YEAR": "Años", "MAXT": "Temperatura máxima", 
          "MINT": "Temperatura mínima", "RAINFALL": "Nivel de lluvia"}

units = {"YEAR": "", "MAXT" : "°C", "MINT" : "°C", "RAINFALL": "mm", "DAY": "", "MONTH": ""}

parameters = ["MAXT", "MINT", "RAINFALL"]


results = []

def clean_date_column(df):
    df['DAY'] = pd.to_numeric(df['DAY'], errors='coerce')
    df['MONTH'] = pd.to_numeric(df['MONTH'], errors='coerce')
    df['YEAR'] = pd.to_numeric(df['YEAR'], errors='coerce')
    df = df.dropna(subset=['DAY', 'MONTH', 'YEAR'])
    df['DAY'] = df['DAY'].astype(int)
    df['MONTH'] = df['MONTH'].astype(int)
    df['YEAR'] = df['YEAR'].astype(int)
    return df

for parameter in parameters:

    for alcaldia in alcaldias:
        query = f"""
        SELECT DAY, YEAR, MONTH, LOCALITY, {parameter}
        FROM `{project_id}.{dataset_id}`
        WHERE {parameter} IS NOT NULL AND LOCALITY = '{alcaldia}'
        """
        query_job = client.query(query)
        df = query_job.to_dataframe()

        df = clean_date_column(df)
        try:
            df['DATE'] = pd.to_datetime(df[['YEAR', 'MONTH', 'DAY']], errors='coerce')
            df = df.dropna(subset=['DATE'])
        except Exception as e:
            print(f"Error al convertir fechas para {alcaldia}: {e}")
            invalid_dates = df[df[['YEAR', 'MONTH', 'DAY']].apply(lambda x: any(x.isnull()), axis=1)]
            invalid_dates.to_csv(f'data/invalid_dates_{alcaldia}.csv', index=False)
            print(f"Datos inválidos guardados para {alcaldia} en 'data/invalid_dates_{alcaldia}.csv'")

        df = df.sort_values(by='DATE')
        df.dropna(subset=[parameter], inplace=True)

        print(f"Modelo de {titles[parameter]} en {alcaldia}\n")
        X = df[['DAY', 'MONTH', 'YEAR']].values
        y = df[parameter].values.reshape(-1, 1)

        scaler_X = StandardScaler()
        scaler_y = StandardScaler()
        X = scaler_X.fit_transform(X)
        y = scaler_y.fit_transform(y)
        X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=0)

        random_forest_model = RandomForestRegressor()
        random_forest_model.fit(X_train, y_train.ravel())

        y_pred = random_forest_model.predict(X_test)
        mse = mean_squared_error(y_test, y_pred)
        mae = mean_absolute_error(y_test, y_pred)
        r2 = r2_score(y_test, y_pred)

        print(f"Resultados para {alcaldia}:")
        print(f"MSE: {mse}, MAE: {mae}, R2: {r2}")

        results.append({
            "Alcaldía": alcaldia,
            "MSE": mse,
            "MAE": mae,
            "R2": r2
        })

        future_dates = pd.date_range(start='2024-01-01', end='2024-12-31', freq='D')
        future_data = pd.DataFrame({'DAY': future_dates.day, 'MONTH': future_dates.month, 'YEAR': future_dates.year})
        future_data_scaled = scaler_X.transform(future_data[['DAY', 'MONTH', 'YEAR']].values)
        future_predictions = random_forest_model.predict(future_data_scaled)
        future_predictions = scaler_y.inverse_transform(future_predictions.reshape(-1, 1)).flatten()

        plt.figure(figsize=(12, 6))
        plt.plot(future_dates, future_predictions)
        plt.title(f'Predicciones de {titles[parameter]} en {alcaldia}')
        plt.xlabel('Fecha')
        plt.ylabel(f'{titles[parameter]} ({units[parameter]})' if units[parameter] else titles[parameter])
        plt.xticks(rotation=45)
        plt.tight_layout()
        plt.savefig(f"images/model_predictions_{parameter}_{alcaldia}.png")
        #plt.show()

        predictions_df = pd.DataFrame({'DATE': future_dates, 'PREDICTION': future_predictions})
        predictions_df.to_csv(f'predictions/predictions_{parameter}_{alcaldia}.csv', index=False)

    results_df = pd.DataFrame(results)

    print("\nTabla de Resultados:")
    print(results_df)

    results_df.to_csv('data/model_evaluation_results.csv', index=False)
    print("Tabla de resultados guardada en 'data/model_evaluation_results.csv'")

    mix_predictions(parameter)

combine()
export_json()
upload_to_firebase()
