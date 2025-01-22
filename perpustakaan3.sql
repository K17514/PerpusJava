-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 22, 2025 at 02:08 AM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 7.4.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `perpustakaan3`
--

-- --------------------------------------------------------

--
-- Table structure for table `buku`
--

CREATE TABLE `buku` (
  `id_buku` int(30) NOT NULL,
  `judul` varchar(30) NOT NULL,
  `pengarang` varchar(30) NOT NULL,
  `penerbit` varchar(30) NOT NULL,
  `kategori` varchar(30) NOT NULL,
  `deskripsi` text NOT NULL,
  `stok` int(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `buku`
--

INSERT INTO `buku` (`id_buku`, `judul`, `pengarang`, `penerbit`, `kategori`, `deskripsi`, `stok`) VALUES
(7, 'Atomic Habits', 'James Clear', 'Gramedia', 'Psikologi', 'Perubahan kecil memberikan hasil yang luar biasa', 6),
(8, 'Look Back', 'Tatsuki Fujimoto', 'Gramedia', 'Komik', 'Two artist searching life', 7),
(9, 'Project Owari', 'Ryukusune', 'Gramedia', 'komik', 'One wrong step and it is the end', 5),
(10, 'Laskar Pelangi', 'Ndini', 'Gramedia', 'Novel', 'yay', 7);

-- --------------------------------------------------------

--
-- Table structure for table `peminjaman`
--

CREATE TABLE `peminjaman` (
  `id_peminjaman` int(11) NOT NULL,
  `id_siswa` int(11) NOT NULL,
  `id_buku` int(11) NOT NULL,
  `tanggal_pinjam` date NOT NULL,
  `tanggal_balik` date DEFAULT NULL,
  `status` enum('Sedang Meminjam','Sudah Dibalikan') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `peminjaman`
--

INSERT INTO `peminjaman` (`id_peminjaman`, `id_siswa`, `id_buku`, `tanggal_pinjam`, `tanggal_balik`, `status`) VALUES
(2, 4, 7, '2025-01-13', '2025-01-16', 'Sudah Dibalikan'),
(4, 8, 9, '1970-01-01', '2025-01-15', 'Sudah Dibalikan'),
(5, 7, 8, '2025-01-15', '2025-01-16', 'Sudah Dibalikan'),
(6, 4, 7, '2025-01-13', '2025-01-15', 'Sudah Dibalikan'),
(9, 8, 8, '2025-01-15', '2025-01-16', 'Sudah Dibalikan'),
(10, 8, 9, '2025-01-14', '2025-01-16', 'Sudah Dibalikan'),
(11, 9, 10, '2025-01-15', '2025-01-16', 'Sudah Dibalikan'),
(12, 6, 8, '2025-01-16', '2025-01-16', 'Sudah Dibalikan'),
(13, 7, 7, '2025-01-16', NULL, 'Sedang Meminjam'),
(14, 5, 9, '2025-01-19', '2025-01-19', 'Sudah Dibalikan');

--
-- Triggers `peminjaman`
--
DELIMITER $$
CREATE TRIGGER `balik` AFTER UPDATE ON `peminjaman` FOR EACH ROW UPDATE buku
SET stok = stok + 1
WHERE id_buku = id_buku
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `pinjam` AFTER INSERT ON `peminjaman` FOR EACH ROW UPDATE buku
SET stok = stok - 1
WHERE id_buku = new.id_buku
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `siswa`
--

CREATE TABLE `siswa` (
  `id_siswa` int(11) NOT NULL,
  `nama` varchar(60) NOT NULL,
  `nis` varchar(16) NOT NULL,
  `kelas` varchar(10) NOT NULL,
  `jenis_kelamin` enum('perempuan','laki-laki') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `siswa`
--

INSERT INTO `siswa` (`id_siswa`, `nama`, `nis`, `kelas`, `jenis_kelamin`) VALUES
(4, 'Saltycia Chairika', '20161087', 'XII AKL ', 'perempuan'),
(5, 'Melna Caintan', '20161037', 'XII RPL A', 'perempuan'),
(6, 'Engeline Chairine', '23161014', 'XI RPL B', 'laki-laki'),
(7, 'Steven Chin', '23161017', 'XI RPL B', 'laki-laki'),
(8, 'Chelsica', '23161024', 'XI RPL B', 'perempuan'),
(9, 'Sim Chiuw Shia', '23160212', 'XI RPL A', 'perempuan');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id_user` int(11) NOT NULL,
  `nama` varchar(60) NOT NULL,
  `username` varchar(60) NOT NULL,
  `password` varchar(60) NOT NULL,
  `level` enum('1','2') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id_user`, `nama`, `username`, `password`, `level`) VALUES
(1, 'Serapeum', 'Sera', 'Sera', '2'),
(2, 'Thelema', 'Thelema', 'Thelema', '1');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `buku`
--
ALTER TABLE `buku`
  ADD PRIMARY KEY (`id_buku`);

--
-- Indexes for table `peminjaman`
--
ALTER TABLE `peminjaman`
  ADD PRIMARY KEY (`id_peminjaman`);

--
-- Indexes for table `siswa`
--
ALTER TABLE `siswa`
  ADD PRIMARY KEY (`id_siswa`),
  ADD UNIQUE KEY `nis` (`nis`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id_user`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `buku`
--
ALTER TABLE `buku`
  MODIFY `id_buku` int(30) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `peminjaman`
--
ALTER TABLE `peminjaman`
  MODIFY `id_peminjaman` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `siswa`
--
ALTER TABLE `siswa`
  MODIFY `id_siswa` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id_user` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
