package com.absontwikkeling.rijtjes;

public class questionObj {

    private int _id;
    private String _table_name;
    private String _question;
    private String _answer;

    public questionObj(String tableName, String question, String answer) {
        this._table_name = tableName;
        this._question = question;
        this._answer = answer;
    }

    public void set_id(int id) {
        this._id = id;
    }

    public void set_table_name(String tableName) {
        this._table_name = tableName;
    }

    public void set_question(String question) {
        this._question = question;
    }

    public void set_answer(String answer) {
        this._answer = answer;
    }

    public int get_id() {
        return _id;
    }

    public String get_table_name() {
        return _table_name;
    }

    public String get_question() {
        return _question;
    }

    public String get_answer() {
        return _answer;
    }

}
