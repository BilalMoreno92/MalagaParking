package e.android9ed.malagaparking;

import java.io.Serializable;
import java.util.Date;

public class Parking implements Serializable{
    private int _id, _capacidad, _libres;
    private String _nombre, _direccion;
    private double _latitude, _longitude;
    private String _fechaAct;

    public Parking(int _id, String _nombre, String _direccion, double _latitude, double _longitude) {
        this._id = _id;
        this._nombre = _nombre;
        this._direccion = _direccion;
        this._latitude = _latitude;
        this._longitude = _longitude;
    }

    public Parking(int _id, int _capacidad, int _libres, String _nombre, String _direccion, double _latitude, double _longitude, String  _fechaAct) {
        this._id = _id;
        this._capacidad = _capacidad;
        this._libres = _libres;
        this._nombre = _nombre;
        this._direccion = _direccion;
        this._latitude = _latitude;
        this._longitude = _longitude;
        this._fechaAct = _fechaAct;
    }

    public Parking(int _id, String _nombre) {
        this._id = _id;
        this._nombre = _nombre;
    }

    public void setCapacidad(int _capacidad) {
        this._capacidad = _capacidad;
    }

    public void setLibres(int _libres) {
        this._libres = _libres;
    }

    public void setDir(String _direccion) {
        this._direccion = _direccion;
    }

    public void setLatitude(double _latitude) {
        this._latitude = _latitude;
    }

    public void setLongitude(double _longitude) {
        this._longitude = _longitude;
    }

    public void setFechaAct(String _fechaAct) {
        this._fechaAct = _fechaAct;
    }

    public int getId() {
        return _id;
    }

    public int getCapacidad() {
        return _capacidad;
    }

    public int getLibres() {
        return _libres;
    }

    public String getNombre() {
        return _nombre;
    }

    public String getDireccion() {
        return _direccion;
    }

    public double getLatitude() {
        return _latitude;
    }

    public double getLongitude() {
        return _longitude;
    }

    public String getFechaAct() {
        return _fechaAct;
    }
}
