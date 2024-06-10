import pandas as pd
import os

folder_path = './predictions/'

def mix_predictions(parameter):
    dfs = []

    if not os.path.exists("mixed_predictions"):
        os.makedirs("mixed_predictions")

    for file_name in os.listdir(folder_path):
        if parameter in file_name and file_name.endswith('.csv'):
            _, parameter, alcaldia = file_name.split('_')
            alcaldia = alcaldia.replace('.csv', '')

            print("Procesando predicciones de", parameter, "para", alcaldia)
            
            df = pd.read_csv(os.path.join(folder_path, file_name))
            
            df['LOCALITY'] = alcaldia
            
            df = df.rename(columns={'DATE': 'DATE', 'PREDICTION': parameter})
            
            dfs.append(df)

    combined_df = pd.concat(dfs, ignore_index=True)

    combined_df.to_csv(f'./mixed_predictions/mixed_{parameter}.csv', index=False)
