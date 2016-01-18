package com.example.akil.resistance;


public class Warriors {

    private int _id;
    private String _name;
    private String _contactno;
    private String _imgpath;
    private String _affiliation;
    private String _species;
    private String _gender;
    private String _date;
    private String _planet;

    public Warriors(String _name,String _contactno,String _imgpath,String _affiliation,String _species,String _gender,String _date,String _planet){
        this._name=_name;
        this._contactno=_contactno;
        this._imgpath=_imgpath;
        this._affiliation=_affiliation;
        this._species=_species;
        this._gender=_gender;
        this._date=_date;
        this._planet=_planet;
    }

    public void set_affiliation(String _affiliation) {
        this._affiliation = _affiliation;
    }

    public void set_contactno(String _contactno) {
        this._contactno = _contactno;
    }

    public void set_gender(String _gender) {
        this._gender = _gender;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_imgpath(String _imgpath){
        this._imgpath=_imgpath;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public void set_planet(String _planet) {
        this._planet = _planet;
    }

    public void set_species(String _species) {
        this._species = _species;
    }


    public String get_affiliation() {
        return _affiliation;
    }

    public String get_contactno() {
        return  _contactno;
    }

    public String get_gender() {
        return  _gender;
    }

    public String get_date() {
        return  _date;
    }

    public int get_id() {
        return  _id;
    }

    public String get_imgpath(){
        return _imgpath;
    }

    public String get_name() {
        return  _name;
    }

    public String get_planet() {
        return _planet;
    }

    public String get_species() {
        return  _species;
    }

}
