package Repositories;


import Repositories.entities.*;
import Repositories.utilities.EGender;
import Repositories.utilities.ERenk;
import Repositories.utilities.ESize;
import Repositories.utilities.ETur;
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

    public static void demoVeriOlustur() {
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
                        random.nextInt(400, 1000) + Double.parseDouble(df.format(random.nextDouble(100, 1000))));
                kiyafet.setRenk(renkArr[random.nextInt(ERenk.values().length)]);
                kiyafet.setSize(sizeArr[random.nextInt(ESize.values().length)]);
                kiyafet.setGender(genderArr[random.nextInt(EGender.values().length)]);
                kiyafet.setTur(turArr[random.nextInt(ETur.values().length)]);
                Integer adet = random.nextInt(1, 21);
                kiyafet.setAdet(adet);
                kiyafet.setSepetLimiti(adet * 3 / 4);
                db.save(kiyafet);
            }
        }

    }


    public static void welcomeMenu(Kullanici kullanici) {
        Sepet sepet;
        if (kullanici == null) sepet = new Sepet();
        else sepet = kullanici.getGuncelSepet();

        System.out.println("##### MODA PATIKASI #####");
        while (true) {
            System.out.println("""
                    1- Urun listele
                    2- Urun incele
                    3- Sepeti görüntüle
                    4- Alisverisi tamamla
                    5- Sepetten ürün çıkar
                    6- Sepetten ürün azalt
                    7- Sepetten tüm ürünleri sil
                    0- Çıkış yap
                    """);
            System.out.print("Seciminiz: ");
            int secim = secimYap();

            switch (secim) {
                case 0:
                    System.out.println("Çıkış yapıyorsunuz.");
                    return;
                case 1: {
                    db.getAll();
                    break;
                }
                case 2: {
                    urunSec(sepet, kullanici);
                    break;
                }
                case 3: {
                    sepetiGoruntule(sepet);
                    break;
                }
                case 4: {
                    alisverisiTamamla(kullanici, sepet);
                    sepet = new Sepet() {
                    };
                    break;
                }
                case 5: {
                    sepettenUrunCikar(sepet);
                    break;
                }
                case 6:
                    sepetUrunAzalt(sepet);
                    break;
                case 7:
                    sepettenTumUrunleriSil(sepet);
            }
        }
    }

    private static void sepettenTumUrunleriSil(Sepet sepet) {
        sepet.getUrunArrayList().removeAll(sepet.getUrunArrayList());
        System.out.println("Sepetinizden tüm ürünler kaldırılmıştır.");
    }

    private static void sepetUrunAzalt(Sepet sepet) {
        sepetiGoruntule(sepet);
        System.out.print("Adetini azaltmak istediğiniz ürünün idsini giriniz: ");
        int urunId = secimYap();
        Urun urun = findById(sepet, urunId);
        if (urun == null) {
            System.out.println("Çıkarmak istediğiniz ürün id'si sepette bulunamamıştır.");
            return;
        }
        System.out.print("Kaç adet azaltmak istiyorsunuz: ");
        int adetAzalt = secimYap();
        sepet.setToplamFiyat(sepet.getToplamFiyat() - urun.getFiyat() * Math.min(adetAzalt, urun.getAdet()));
        int adetMin = urun.getAdet() - adetAzalt;
        urun.setAdet(Math.max(0, adetMin));
        if (urun.getAdet() == 0) {
            sepet.getUrunArrayList().remove(findById(sepet, urunId));
        }

    }


    private static void sepettenUrunCikar(Sepet sepet) {
        sepetiGoruntule(sepet);
        System.out.print("Sepetten kaldirmak istediginiz urunun id'sini giriniz: ");
        int urunId = secimYap();
        Urun urun = findById(sepet, urunId);
        if (urun == null) {
            System.out.println("Çıkarmak istediğiniz ürün id'si sepette bulunamamıştır.");
            return;
        }
        sepet.setToplamFiyat(sepet.getToplamFiyat() - (urun.getFiyat() * urun.getAdet()));
        sepet.getUrunArrayList().remove(findById(sepet, urunId));
        System.out.println("Ürün sepetinizden kaldırılmıştır.");
    }

    private static Urun findById(Sepet sepet, int urunId) {
        for (Urun urun : sepet.getUrunArrayList()) {
            if (urun.getId() == urunId) return urun;
        }
        return null;
    }

    private static Kullanici alisverisiTamamla(Kullanici kullanici, Sepet sepet) {
        int i = 0;
        while (kullanici == null) {
            System.out.println("Alisverisi tamamlamak icin hesabiniza giris yapmaniz gerekiyor");
            kullanici = KullaniciKayitSistemi.girisYap();
            i++;
            if (i > 3) {
                System.out.println("3 kere başarısız giriş yaptınız.");
                return null;
            }
        }
        kullanici.getSatinAlimGecmisi().add(sepet);
        for (Urun urun : sepet.getUrunArrayList()) {
            Urun siradakiUrun = db.findByID(urun.getId());
            siradakiUrun.setAdet(siradakiUrun.getAdet() - urun.getAdet());
        }
        System.out.println("Alisverisiniz gerceklesmistir, kargo bilgileri icin bilgilendirme maili alacaksiniz.  " +
                "Bizi sectiginiz icin tesekkur ederiz!");
        return kullanici;
    }

    public static void sepetiGoruntule(Sepet sepet) {
        if (sepet.getUrunArrayList().isEmpty()) {
            System.out.println("Sepetiniz bos");
            return;
        }
        System.out.println(sepet.getUuid());
        for (Urun urun : sepet.getUrunArrayList()) {
            System.out.printf("id:%-1d %-12s %2d ad. x %8.1f TL = %8.1f TL%n", urun.getId(), urun.getUrunAd(),
                    urun.getAdet(), urun.getFiyat(), urun.getAdet() * urun.getFiyat());
        }
        System.out.printf("%40s %9.1f TL%n", "Toplam fiyat =", sepet.getToplamFiyat());

    }

    // burdan id alıp urunSecenekleri metoduna gidiyoruz.
    public static void urunSec(Sepet sepet, Kullanici kullanici) {
        while (true) {
            System.out.print("İncelemek istediğiniz ürün için id giriniz: ");
            int secim = secimYap();
            Urun urun = db.findByID(secim);
            if (urun == null) {
                System.out.println("Girdiğiniz id'de kayıtlı ürün bulunmamaktadır.");
                return;
            }
            urunSecenekleri(urun, sepet, kullanici);
            return;
        }
    }

    public static void urunSecenekleri(Urun urun, Sepet sepet, Kullanici kullanici) {
        System.out.println(urun);
        System.out.println("1 - Sepete ekle");
        System.out.println("2 - Ürün detaylarını göster");
        System.out.println("3 - Favorilerime ekle");
        System.out.println("0 - Ana menüye geri dön");
        System.out.print("Seciminiz: ");
        while (true) {
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
                case 3:
                    favorilerimeEkle(urun, kullanici);
                    return;
                default:
                    System.out.println("Lütfen menüde belirtilen uygun aralıklarda seçim yapınız.");
            }
        }


    }

    private static void favorilerimeEkle(Urun urun, Kullanici kullanici) {
        ArrayList<Urun> favList = kullanici.getFavoriList();
        if(!favList.contains(urun)){
            favList.add(urun);
            System.out.println("Ürün başarıyla favorilerinize eklendi.");
            return;
        }
        System.out.println("Ürün zaten favorilerinizde.");
    }

    private static void urunDetayGoruntule(Urun urun, Sepet sepet) {
        System.out.println(urun.detayliGoruntule());
        while (true) {
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
            if (tempUrun != null) System.out.println("Sepetinizde bu urunden " + sepettekiAdet +" adet bulunmaktadir.");
            System.out.print("Kaç adet almak istersiniz?(Geri donmek icin 0 tuslayiniz): ");
            int adet = secimYap() + sepettekiAdet;
            if (adet > urun.getSepetLimiti()){
                System.out.println("Maalesef ayni urunden cok sayida almaya calisiyorsunuz.  Talep " +
                        "gerceklestirilemedi.");
                return;
            }
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

    public static boolean adetCheck(Urun urun, int adet) {

        return urun.getAdet() >= adet;
    }

    public static int secimYap() {
        int secim;
        while (true) {
            try {
                secim = sc.nextInt();
                if (secim < 0) {
                    System.out.println("Negatif deger kabul etmiyoruz");
                    continue;
                }
                return secim;
            } catch (Exception e) {
                System.out.println("Gecersiz deger");
            } finally {
                sc.nextLine();
            }
        }
    }
}