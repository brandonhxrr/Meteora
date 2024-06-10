import csv
import json

csv_file_path = './predictions_combined.csv'
json_file_path = './final_predictions.json'

def export_json():
    data = {}
    with open(csv_file_path, mode='r') as csv_file:
        csv_reader = csv.DictReader(csv_file)
        for row in csv_reader:
            locality = row['LOCALITY']
            
            
            date = row['DATE']
            year, month, day = map(int, date.split('-'))
            
            maxt = float(row['MAXT'])
            mint = float(row['MINT'])
            rainfall = float(row['RAINFALL'])

            if locality not in data:
                data[locality] = {}
            if year not in data[locality]:
                data[locality][year] = {}
            if month not in data[locality][year]:
                data[locality][year][month] = {}
            if day not in data[locality][year][month]:
                data[locality][year][month][day] = {}
            
            data[locality][year][month][day]['maxt'] = maxt
            data[locality][year][month][day]['mint'] = mint
            data[locality][year][month][day]['rainfall'] = rainfall

    with open(json_file_path, mode='w') as json_file:
        json.dump(data, json_file, indent=2)

export_json()