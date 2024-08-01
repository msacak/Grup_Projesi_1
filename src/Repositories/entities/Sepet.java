package Repositories.entities;

import java.util.ArrayList;
import java.util.UUID;

public class Sepet {

    private Double toplamFiyat =0.;
    ArrayList<Urun> urunArrayList;
    private final String uuid;
    
    public String getUuid() {
        return uuid;
    }
    
    public Sepet(){
        urunArrayList = new ArrayList<>();
        uuid = UUID.randomUUID().toString();
    }

    public ArrayList<Urun> getUrunArrayList() {
        return urunArrayList;
    }

    public Double getToplamFiyat() {
        return toplamFiyat;
    }

    public void setToplamFiyat(Double toplamFiyat) {
        this.toplamFiyat = toplamFiyat;
    }
    
    @Override
    public String toString() {
        return "Sepet{" + "toplamFiyat=" + getToplamFiyat() + ", urunArrayList=" + getUrunArrayList() + '}';
    }
}