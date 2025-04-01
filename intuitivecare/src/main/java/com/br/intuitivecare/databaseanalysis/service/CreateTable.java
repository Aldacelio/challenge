package com.br.intuitivecare.databaseanalysis.service;

import java.sql.Connection;
import com.br.intuitivecare.utils.ui.Dialog;
import com.br.intuitivecare.utils.ui.ProgressDialog;
import com.br.intuitivecare.databaseanalysis.util.ScriptExecute;

public class CreateTable {

    private static final String SCHEMA_PATH = "src/main/resources/db/schema.sql";

    public void create(Connection conn) throws Exception {
        ProgressDialog.show("Criando tabelas...", "Criando tabelas");
        ScriptExecute.execute(conn, SCHEMA_PATH);
        ProgressDialog.close("Criando tabelas");
        Dialog.showMessage("Tabelas criadas com sucesso!", "Concluído");
    }
}