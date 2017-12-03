package com.absontwikkeling.rijtjes;

// TODO: Sort the code
// TODO: Explain code better
// TODO: Add change strings to @String rescources

public class questionObj {

    private int _id;
    private String _table_name;
    private String _question;
    private String _answer;

    public questionObj() {

    }

    public questionObj(String question, String answer, String tableName) {
        this._table_name = tableName.replaceAll("\\s","_");
        this._question = question;
        this._answer = answer;
    }

    public void set_id(int id) {
        this._id = id;
    }

    public int get_id() {
        return _id;
    }


    public void set_table_name(String tableName) {
        this._table_name = tableName;
    }

    public String get_table_name() {
        return _table_name;
    }


    public void set_question(String question) {
        this._question = question;
    }

    public String get_question() {
        return _question;
    }

    public void set_answer(String answer) {
        this._answer = answer;
    }

    public String get_answer() {
        return _answer;
    }
}
