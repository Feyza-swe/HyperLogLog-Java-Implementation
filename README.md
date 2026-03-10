# HyperLogLog (HLL) Cardinality Estimator

Bu proje, Büyük Veri Analitiği kapsamında "Cardinality Estimation" (Küme Büyüklüğü Tahmini) problemini çözmek için kullanılan **HyperLogLog (HLL)** algoritmasının sıfırdan Java ile gerçekleştirilmiş bir uygulamasıdır.

## 🚀 Proje Amacı
Milyarlarca verinin bulunduğu sistemlerde, benzersiz öğe sayısını (örneğin: bir web sitesine giren eşsiz ziyaretçi sayısı) her bir öğeyi hafızada tutmadan (Set kullanmadan), çok küçük bir bellek alanı (birkaç KB) kullanarak %1-3 hata payıyla tahmin etmektir.

## 🛠️ Teknik Özellikler
Algoritma şu temel bileşenleri içermektedir:
- **Yüksek Kaliteli Hashing:** Veriyi homojen dağıtmak için 32-bit hash fonksiyonu.
- **Bucketing (Kovalama):** $m = 2^p$ register (kova) yapısı ile varyans kontrolü.
- **Leading Zeros (Ardışık Sıfırlar):** Hash değerindeki nadir olayları takip eden register yapısı.
- **Harmonik Ortalama:** Uç değerlerden etkilenmeyen hassas tahmin hesaplama.
- **Düzeltme Faktörleri:** 
  - Küçük veri setleri için *Linear Counting* düzeltmesi.
  - Büyük veri setleri için logaritmik düzeltme.
- **Mergeability (Birleştirilebilirlik):** İki farklı HLL yapısının veri kaybı olmadan bir araya getirilmesi.

## 📊 Teorik Analiz
Algoritmanın standart hata oranı kova sayısına ($m$) bağlıdır:
$$\text{Standart Hata} \approx \frac{1.04}{\sqrt{m}}$$

Örneğin; $p=10$ ($m=1024$) seçildiğinde hata payı yaklaşık **%3.25** olurken, sadece **1 KB** bellek kullanılır.

## 💻 Kurulum ve Çalıştırma
Projeyi yerel makinenizde çalıştırmak için:

1. Repoyu klonlayın:
   ```bash
   git clone https://github.com/kullanici-adiniz/HyperLogLog-Java-Implementation.git

2. Java dosyasını derleyin:   
 ```bash
java HyperLogLog.java
```

3. Çalıştırın:
 ```bash
java HyperLogLog
```

## 📈 Örnek Çıktı
```bash
Gerçek Benzersiz Öğe Sayısı: 10,000
HLL Tahmin Edilen Sayı: 9,842
Hata Oranı: %1.58
```
   
