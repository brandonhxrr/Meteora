import pandas as pd
import os

folder_path = './mixed_predictions/'

def combine():
    dfs = []

    for file_name in os.listdir(folder_path):
        if file_name.startswith('mixed_'):

            df = pd.read_csv(os.path.join(folder_path, file_name))

            dfs.append(df)

    combined_df = pd.concat(dfs, axis=1)
    combined_df = combined_df.loc[:,~combined_df.columns.duplicated()]

    combined_df = combined_df[['LOCALITY', 'DATE', 'MAXT', 'MINT', 'RAINFALL']]
    combined_df = combined_df.sort_values(by=['LOCALITY', 'DATE'])

    combined_df.to_csv('predictions_combined.csv', index=False)
