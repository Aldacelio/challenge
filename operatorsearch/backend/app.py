from flask import Flask, request, jsonify
from flask_cors import CORS
import pandas as pd

app = Flask(__name__)
CORS(app)

df = pd.read_csv('../../intuitivecare/src/main/resources/data/operadoras/Relatorio_cadop.csv',
                 sep=';', 
                 encoding='utf-8',
                 dtype={
                     'Registro_ANS': str,
                     'CNPJ': str,
                     'Razao_Social': str,
                     'Nome_Fantasia': str,
                     'Modalidade': str,
                     'Logradouro': str,
                     'Bairro': str,
                     'Cidade': str,
                     'UF': str,
                     'Representante': str,
                 },
                 parse_dates=['Data_Registro_ANS'])

@app.route('/api/search', methods=['GET'])
def search_operators():
    query = request.args.get('q', '')
    if not query:
        return jsonify([])
    
    result = df[
        df['Registro_ANS'].str.contains(query, case=False, na=False) |
        df['CNPJ'].str.contains(query, case=False, na=False) |
        df['Razao_Social'].str.contains(query, case=False, na=False) |
        df['Nome_Fantasia'].str.contains(query, case=False, na=False) |
        df['Modalidade'].str.contains(query, case=False, na=False) |
        df['Logradouro'].str.contains(query, case=False, na=False) |
        df['Bairro'].str.contains(query, case=False, na=False) |
        df['Cidade'].str.contains(query, case=False, na=False) |
        df['UF'].str.contains(query, case=False, na=False) |
        df['Representante'].str.contains(query, case=False, na=False)
        
    ]
    
    result['Data_Registro_ANS'] = result['Data_Registro_ANS'].dt.strftime('%Y-%m-%d')

    records = result.to_dict('records')
    return jsonify(records)

if __name__ == '__main__':
    app.run(debug=True, port=5000)
