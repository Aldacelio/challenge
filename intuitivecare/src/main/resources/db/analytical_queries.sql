WITH ultimo_trimestre AS (
    SELECT MAX(DATE_TRUNC('quarter', data)) as trimestre
    FROM demonstracoes_contabeis
)
SELECT 
    o.registro_ans,
    o.razao_social,
    ABS(dc.vl_saldo_final) as despesa_total
FROM demonstracoes_contabeis dc
JOIN operadoras o ON dc.reg_ans = o.registro_ans
WHERE 
    dc.descricao LIKE 'EVENTOS/ SINISTROS CONHECIDOS OU AVISADOS%' AND
    DATE_TRUNC('quarter', dc.data) = (SELECT trimestre FROM ultimo_trimestre)
ORDER BY despesa_total DESC
LIMIT 10;

WITH ultimo_ano AS (
    SELECT EXTRACT(YEAR FROM MAX(data)) as ano
    FROM demonstracoes_contabeis
)
SELECT 
    o.registro_ans,
    o.razao_social,
    SUM(ABS(dc.vl_saldo_final)) as despesa_total_anual
FROM demonstracoes_contabeis dc
JOIN operadoras o ON dc.reg_ans = o.registro_ans
WHERE 
    dc.descricao LIKE 'EVENTOS/ SINISTROS CONHECIDOS OU AVISADOS%' AND
    EXTRACT(YEAR FROM dc.data) = (SELECT ano FROM ultimo_ano)
GROUP BY o.registro_ans, o.razao_social
ORDER BY despesa_total_anual DESC
LIMIT 10;