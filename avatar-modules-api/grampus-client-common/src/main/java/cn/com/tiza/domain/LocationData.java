package cn.com.tiza.domain;

import cn.com.tiza.DataConstants;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author tz0658
 */
public class LocationData implements Serializable {
    private Integer status;         //0:valid location, 1:invalid location
    private Integer tlat;   //0:northern latitude, 1:south latitude
    private Integer tlon; //0:east longitude, 1:west longitude
    @JsonProperty(DataConstants.MDT_PO_LAT)
    private Double lat;
    @JsonProperty(DataConstants.MDT_PO_LON)
    private Double lon;

    private Double slat;
    private Double slon;

    private String prov;
    private String city;
    private String dist;
    private Double speed;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTlat() {
        return tlat;
    }

    public void setTlat(Integer tlat) {
        this.tlat = tlat;
    }

    public Integer getTlon() {
        return tlon;
    }

    public void setTlon(Integer tlon) {
        this.tlon = tlon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getSlat() {
        return slat;
    }

    public void setSlat(Double slat) {
        this.slat = slat;
    }

    public Double getSlon() {
        return slon;
    }

    public void setSlon(Double slon) {
        this.slon = slon;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String latStr() {
        return String.format("%.6f", this.lat);
    }

    public String lonStr() {
        return String.format("%.6f", this.lon);
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "LocationData{" +
                "status=" + status +
                ", tlat=" + tlat +
                ", tlon=" + tlon +
                ", lat=" + lat +
                ", lon=" + lon +
                ", slat=" + slat +
                ", slon=" + slon +
                ", prov='" + prov + '\'' +
                ", city='" + city + '\'' +
                ", dist='" + dist + '\'' +
                ", speed=" + speed +
                '}';
    }
}
