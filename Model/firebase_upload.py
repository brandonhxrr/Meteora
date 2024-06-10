import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
import json

def upload_to_firebase():
    credentials_url = input("Ingrese la URL de las credenciales de Firebase: ")
    cred = credentials.Certificate(credentials_url)

    database_url = input("Ingrese la URL de la base de datos de Firebase: ")

    firebase_admin.initialize_app(cred, {
        'databaseURL': database_url
    })

    with open('final_predictions.json', 'r') as file:
        json_data = json.load(file)

    ref = db.reference('/predictions/')
    ref.set(json_data)

    print("Datos subidos exitosamente a Firebase")
