package com.epam.jmp.models;

public class EmployeeCreationResponse {
    private String _index;
    private String _id;
    private int _version;
    private String result;
    private Shards _shards;
    private int _seq_no;
    private int _primary_term;

    public String get_index() {
        return _index;
    }

    public void set_index(String _index) {
        this._index = _index;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int get_version() {
        return _version;
    }

    public void set_version(int _version) {
        this._version = _version;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Shards get_shards() {
        return _shards;
    }

    public void set_shards(Shards _shards) {
        this._shards = _shards;
    }

    public int get_seq_no() {
        return _seq_no;
    }

    public void set_seq_no(int _seq_no) {
        this._seq_no = _seq_no;
    }

    public int get_primary_term() {
        return _primary_term;
    }

    public void set_primary_term(int _primary_term) {
        this._primary_term = _primary_term;
    }
}