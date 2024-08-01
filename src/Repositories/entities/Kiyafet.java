package Repositories.entities;

public class Kiyafet extends Urun{

    private ETur tur;
    private ERenk renk;
    private ESize size;
    private EGender gender;

    public Kiyafet(Kiyafet kiyafet, int adet) {
        super(kiyafet.getUrunAd(), kiyafet.getFiyat());
        this.tur = kiyafet.tur;
        this.renk = kiyafet.renk;
        this.size = kiyafet.size;
        this.gender = kiyafet.gender;
        super.setAdet(adet);
    }

    public Kiyafet(String urunAd, Double fiyat) {
        super(urunAd, fiyat);
    }

    public ETur getTur() {
        return tur;
    }

    public void setTur(ETur tur) {
        this.tur = tur;
    }

    public ERenk getRenk() {
        return renk;
    }

    public void setRenk(ERenk renk) {
        this.renk = renk;
    }

    public ESize getSize() {
        return size;
    }

    public void setSize(ESize size) {
        this.size = size;
    }

    public EGender getGender() {
        return gender;
    }

    public void setGender(EGender gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Kiyafet{" +
                "id=" + getId() +
                ", urunAd='" + getUrunAd() + '\'' +
                ", fiyat=" + getFiyat() +
                +
                '}';
    }
    
    
    @Override
    public String detayliGoruntule() {
        return super.detayliGoruntule() + "Kiyafet{" + "gender=" + getGender() + ", renk=" + getRenk() + ", size=" + getSize() + ", tur=" + getTur() + '}';
    }
}