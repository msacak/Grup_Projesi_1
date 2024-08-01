package Repositories;


import Repositories.entities.*;
import kullanici_kayit_sistemi.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class UrunSecimSistemi {
    static Scanner sc = new Scanner(System.in);
    static DatabaseManager<Kiyafet> db = new DatabaseManager();


    public static void main(String[] args) {
    
    }

    public static void demoVeriOlustur(){
        ArrayList<Kiyafet> kiyafetList = db.urunList;
        String[] adlar = {"tisort", "pantolon", "ayakkabi"};
        DecimalFormat df = new DecimalFormat("#,00");
        for (int j = 0; j < adlar.length; j++) {
            for (int i = 0; i < 5; i++) {
                Random random = new Random();
                ERenk[] renkArr = ERenk.values();
                ESize[] sizeArr = ESize.values();
                EGender[] genderArr = EGender.values();
                ETur[] turArr = ETur.values();
                
                
                Kiyafet kiyafet = new Kiyafet(adlar[j], // çalışıyorsa dokunma
                                              random.nextInt(400, 1000) + Double.parseDouble(df.format(random.nextDouble(100,1000))));
                kiyafet.setRenk(renkArr[random.nextInt(ERenk.values().length)]);
                kiyafet.setSize(sizeArr[random.nextInt(ESize.values().length)]);
                kiyafet.setGender(genderArr[random.nextInt(EGender.values().length)]);
                kiyafet.setTur(turArr[random.nextInt(ETur.values().length)]);
                kiyafet.setAdet(random.nextInt(1, 21));
                db.save(kiyafet);
            }
        }
        System.out.println(kiyafetList);

    }
    
    
    
    public static void welcomeMenu(Kullanici kullanici) {
        Sepet sepet = new Sepet();
        while (true) {
            System.out.println("""
                                       1- Urun listele
                                       2- Urun incele
                                       3- Sepeti görüntüle
                                       4- Alisverisi tamamla
                                       0- Çıkış yap
                                       """);
                               
                               
                               
                               
                               
                               
                               System.out.print("Seciminiz: ");
            int secim = secimYap();
            
            

            switch (secim) {
                case 0:
                    System.out.println("Çıkış yapıyorsunuz.");
                    return;
                case 1:{
                   db.getAll();
                   break;
                }
                case 2:{
                    urunSec(sepet);
                    break;
                }
                case 3:{
                    sepetiGoruntule(sepet);
                    break;
                }
                case 4: {
                    alisverisiTamamla(kullanici, sepet);
                    sepet = new Sepet(){};
                    break;
                }
            }
        }
    }
    
    private static void alisverisiTamamla(Kullanici kullanici, Sepet sepet) {
        kullanici.getSatinAlimGecmisi().add(sepet);
        System.out.println("Alisverisiniz gerceklesmistir, kargo bilgileri icin bilgilendirme maili alacaksiniz.  " +
                                   "Bizi sectiginiz icin tesekkur ederiz!");
    }
    
    public static void sepetiGoruntule(Sepet sepet) {
        if (sepet.getUrunArrayList().isEmpty()){
            System.out.println("Sepetiniz bos");
            return;
        }
        System.out.println(sepet.getUuid());
        for (Urun urun: sepet.getUrunArrayList()) {
            System.out.printf("id:%-1d %-12s %2d ad. x %8.1f TL = %8.1f TL%n", urun.getId(), urun.getUrunAd(),
                              urun.getAdet(), urun.getFiyat(), urun.getAdet() * urun.getFiyat());
        }
        System.out.printf("%40s %9.1f TL%n", "Toplam fiyat =", sepet.getToplamFiyat());
        
    }
    
    // burdan id alıp urunSecenekleri metoduna gidiyoruz.
    public static void urunSec(Sepet sepet){
        while(true) {
            System.out.println("Almak istediğiniz ürün için id giriniz: ");
            int secim = secimYap();
            Urun urun = db.findByID(secim);
            if (urun == null) {
                System.out.println("Girdiğiniz id'de kayıtlı ürün bulunmamaktadır.");
                return;
            }
            urunSecenekleri(urun, sepet);
            return;
        }
    }

    public static void urunSecenekleri(Urun urun,Sepet sepet){
        System.out.println(urun);
        System.out.println("1 - Sepete ekle");
        System.out.println("2 - Ürün detaylarını göster");
        System.out.println("0 - Ana menüye geri dön");
        System.out.print("Seciminiz: ");
        while (true){
            int secim = secimYap();
            switch (secim) {
                case 0:
                    System.out.println("Ana menüye dönüyorsunuz.");
                    return;
                case 1:
                    sepeteEkle(urun, sepet);
                    return;
                case 2:
                    urunDetayGoruntule(urun, sepet);
                    return;
                default:
                    System.out.println("Gecersiz girdi");
            }
        }


    }
    
    private static void urunDetayGoruntule(Urun urun, Sepet sepet) {
        System.out.println(urun.detayliGoruntule());
        while (true){
            System.out.println("1 - Sepete ekle");
            System.out.println("0 - Ana menüye geri dön");
            System.out.print("Seciminiz: ");
            int secim = secimYap();
            switch (secim) {
                case 0:
                    System.out.println("Ana menüye dönüyorsunuz.");
                    return;
                case 1:
                    sepeteEkle(urun, sepet);
                    return;
                default:
                    System.out.println("Gecersiz girdi");
            }
        }
    }
    
    public static void sepeteEkle(Urun urun,Sepet sepet){
        while (true) {
            if (urun.getAdet() == 0) {
                System.out.println("Urun stokta bulunmamaktadır");
                return;
            }
            int sepettekiAdet = 0;
            Urun tempUrun = null;
            for (Urun urunX:sepet.getUrunArrayList()){
                if (urunX.getId().equals(urun.getId())) {
                    sepettekiAdet = urunX.getAdet();
                    tempUrun = urunX;
                    break;
                }
            }
            System.out.println("Bu üründen " + urun.getAdet() + " adet bulunmaktadir.");
            System.out.println("Kaç adet almak istersiniz?(Geri donmek icin 0 tuslayiniz");
            int adet = secimYap() + sepettekiAdet;
            if (adet == 0){
                System.out.println("Satin alimdan vazgecildi, ana menuye donuluyor...");
                return;
            }
            
            if (adetCheck(urun, adet)) {
                if (tempUrun != null){
                    tempUrun.setAdet(adet);
                    sepet.setToplamFiyat(sepet.getToplamFiyat() + urun.getFiyat() * (adet - sepettekiAdet));
                    System.out.println("Sepetinizdeki adet guncellenmistir.");
                    return;
                }
                Urun sepetUrun = new Urun(urun, adet);
                sepet.setToplamFiyat(sepet.getToplamFiyat() + urun.getFiyat() * adet);
                sepet.getUrunArrayList().add(sepetUrun);
                System.out.println("Ürün sepete eklenmiştir.");
                return;
                
            }
            else {
                System.out.println("Bu üründen yeterli sayida stoğumuz bulunmamaktadir.");
            }
        }
    }
    public static boolean adetCheck(Urun urun,int adet){

        return urun.getAdet()>=adet;
    }
    
    public static int secimYap(){
        int secim;
        while (true) {
            try {
                secim = sc.nextInt();
                if (secim < 0) {
                    System.out.println("Negatif deger kabul etmiyoruz");
                    continue;
                }
                return secim;
            }
            catch (Exception e) {
                System.out.println("Gecersiz deger");
            }
            finally {
                sc.nextLine();
            }
        }
    }
}