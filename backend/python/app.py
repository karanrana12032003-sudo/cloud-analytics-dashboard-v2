from flask import Flask, request, jsonify
import pandas as pd
import io
import boto3

app = Flask(__name__)
s3 = boto3.client('s3')

@app.route('/process-data', methods=['POST'])
def process_data():
    if 'file' not in request.files:
        return jsonify({'error': 'No file part'}), 400
    file = request.files['file']
    if file.filename == '':
        return jsonify({'error': 'No selected file'}), 400
    if file:
        # Read the file into a pandas DataFrame
        if file.filename.endswith('.csv'):
            df = pd.read_csv(io.StringIO(file.stream.read().decode('utf-8')))
        elif file.filename.endswith(('.xls', '.xlsx')):
            df = pd.read_excel(file.stream.read())
        else:
            return jsonify({'error': 'Unsupported file type'}), 400

        # Perform some basic cleaning and transformation (example)
        df = df.dropna() # Drop rows with any missing values
        df = df.drop_duplicates() # Drop duplicate rows

        # Example: Convert a column to uppercase if it exists
        if 'category' in df.columns:
            df['category'] = df['category'].str.upper()

        # Save processed data to S3 (example)
        bucket_name = 'your-s3-bucket-name'
        file_key = f'processed_data/{file.filename}'
        s3.put_object(Bucket=bucket_name, Key=file_key, Body=df.to_csv(index=False))

        return jsonify({'message': 'Data processed and saved to S3', 'file_key': file_key}), 200

@app.route('/health', methods=['GET'])
def health_check():
    return jsonify({'status': 'healthy'}), 200

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)