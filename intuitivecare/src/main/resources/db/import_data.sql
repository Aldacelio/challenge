CREATE OR REPLACE FUNCTION import_operadoras(base_path TEXT) RETURNS INTEGER AS $$
DECLARE
    file_path TEXT;
    import_count INTEGER;
BEGIN
    file_path := base_path || '/operadoras/Relatorio_cadop.csv';
    
    EXECUTE format('
        COPY operadoras FROM %L WITH (
            FORMAT csv,
            DELIMITER '';'',
            QUOTE ''"'',
            HEADER,
            ENCODING ''UTF8'',
            NULL '''',
            FORCE_NOT_NULL (registro_ans, cnpj, razao_social)
        )', file_path);
    
    GET DIAGNOSTICS import_count = ROW_COUNT;
    RETURN import_count;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION import_demonstracoes(base_path TEXT) RETURNS INTEGER AS $$
DECLARE
    file_path TEXT;
    total_count INTEGER := 0;
    import_count INTEGER;
BEGIN
    FOR ano IN 2023..2024 LOOP
        FOR trimestre IN 1..4 LOOP
            file_path := base_path || '/demonstracoes_contabeis/' || trimestre || 'T' || ano || '.csv';
            
            CREATE TEMP TABLE temp_import (
                data DATE,
                reg_ans VARCHAR(20),
                cd_conta_contabil VARCHAR(20),
                descricao VARCHAR(255),
                vl_saldo_inicial VARCHAR(50),
                vl_saldo_final VARCHAR(50)
            ) WITH (autovacuum_enabled = off);
            
            EXECUTE format('
                COPY temp_import FROM %L WITH (
                    FORMAT csv,
                    DELIMITER '';'',
                    QUOTE ''"'',
                    HEADER,
                    ENCODING ''UTF8'',
                    NULL ''''
                )', file_path);
            
            INSERT INTO demonstracoes_contabeis (
                data, reg_ans, cd_conta_contabil, descricao,
                vl_saldo_inicial, vl_saldo_final
            )
            SELECT 
                data,
                reg_ans,
                cd_conta_contabil,
                descricao,
                NULLIF(replace(vl_saldo_inicial, ',', '.'),'')::NUMERIC,
                NULLIF(replace(vl_saldo_final, ',', '.'),'')::NUMERIC
            FROM temp_import;
            
            GET DIAGNOSTICS import_count = ROW_COUNT;
            total_count := total_count + import_count;
            
            DROP TABLE temp_import;
        END LOOP;
    END LOOP;
    
    RETURN total_count;
END;
$$ LANGUAGE plpgsql;