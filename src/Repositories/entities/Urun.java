package Repositories.entities;

public class Urun {
    private static Integer idCount = 0;
    private Integer id;
    private String urunAd;
    private Double fiyat;
    private Integer adet=0;
    private Integer sepetLimiti = 0;
    private String aciklama;


    public Urun(String urunAd, Double fiyat) {
        this.urunAd = urunAd;
        this.fiyat = fiyat;
        this.id = ++idCount;
    }

    public Urun(Urun urun, int adet) {
        this(urun.getUrunAd(), urun.getFiyat());
        this.adet=adet;
        this.id = urun.getId();
        --idCount;
        this.sepetLimiti =urun.sepetLimiti;


    }

    public Integer getSepetLimiti() {
        return sepetLimiti;
    }

    public void setSepetLimiti(Integer sepetLimiti) {
        this.sepetLimiti = sepetLimiti;
    }

    public Integer getAdet() {
        return adet;
    }

    public void setAdet(Integer adet) {
        this.adet = adet;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrunAd() {
        return urunAd;
    }

    public void setUrunAd(String urunAd) {
        this.urunAd = urunAd;
    }

    public Double getFiyat() {
        return fiyat;
    }

    public void setFiyat(Double fiyat) {
        this.fiyat = fiyat;
    }


    @Override
    public String toString() {

        System.out.printf("id=%d, urunAd='%s', fiyat=%.1f", id, urunAd, fiyat);
        return "";
    }
    
    public String detayliGoruntule() {
        return "Urun{" + "aciklama='" + aciklama + '\'' + ", id=" + getId() + ", urunAd='" + getUrunAd() + '\'' + ", fiyat=" + getFiyat() + ", adet=" + getAdet() + '}';
    }
    
    //tostring -> id, ürün adı, fiyat göstersin
    // ürünü seçtikten sonra detaylı toString göster. iki toString yazacaksın.
    // "Urun{" +
    //                "id=" + getId() +
    //                ", urunAd='" + getUrunAd() + '\'' +
    //                ", fiyat=" + getFiyat() +
    //                '}'
}