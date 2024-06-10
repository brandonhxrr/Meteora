import os
import csv
import urllib.request
import pandas as pd
from google.cloud import bigquery
from google.oauth2 import service_account
import chardet

def detect_encoding(file_path):
    with open(file_path, 'rb') as f:
        result = chardet.detect(f.read())
    return result['encoding']

def download_data_smn():
    if not os.path.exists("dirty_smn"):
        os.makedirs("dirty_smn")

    for i in range(9001, 9072):
        url = f"https://smn.conagua.gob.mx/tools/RESOURCES/Diarios/{i}.txt"
        filename = f"dirty_smn/{i}.txt"

        try:
            headers = {'User-Agent': 'Mozilla/5.0'}
            req = urllib.request.Request(url, headers=headers)
            with urllib.request.urlopen(req) as response:
                with open(filename, 'wb') as file:
                    file.write(response.read())
            print(f"Archivo {i}.txt descargado correctamente.")
        except Exception as e:
            print(f"Error al descargar el archivo {i}.txt:", str(e))

def download_data_sih():
    if not os.path.exists("dirty_sih"):
        os.makedirs("dirty_sih")

    stations = [
        "C09005", "C09059", "C09046", "C09023", "C09038", "C09039", "SBADF", "TELDF", "C09049", "TRGDF", 
        "TNGDF", "C09003", "CMYDF", "C09021", "PSNDF", "PTRDF", "C09011", "DVLDF", "C09054", "C09040", 
        "C09070", "C09008", "C09071", "C09014", "EAZDF", "C09072", "C09055", "PXTDF", "CCUDF", "C09016", 
        "C09019", "DTLDF", "EYQDF", "EZCDF", "C09030", "LVNDF", "TSLDF", "TECDF", "C09033", "C09069", 
        "C09031", "DDFDF", "MCRDF", "RCMDF", "C09015", "C09006", "CMTDF", "CEADF", "CYLDF", "C09017", 
        "C09029", "C09028", "C09025", "GCNDF", "PBLDF", "PRMDF", "PRRDF", "PGNDF", "C09043", "SJADF", 
        "SGDDF", "TCMDF", "C09062", "C09009", "C09056", "PLMDF", "C09036", "SCADF", "CDMDF", "EMRDF", 
        "LU4DF", "C09026", "OCSDF", "PBADF", "PMLDF", "PBODF", "PLEDF", "SCMDF", "C09052", "HYTDF", 
        "C09067", "MAGDF", "C09037", "TSFDF", "C09064", "CHLDF", "C09010", "C09012", "C09047", "C09050", 
        "CTMDF", "PCHDF", "PCTDF", "PBTDF", "C09048", "TSJDF", "C09032", "MLPDF", "C09044", "CTNDF", 
        "C09045", "SANDF", "TTLDF", "C09058", "PSCDF", "SPPDF", "C09051", "C09002", 
        "AJSDF", "C09004", "CFSDF", "C09020", "DATDF", "C09022", "C09024", "MZTDF", "PVCDF", "PSPDF", 
        "RBTDF", "TPJDF", "C09001", "C09007", "CDVDF", "C09013", "PBCDF", "TPNDF", "C09034", "PNTDF", 
        "PSLDF", "C09041", "C09042"
    ]
    
    for station in stations:
        url = f"https://sih.conagua.gob.mx/basedatos/Climas/{station}.csv"
        filename = f"dirty_sih/{station}.csv"

        try:
            headers = {'User-Agent': 'Mozilla/5.0'}
            req = urllib.request.Request(url, headers=headers)
            with urllib.request.urlopen(req) as response:
                with open(filename, 'wb') as file:
                    file.write(response.read())
            print(f"Archivo {station}.csv descargado correctamente.")
        except Exception as e:
            print(f"Error al descargar el archivo {station}.csv:", str(e))

def clean_data_smn():
    if not os.path.exists("clean"):
        os.makedirs("clean")

    csv_filepath = os.path.join("clean", "cleaned_smn.csv")
    with open(csv_filepath, mode="w", newline="") as csv_file:
        csv_writer = csv.writer(csv_file)
        csv_writer.writerow(["LOCALITY", "YEAR", "MONTH", "DAY", "RAINFALL", "MAXT", "MINT"])

        for filename in os.listdir("dirty_smn"):
            if filename.endswith(".txt"):
                filepath = os.path.join("dirty_smn", filename)

                encoding = detect_encoding(filepath)
                with open(filepath, mode="r", encoding=encoding, errors='ignore') as txt_file:
                    txt_lines = txt_file.readlines()

                locality = txt_lines[7].split(":")[1].strip()
            
                for line in txt_lines[20:]:
                    line = line.strip()
                    if line and line != "--------------------------------------":
                        
                        data = line.split()
                        try:
                            data = [data[0], data[1], data[3], data[4]]
                            fecha = data[0]

                            if "/" in fecha:
                                day, month, year = fecha.split('/')
                            elif "-" in fecha:
                                year, month, day = fecha.split('-')
                            else:
                                raise ValueError("Formato de fecha no reconocido")
                            
                            rainfall = data[1] if data[1] != "Nulo" else ""
                            maxt = data[2] if data[2] != "Nulo" else ""
                            mint = data[3] if data[3] != "Nulo" else ""
                            if(rainfall != "" or maxt != "" or mint != ""):
                                csv_writer.writerow([locality, year, month, day , rainfall, maxt, mint])
                        except Exception as e:
                            print("Error al procesar la línea:", line, str(e))

                print(f"Archivo {filename} limpiado correctamente")


def clean_data_sih():
    summary_data = []

    if not os.path.exists("clean"):
        os.makedirs("clean")

    csv_filepath = os.path.join("clean", "cleaned_sih_temp.csv")
    with open(csv_filepath, mode="w", newline="") as csv_file:
        csv_writer = csv.writer(csv_file)
        csv_writer.writerow(["LOCALITY", "YEAR", "MONTH", "DAY", "RAINFALL", "MAXT", "MINT"])

        for filename in os.listdir("dirty_sih"):
            if filename.endswith(".csv"):
                filepath = os.path.join("dirty_sih", filename)
                with open(filepath, mode="r", encoding="utf-8", errors='ignore') as txt_file:
                    txt_lines = txt_file.readlines()

                locality = txt_lines[5].split(":")[1].strip()
                total_records = len(txt_lines) - 8
                cleaned_records = 0

                for line in txt_lines[8:]:
                    line = line.strip()
                    if line:
                        data = line.split(',')
                        data = [data[0], data[1], data[3], data[4]]
                        fecha = data[0]
                        year, month, day = fecha.split('/')
                        rainfall = data[1] if data[1] not in ["", "-"] else ""
                        maxt = data[2] if data[2] not in ["", "-"] else ""
                        mint = data[3] if data[3] not in ["", "-"] else ""

                        if rainfall or maxt or mint:
                            csv_writer.writerow([locality, year, month, day, rainfall, maxt, mint])
                            cleaned_records += 1

                print(f"Archivo {filename} procesado correctamente. Total de registros antes: {total_records}, después: {cleaned_records}")
                summary_data.append({
                    "Nombre del Archivo": filename,
                    "Total de Registros Antes": total_records,
                    "Total de Registros Después": cleaned_records
                })

        summary_df = pd.DataFrame(summary_data)
        summary_table_path = os.path.join("clean", "summary_table_sih.csv")
        summary_df.to_csv(summary_table_path, index=False)

        encoding = detect_encoding(csv_filepath)

        df = pd.read_csv(csv_filepath, encoding=encoding)

        localidades = {
            "Álvaro Obregón": "ALVARO OBREGON",
            "Azcapotzalco": "AZCAPOTZALCO",
            "Benito Juárez": "BENITO JUAREZ",
            "Coyoacán": "COYOACAN",
            "Cuajimalpa de Morelos": "CUAJIMALPA DE MORELOS",
            "Cuauhtémoc": "CUAUHTEMOC",
            "Gustavo A. Madero": "GUSTAVO A MADERO",
            "Iztacalco": "IZTACALCO",
            "Iztapalapa": "IZTAPALAPA",
            "La Magdalena Contreras": "LA MAGDALENA CONTRERAS",
            "Miguel Hidalgo": "MIGUEL HIDALGO",
            "Milpa Alta": "MILPA ALTA",
            "Nezahualcóyotl": "NEZAHUALCOYOTL",
            "Tlalpan": "TLALPAN",
            "Tláhuac": "TLAHUAC",
            "Venustiano Carranza": "VENUSTIANO CARRANZA",
            "Xochimilco": "XOCHIMILCO"
        }

        df['LOCALITY'] = df['LOCALITY'].map(localidades)

        renamed_csv_path = './clean/cleaned_sih.csv'
        df.to_csv(renamed_csv_path, index=False)
                

def merge_data():
    cleaned_smn_path = os.path.join("clean", "cleaned_smn.csv")
    cleaned_sih_path = os.path.join("clean", "cleaned_sih.csv")
    merged_filepath = os.path.join("clean", "merged_data.csv")

    smn_df = pd.read_csv(cleaned_smn_path)
    sih_df = pd.read_csv(cleaned_sih_path)

    merged_df = pd.concat([smn_df, sih_df], ignore_index=True)
    merged_df.to_csv(merged_filepath, index=False)
    print(f"Datos combinados guardados en {merged_filepath}")


def load_to_bigquery():
    json_credentials_path = input("Ingrese la ruta del archivo JSON de credenciales de BigQuery: ")
    credentials = service_account.Credentials.from_service_account_file(json_credentials_path)
    client = bigquery.Client(credentials=credentials, project=credentials.project_id)

    merged_filepath = os.path.join("clean", "merged_data.csv")
    df = pd.read_csv(merged_filepath, dtype=str)

    df['YEAR'] = pd.to_numeric(df['YEAR'], errors='coerce')
    df['MONTH'] = pd.to_numeric(df['MONTH'], errors='coerce')
    df['DAY'] = pd.to_numeric(df['DAY'], errors='coerce')
    df['RAINFALL'] = pd.to_numeric(df['RAINFALL'].str.replace(',', ''), errors='coerce')
    df['MAXT'] = pd.to_numeric(df['MAXT'].str.replace(',', ''), errors='coerce')
    df['MINT'] = pd.to_numeric(df['MINT'].str.replace(',', ''), errors='coerce')

    print("Valores nulos por columna antes de subir a BigQuery:")
    print(df.isna().sum())

    schema = [
        bigquery.SchemaField("LOCALITY", "STRING"),
        bigquery.SchemaField("YEAR", "INTEGER"),
        bigquery.SchemaField("MONTH", "INTEGER"),
        bigquery.SchemaField("DAY", "INTEGER"),
        bigquery.SchemaField("RAINFALL", "FLOAT"),
        bigquery.SchemaField("MAXT", "FLOAT"),
        bigquery.SchemaField("MINT", "FLOAT"),
    ]

    table_id = credentials.project_id+'.conagua.weather'
    job_config = bigquery.LoadJobConfig(
        schema=schema,
        write_disposition=bigquery.WriteDisposition.WRITE_TRUNCATE,
    )

    load_job = client.load_table_from_dataframe(df, table_id, job_config=job_config)
    load_job.result()
    table = client.get_table(table_id)
    print(f"Cargados {table.num_rows} filas a la tabla {table_id}")

def main_menu():
    while True:
        print("\n---------------METEORA---------------")
        print("\n\nMenú de Opciones:")
        print("1. Descargar datos del SMN")
        print("2. Descargar datos del SIH")
        print("3. Limpiar datos del SMN")
        print("4. Limpiar datos del SIH")
        print("5. Combinar datos del SMN y del SIH")
        print("6. Cargar datos a BigQuery")
        print("7. Ejecutar todo el proceso")
        print("8. Salir")

        choice = input("Seleccione una opción (1-8): ")

        if choice == '1':
            download_data_smn()
        elif choice == '2':
            download_data_sih()
        elif choice == '3':
            clean_data_smn()
        elif choice == '4':
            clean_data_sih()
        elif choice == '5':
            merge_data()
        elif choice == '6':
            load_to_bigquery()
        elif choice == '7':
            download_data_smn()
            download_data_sih()
            clean_data_smn()
            clean_data_sih()
            merge_data()
            load_to_bigquery()
        elif choice == '8':
            print("Saliendo...")
            break
        else:
            print("Opción no válida, por favor intente de nuevo.")

if __name__ == "__main__":
    main_menu()
