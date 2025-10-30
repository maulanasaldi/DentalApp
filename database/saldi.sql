-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: saldi
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `basis_pengetahuan`
--

DROP TABLE IF EXISTS `basis_pengetahuan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `basis_pengetahuan` (
  `id_aturan` int NOT NULL AUTO_INCREMENT,
  `id_penyakit` int NOT NULL,
  `id_gejala` int NOT NULL,
  PRIMARY KEY (`id_aturan`),
  KEY `id_penyakit` (`id_penyakit`),
  KEY `id_gejala` (`id_gejala`),
  CONSTRAINT `basis_pengetahuan_ibfk_1` FOREIGN KEY (`id_penyakit`) REFERENCES `penyakit` (`id_penyakit`),
  CONSTRAINT `basis_pengetahuan_ibfk_2` FOREIGN KEY (`id_gejala`) REFERENCES `gejala` (`id_gejala`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `basis_pengetahuan`
--

LOCK TABLES `basis_pengetahuan` WRITE;
/*!40000 ALTER TABLE `basis_pengetahuan` DISABLE KEYS */;
INSERT INTO `basis_pengetahuan` VALUES (1,1,1),(2,1,2),(3,1,3),(4,2,4),(5,2,5),(6,2,6),(7,2,7),(8,3,8),(9,3,9),(10,3,10),(11,3,11),(12,4,12),(13,4,13),(14,4,14),(15,4,15),(16,5,16),(17,5,17),(18,5,18),(19,5,19),(20,6,20),(21,6,21),(22,6,22),(23,6,23),(24,7,24),(25,7,25),(26,7,26),(27,8,27),(28,8,28),(29,8,29),(30,8,30),(31,9,31),(32,9,32),(33,9,33),(34,9,34),(35,10,35),(36,10,36),(37,10,37),(38,11,38),(39,11,39),(40,11,40),(41,12,41),(42,12,42),(43,12,43),(44,13,44),(45,13,45),(46,13,46),(47,14,47),(48,14,48),(49,14,49),(50,14,49),(51,15,50),(52,15,51),(53,15,52),(54,16,53),(55,16,54),(56,16,55);
/*!40000 ALTER TABLE `basis_pengetahuan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gejala`
--

DROP TABLE IF EXISTS `gejala`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gejala` (
  `id_gejala` int NOT NULL AUTO_INCREMENT,
  `gejala` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id_gejala`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gejala`
--

LOCK TABLES `gejala` WRITE;
/*!40000 ALTER TABLE `gejala` DISABLE KEYS */;
INSERT INTO `gejala` VALUES (1,'Bercak putih atau bercak cokelat pada permukaan gigi, tanda awal demineralisasi enamel.'),(2,'Gigi menjadi sensitif dan nyeri terhadap makanan/minuman dingin, panas, atau manis.'),(3,'Nyeri hebat dan dapat membentuk abses (nanah) akibat karies mencapai pulpa.'),(4,'Gusi membengkak, kemerahan atau keunguan, mudah berdarah terutama saat menyikat gigi atau membersihkan sela gigi (flossing).'),(5,'Gusi terasa nyeri dan terkadang mengeluarkan nanah ringan.'),(6,'Napas berbau tidak sedap (halitosis) akibat akumulasi plak bakteri.'),(7,'Gusi mulai menyusut (akar gigi terlihat) dan gigi mulai goyang pada kasus lanjut.'),(8,'Gejala berkembang dari gingivitis yang tidak terobati. Gusi sangat bengkak, merah hingga keunguan, mudah berdarah, dan terasa nyeri.'),(9,'Gusi mulai menyusut, terbentuk kantong periodontal di antara gigi dan gusi sehingga gigi tampak lebih panjang dan renggang.'),(10,'Nyeri saat mengunyah, gigi menjadi sensitif dan mudah goyang.'),(11,'Napas berbau tidak sedap karena infeksi kronis.'),(12,'Muncul bercak putih tebal seperti kapas di lidah, gusi, langit-langit mulut atau pipi bagian dalam.'),(13,'Bercak bisa berdarah bila dikerok dan menimbulkan rasa perih atau terbakar di mulut.'),(14,'Kesulitan menelan, lidah terasa aneh (lemah mengecap), dan mulut terasa kering atau tidak nyaman.'),(15,'Pada pengguna gigi palsu, timbul stomatitis (gusi kemerahan dan nyeri di balik gigi palsu).'),(16,'Luka borok (ulser) oval atau bulat di dalam mulut (dinding pipi, bibir bagian dalam, lidah), berwarna putih atau kuning di tengah dengan tepi berwarna merah.'),(17,'Luka menimbulkan nyeri terutama saat makan atau minum.'),(18,'Bisa timbul satu atau beberapa sekaligus.'),(19,'Sering tidak disertai demam atau gejala sistemik.'),(20,'Muncul lepuhan kecil berisi cairan di sekitar bibir atau mulut (cold sores), sering diawali rasa gatal atau kesemutan sebelum luka muncul.'),(21,'Setelah pecah, luka terasa nyeri.'),(22,'Dapat disertai demam ringan, nyeri otot/kepala, sakit tenggorokan, dan kelenjar getah bening membengkak di daerah wajah/leher.'),(23,'Herpes mulut sangat menular melalui kontak langsung (misalnya berciuman atau berbagi alat makan).'),(24,'Tercium aroma tidak sedap dari mulut (seperti bau busuk, amis, atau asam) setiap kali berbicara atau menghembuskan napas.'),(25,'Sering disertai mulut terasa kering, lidah berlapis putih atau kuning, serta kemungkinan gigi berlubang atau radang gusi kronis mendasari.'),(26,'Penderita biasanya sadar setelah diberi tahu orang lain.'),(27,'Perasaan mulut dan lidah sangat kering, kadang lengket, disertai haus terus-menerus.'),(28,'Suara bisa menjadi serak, dan makanan atau obat susah ditelan.'),(29,'Lidah sering tampak berlapis putih atau kasar, dan terjadi kesulitan mengecap (terasa pahit).'),(30,'Mulut kering dapat disebabkan oleh obat-obatan, dehidrasi, atau sindrom penyakit (misal Sjögren).'),(31,'Nyeri di daerah sendi rahang (dekat telinga) yang bertambah saat membuka mulut lebar, mengunyah, atau menguap.'),(32,'Terdengar suara “klik” atau gesekan ketika membuka/tutup mulut. Rahang terasa kaku dan sulit digerakkan penuh.'),(33,'Sering disertai nyeri kepala (migren), nyeri leher atau bahu, tinnitus ringan (telinga berdengung), dan kadang rasa sakit di telinga tanpa infeksi.'),(34,'Gangguan ini bisa dipicu stres atau kebiasaan menggemeretakkan gigi (bruxism).'),(35,'Sariawan atau luka pada mulut yang tidak kunjung sembuh, muncul bercak putih atau merah pada mukosa mulut, dan rasa nyeri di dalam mulut.'),(36,'Timbul benjolan atau penebalan pada dinding mulut, langit-langit, atau gusi yang sulit hilang.'),(37,'Gigi goyang tanpa sebab, kesulitan mengunyah atau menelan, dan nyeri menjalar ke rahang atau telinga.'),(38,'Terlihat bercak putih keabuan pada lidah, pipi bagian dalam, langit-langit, atau gusi yang tidak hilang meski dibersihkan.'),(39,'Bercak ini bertekstur tebal dan keras bila diraba, kadang disertai bercak merah menonjol yang bisa menjadi tanda awal kanker.'),(40,'Umumnya tidak menimbulkan nyeri, tapi daerah bercak bisa sensitif terhadap makanan panas, pedas, atau sentuhan.'),(41,'Muncul bercak putih halus berbentuk jaring (Wickham striae) pada mukosa mulut, terutama di lidah dan pipi dalam, yang dapat menimbulkan sensasi terbakar atau nyeri.'),(42,'Area yang terkena seringkali tampak kering dan berlekuk-lekuk karena peradangan.'),(43,'Penderita mungkin merasakan mulut pahit atau ada rasa tidak nyaman saat makan.'),(44,'Nyeri gigi hebat yang memburuk saat mengunyah atau menekan gigi, sering menjalar ke rahang, telinga, atau leher.'),(45,'Terdapat pembengkakan dan kemerahan pada gusi atau wajah di sekitar gigi yang terinfeksi, kadang disertai demam dan pembengkakan kelenjar getah bening leher.'),(46,'Penderita mungkin sensitif terhadap rangsang suhu (panas/dingin) dan mengalami bau mulut tak sedap.'),(47,'Lidah tampak bengkak, kemerahan, dan licin (hilangnya papila), sering disertai rasa sakit atau terbakar.'),(48,'Permukaan lidah dapat retak atau berlubang, bahkan muncul lepuhan atau plak.'),(49,'Perubahan bentuk lidah dapat menyebabkan kesulitan bicara, mengunyah, atau menelan.'),(50,'Muncul luka atau retakan di satu atau kedua sudut bibir yang tampak kemerahan, pecah-pecah, dan terkadang berdarah.'),(51,'Area tersebut dapat membengkak dan mengeras, serta menimbulkan rasa nyeri atau gatal yang mengganggu.'),(52,'Kulit di ujung bibir terlihat teriritasi dan kering.'),(53,'Sensasi terbakar atau panas terutama pada lidah, tetapi juga dapat mengenai langit-langit mulut, bibir, gusi, dan seluruh rongga mulut.'),(54,'Penderita sering merasakan mulut kering dan cepat haus, serta timbul perubahan rasa (misal rasa pahit, logam).'),(55,'Terkadang muncul rasa kesemutan, menyengat, atau mati rasa di permukaan lidah dan mukosa mulut. Gejala biasanya bervariasi dari ringan hingga sangat mengganggu dan dapat memburuk seiring hari.');
/*!40000 ALTER TABLE `gejala` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pasien`
--

DROP TABLE IF EXISTS `pasien`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pasien` (
  `id_pasien` int NOT NULL AUTO_INCREMENT,
  `nama_pasien` varchar(100) NOT NULL,
  `tgl_lahir` date DEFAULT NULL,
  `nik` varchar(16) DEFAULT NULL,
  `pekerjaan` varchar(50) DEFAULT NULL,
  `no_tlpn_pasien` varchar(20) DEFAULT NULL,
  `alamat_pasien` varchar(255) DEFAULT NULL,
  `tgl_pendaftaran` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `status_notif` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id_pasien`),
  UNIQUE KEY `nik` (`nik`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pasien`
--

LOCK TABLES `pasien` WRITE;
/*!40000 ALTER TABLE `pasien` DISABLE KEYS */;
INSERT INTO `pasien` VALUES (59,'Andi Bocah','2015-07-01','3271011500000001','Pelajar','081234000001','Jakarta','2025-07-01 08:20:18',0),(60,'Budi Remaja','2010-05-10','3271011500000002','Pelajar','081234000002','Bandung','2025-07-01 08:20:18',0),(61,'Citra Dewasa Muda','1998-11-22','3271011500000003','Mahasiswa','081234000003','Surabaya','2025-07-01 08:20:18',0),(62,'Dina Muda','2000-02-14','3271011500000004','Karyawan','081234000004','Yogyakarta','2025-07-01 08:20:18',0),(63,'Eko Dewasa','1980-06-05','3271011500000005','Wiraswasta','081234000005','Semarang','2025-07-01 08:20:18',0),(64,'Fajar Dewasa','1975-12-09','3271011500000006','Petani','081234000006','Bali','2025-07-01 08:20:18',0),(65,'Gita Lansia','1960-08-30','3271011500000007','Pensiunan','081234000007','Depok','2025-07-01 08:20:18',0),(66,'Hendra Tua','1955-04-18','3271011500000008','Pensiunan','081234000008','Makassar','2025-07-01 08:20:18',0),(67,'Ika Umum','1990-01-01','3271011500000009','Ibu Rumah Tangga','081234000009','Padang','2025-07-01 08:20:18',0),(68,'Joko Kecil','2013-03-20','3271011500000010','Pelajar','081234000010','Medan','2025-07-01 08:20:18',0);
/*!40000 ALTER TABLE `pasien` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pendaftaran`
--

DROP TABLE IF EXISTS `pendaftaran`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pendaftaran` (
  `id_pendaftaran` int NOT NULL AUTO_INCREMENT,
  `id_pasien` int NOT NULL,
  `tanggal_daftar` datetime DEFAULT CURRENT_TIMESTAMP,
  `status_diagnosa` enum('BELUM','SUDAH') DEFAULT 'BELUM',
  `status_notif` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id_pendaftaran`),
  KEY `id_pasien` (`id_pasien`),
  CONSTRAINT `pendaftaran_ibfk_1` FOREIGN KEY (`id_pasien`) REFERENCES `pasien` (`id_pasien`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pendaftaran`
--

LOCK TABLES `pendaftaran` WRITE;
/*!40000 ALTER TABLE `pendaftaran` DISABLE KEYS */;
INSERT INTO `pendaftaran` VALUES (57,59,'2025-07-01 15:22:39','SUDAH',0),(58,60,'2025-07-01 15:22:39','SUDAH',0),(59,61,'2025-07-01 15:22:39','SUDAH',0),(60,62,'2025-07-01 15:22:39','BELUM',1),(61,63,'2025-07-01 15:22:39','SUDAH',0),(62,64,'2025-07-01 15:22:39','SUDAH',1),(63,65,'2025-07-01 15:22:39','SUDAH',0),(64,66,'2025-07-01 15:22:39','SUDAH',1),(65,67,'2025-07-01 15:22:39','SUDAH',1),(66,68,'2025-07-01 15:22:39','SUDAH',0);
/*!40000 ALTER TABLE `pendaftaran` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `penyakit`
--

DROP TABLE IF EXISTS `penyakit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `penyakit` (
  `id_penyakit` int NOT NULL AUTO_INCREMENT,
  `nama_penyakit` varchar(100) NOT NULL,
  `solusi` text,
  PRIMARY KEY (`id_penyakit`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `penyakit`
--

LOCK TABLES `penyakit` WRITE;
/*!40000 ALTER TABLE `penyakit` DISABLE KEYS */;
INSERT INTO `penyakit` VALUES (1,'Karies Gigi (Gigi Berlubang)','Membersihkan lubang gigi dan melakukan penambalan (restorasi) dengan bahan komposit yang sesuai warna gigi.\nJika kerusakan sudah parah, perawatan saluran akar (root canal) atau pencabutan gigi mungkin diperlukan untuk menghentikan infeksi. \nPencegahan penting dilakukan dengan kebersihan mulut rutin: menyikat gigi 2× sehari dengan pasta berfluoride, flossing, dan kontrol ke dokter gigi setiap 6 bulan untuk membersihkan plak/karang.'),(2,'Radang Gusi (Gingivitis)','Pembersihan profesional (scaling) dan perawatan akar (root planing) untuk menghilangkan plak dan karang gigi. Dokter gigi juga akan menambal atau menggantikan gigi yang rusak jika diperlukan. Selain itu, tingkatkan kebersihan mulut: sikat gigi dua kali sehari (pasta berfluoride), flossing sehari sekali, dan berkumur dengan obat kumur antiseptik sesuai anjuran. Hindari merokok dan kurangi konsumsi gula.'),(3,'Periodontitis (Penyakit Gusi Lanjutan)','Perawatan tahap awal meliputi scaling subgingiva (membersihkan plak/karang di bawah gusi) dan pemberian antibiotik (oral atau gel) untuk mengatasi infeksi bakteri di kantong periodontal. Gigi yang parah terinfeksi mungkin dicabut agar infeksi tidak menyebar. Pada kasus berat, dokter dapat melakukan bedah flap (pembersihan kantong gusi), graft jaringan lunak/tulang, atau pemasangan splint (penyangga gigi). Pencegahan dengan kebersihan mulut yang baik sangat penting.'),(4,'Kandidiasis Oral (Sariawan Jamur)','Terapi utama adalah obat antijamur: misalnya nystatin, clotrimazole, miconazole (gel oral), atau fluconazole sesuai resep dokter. Selain itu, jaga kebersihan mulut dengan baik, dan lakukan terapi pendukung rumahan, seperti berkumur air garam hangat atau larutan baking soda untuk mengurangi infeksi jamur. Istirahatkan mulut, konsumsi makanan lunak, dan bila perlu, ganti gigi palsu secara teratur.'),(5,'Sariawan (Aphthous Stomatitis)','Umumnya tidak memerlukan pengobatan khusus karena akan sembuh sendiri dalam 1–2 minggu. Untuk mengurangi nyeri, lakukan kompres es atau berkumur dengan obat kumur antiseptik (misal silver nitrate). Hindari makanan pedas atau asam yang mengiritasi luka. Jaga kebersihan mulut dan konsumsi makanan bergizi untuk mempercepat penyembuhan.'),(6,'Herpes Oral (Herpes Labialis)','Obat antivirus seperti acyclovir, famciclovir, atau valacyclovir diresepkan dokter untuk mempercepat penyembuhan dan mengurangi penularan. Selain itu, kompres es atau kompres dingin di area luka dan obat pereda nyeri (misal parasetamol) membantu meredakan sakit. Jaga kebersihan luka dan hindari menyentuh atau menjilatnya. Kelompok risiko (anak-anak, penderita imunosupresi) harus ekstra hati-hati.'),(7,'Bau Mulut (Halitosis)','Tingkatkan kebersihan mulut: sikat gigi dan lidah minimal 2 kali sehari, gunakan benang gigi untuk bersihkan sela-sela gigi, dan kumur dengan obat kumur antibakteri (seperti listerine). Perbaiki gaya hidup: berhenti merokok, banyak minum air putih, serta batasi konsumsi makanan penyebab bau kuat (bawang, alkohol). Jika penyebabnya masalah gigi/gusi (misalnya gigi berlubang atau plak tebal), segera periksakan ke dokter untuk penambalan, pencabutan, atau scaling. Jika disebabkan oleh masalah lain (sinusitis, GERD), obati penyakit tersebut sesuai anjuran dokter.'),(8,'Mulut Kering (Xerostomia)','Tingkatkan hidrasi: minum air putih sering dan kunyah permen karet bebas gula atau hisap permen keras (mint) untuk merangsang produksi air liur. Hindari alkohol dan merokok. Jika penyebabnya obat tertentu, bicarakan ke dokter untuk mengurangi dosis atau mengganti obat tersebut. Dokter dapat meresepkan saliva buatan (obat kumur khusus berenzim) atau obat stimulasi saliva (pilocarpine, cevimeline) bila perlu. Menjaga kebersihan mulut juga mencegah karies akibat mulut kering.'),(9,'Gangguan Sendi Rahang (Temporomandibular Disorder – TMD)','Istirahatkan rahang: hindari mengunyah makanan keras atau permen karet, dan lakukan kompres hangat di otot rahang. Dokter dapat meresepkan obat pereda nyeri (parasetamol, NSAID) dan pelemas otot (muscle relaxant) untuk mengurangi nyeri otot rahang. Terapi fisik sederhana (latihan relaksasi rahang, pijat otot wajah) bermanfaat. Pada kasus kronis atau akibat bruxism, dokter gigi mungkin membuat splint gigi malam (night guard) untuk melindungi sendi. Penanganan stres dan koreksi gigi (jika maloklusi) juga dapat direkomendasikan.'),(10,'Kanker Mulut','Penanganan tergantung stadium kanker, meliputi tindakan bedah (operasi pengangkatan tumor), radioterapi, kemoterapi, atau terapi target. Selain itu penderita perlu dukungan nutrisi dan perawatan paliatif untuk meredakan gejala serta memantau perkembangan penyakit secara berkala.'),(11,'Leukoplakia','Hilangkan faktor iritasi penyebabnya, misalnya perbaiki gigi patah atau tajam dan hentikan merokok. Bila bercak putih tidak menghilang atau terdapat sel prakanker, dokter akan melakukan pengangkatan dengan operasi, laser, atau terapi pembekuan (krioterapi). Untuk leukoplakia jenis rambut (terkait infeksi), dapat diberikan obat antivirus serta salep asam retinoat untuk memperkecil bercak. Pemeriksaan rutin ke dokter gigi dianjurkan untuk memantau kondisi ini.'),(12,'Lichen Planus (Oral)','Terapi bertujuan meredakan peradangan dan gejala. Dokter biasanya meresepkan kortikosteroid topikal (salep) atau oral untuk mengurangi inflamasi serta imunosupresan oles jika diperlukan. Antihistamin dapat diberikan untuk mengurangi rasa gatal. Jika timbul infeksi sekunder, berikan antibiotik atau antijamur sesuai kebutuhan. Pasien disarankan menjaga kebersihan mulut, menghindari makanan pedas/asam dan rokok, serta mengganti gigi palsu atau tambalan emas/amalgam yang dicurigai sebagai pemicu.'),(13,'Abses Gigi','Dokter gigi akan melakukan insisi dan drainase untuk mengeluarkan nanah dari abses. Pemberian antibiotik diperlukan jika infeksi sudah meluas. Selanjutnya, lakukan perawatan saluran akar (root canal) untuk membersihkan infeksi pada akar gigi dan menutup kembali saluran akar. Jika gigi sudah rusak parah dan tidak dapat diselamatkan, gigi tersebut akan dicabut. Pengelolaan nyeri dan perawatan pendukung juga penting selama proses penyembuhan.'),(14,'Glositis (Radang Lidah)','Sesuaikan dengan penyebab. Bila disebabkan anemia atau defisiensi vitamin (terutama B12/zat besi), berikan suplemen zat besi dan vitamin B kompleks serta konsumsi makanan kaya nutrisi. Jika ada infeksi bakteri/jamur, berikan antibiotik atau antijamur. Kortikosteroid topikal juga dapat diresepkan untuk meredakan kemerahan dan nyeri lidah. Jaga kebersihan mulut dengan menyikat gigi dan membersihkan lidah secara teratur untuk mendukung penyembuhan.'),(15,'Cheilitis Angular (Radang Sudut Bibir)','Sesuaikan dengan penyebab infeksi. Jika bakteri, oleskan salep antibiotik (misal mupirocin atau asam fusidat) pada sudut bibir. Jika jamur (sering Candida), gunakan krim antijamur (misal nystatin, klotrimazol, atau miconazole). Perbaiki faktor pemicu lokal, misalnya rapikan gigi yang tajam atau benjol, dan pasang/memperbaiki gigi palsu yang longgar. Tingkatkan asupan nutrisi (vitamin B, zat besi, asam folat) jika defisiensi berperan, serta oleskan petroleum jelly untuk melembapkan dan melindungi kulit bibir.'),(16,'Sindrom Mulut Terbakar','Jika tidak ditemukan penyebab spesifik (idiopatik), penatalaksanaan difokuskan pada mengurangi gejala dan memodifikasi gaya hidup. Langkah yang dianjurkan antara lain olahraga teratur, mengurangi stres, menghindari makanan/minuman pedas dan asam, mengganti pasta gigi atau obat kumur yang mengandung alkohol, serta menjaga hidrasi tubuh yang cukup. Untuk jenis sekunder (bila disebabkan kondisi medis lain seperti xerostomia, refluks, atau defisiensi nutrisi), pengobatan ditujukan pada kondisi penyebabnya (misal suplemen vitamin, perawatan GERD, mengatasi kering mulut). Konsultasi ke dokter gigi atau spesialis disarankan agar penanganan disesuaikan dengan penyebab yang mendasari.');
/*!40000 ALTER TABLE `penyakit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `riwayat_diagnosa`
--

DROP TABLE IF EXISTS `riwayat_diagnosa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `riwayat_diagnosa` (
  `id_riwayat_diagnosa` int NOT NULL AUTO_INCREMENT,
  `id_pasien` int NOT NULL,
  `id_user` int NOT NULL,
  `tgl_diagnosa` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_riwayat_diagnosa`),
  KEY `id_pasien` (`id_pasien`),
  KEY `fk_id_user` (`id_user`),
  CONSTRAINT `fk_id_user` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`),
  CONSTRAINT `riwayat_diagnosa_ibfk_1` FOREIGN KEY (`id_pasien`) REFERENCES `pasien` (`id_pasien`)
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `riwayat_diagnosa`
--

LOCK TABLES `riwayat_diagnosa` WRITE;
/*!40000 ALTER TABLE `riwayat_diagnosa` DISABLE KEYS */;
INSERT INTO `riwayat_diagnosa` VALUES (83,64,1,'2025-07-01 08:24:02'),(84,66,1,'2025-07-01 08:24:15'),(85,67,1,'2025-07-01 08:24:27');
/*!40000 ALTER TABLE `riwayat_diagnosa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `riwayat_gejala`
--

DROP TABLE IF EXISTS `riwayat_gejala`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `riwayat_gejala` (
  `id_riwayat_gejala` int NOT NULL AUTO_INCREMENT,
  `id_riwayat_diagnosa` int NOT NULL,
  `id_gejala` int NOT NULL,
  PRIMARY KEY (`id_riwayat_gejala`),
  KEY `id_riwayat_diagnosa` (`id_riwayat_diagnosa`),
  KEY `id_gejala` (`id_gejala`),
  CONSTRAINT `riwayat_gejala_ibfk_1` FOREIGN KEY (`id_riwayat_diagnosa`) REFERENCES `riwayat_diagnosa` (`id_riwayat_diagnosa`),
  CONSTRAINT `riwayat_gejala_ibfk_2` FOREIGN KEY (`id_gejala`) REFERENCES `gejala` (`id_gejala`)
) ENGINE=InnoDB AUTO_INCREMENT=205 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `riwayat_gejala`
--

LOCK TABLES `riwayat_gejala` WRITE;
/*!40000 ALTER TABLE `riwayat_gejala` DISABLE KEYS */;
INSERT INTO `riwayat_gejala` VALUES (202,83,5),(203,84,12),(204,85,23);
/*!40000 ALTER TABLE `riwayat_gejala` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `riwayat_penyakit`
--

DROP TABLE IF EXISTS `riwayat_penyakit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `riwayat_penyakit` (
  `id_riwayat_penyakit` int NOT NULL AUTO_INCREMENT,
  `id_riwayat_diagnosa` int NOT NULL,
  `id_penyakit` int NOT NULL,
  PRIMARY KEY (`id_riwayat_penyakit`),
  KEY `id_riwayat_diagnosa` (`id_riwayat_diagnosa`),
  KEY `id_penyakit` (`id_penyakit`),
  CONSTRAINT `riwayat_penyakit_ibfk_1` FOREIGN KEY (`id_riwayat_diagnosa`) REFERENCES `riwayat_diagnosa` (`id_riwayat_diagnosa`) ON DELETE CASCADE,
  CONSTRAINT `riwayat_penyakit_ibfk_2` FOREIGN KEY (`id_penyakit`) REFERENCES `penyakit` (`id_penyakit`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `riwayat_penyakit`
--

LOCK TABLES `riwayat_penyakit` WRITE;
/*!40000 ALTER TABLE `riwayat_penyakit` DISABLE KEYS */;
INSERT INTO `riwayat_penyakit` VALUES (65,83,2),(66,84,4),(67,85,6);
/*!40000 ALTER TABLE `riwayat_penyakit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id_user` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nama_lengkap` varchar(100) NOT NULL,
  `role` enum('resepsionis','dokter') NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `foto` varchar(255) DEFAULT NULL,
  `no_tlp` varchar(20) DEFAULT NULL,
  `hari_praktik` varchar(50) DEFAULT NULL,
  `jam_praktik` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_user`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'maulanasaldi','maulana22','M. Maulana Saldi','dokter','2025-05-11 09:51:58','C:\\Users\\mmaul\\Downloads\\saldi.jpeg','088809155503','Senin-Selasa','15:00 - 20:00'),(2,'maolida','maolida22','Ahsanti Maolida','resepsionis','2025-06-13 08:25:49','C:\\Users\\mmaul\\Downloads\\cewek formal.jpg','085274620193','Jum\'at - Sabtu','18:00 - 20:00');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-10  7:58:23
