from flask import Flask, request, jsonify
from flask_cors import CORS
import pandas as pd
from functools import lru_cache

app = Flask(__name__)
CORS(app)

@lru_cache(maxsize=1)
def load_dataframe():
    df = pd.read_csv('../../intuitivecare/src/main/resources/data/operadoras/Relatorio_cadop.csv',
                     sep=';', 
                     encoding='utf-8',
                     dtype={
                         'Registro_ANS': str,
                         'CNPJ': str,
                         'Razao_Social': str,
                         'Modalidade': str,
                         'Cidade': str,
                         'UF': str,
                         'Representante': str,
                     })
    
    df['search_text'] = (df['Razao_Social'].str.lower().fillna('') + ' ' + 
                        df['Registro_ANS'].str.lower().fillna(''))
    return df

@app.route('/api/search', methods=['GET'])
def search_operators():
    query = request.args.get('q', '').strip().lower()
    page = int(request.args.get('page', 1))
    per_page = int(request.args.get('per_page', 10))
    
    if not query:
        return jsonify({'total': 0, 'page': page, 'items': []})
    
    df = load_dataframe()
    
    mask = df['search_text'].str.contains(query, na=False)
    result = df[mask]
    
    columns_to_show = [
        'Registro_ANS', 'CNPJ', 'Razao_Social',
        'Modalidade', 'Cidade', 'UF', 'Representante'
    ]
    
    total_results = len(result)
    
    start_idx = (page - 1) * per_page
    end_idx = start_idx + per_page
    
    result = result.iloc[start_idx:end_idx][columns_to_show]
    
    result = result.fillna('-')
    result = result.apply(lambda x: x.str.strip() if x.dtype == "object" else x)
    result = result.replace(r'^\s*$', '-', regex=True)
    
    return jsonify({
        'total': total_results,
        'page': page,
        'items': result.to_dict('records')
    })

if __name__ == '__main__':
    app.run(debug=True, port=5000)
