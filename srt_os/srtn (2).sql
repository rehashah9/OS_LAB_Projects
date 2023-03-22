-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 13, 2023 at 10:15 AM
-- Server version: 10.4.21-MariaDB
-- PHP Version: 7.3.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `srtn`
--

-- --------------------------------------------------------

--
-- Table structure for table `gangtable`
--

CREATE TABLE `gangtable` (
  `process_id` int(5) NOT NULL,
  `executed_time` int(5) NOT NULL,
  `from_t` int(5) NOT NULL,
  `till` int(5) NOT NULL,
  `remaining_time` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `process`
--

CREATE TABLE `process` (
  `process_id` int(5) DEFAULT NULL,
  `process_name` varchar(25) NOT NULL,
  `arrival_time` int(5) NOT NULL,
  `burst_time` int(5) NOT NULL,
  `remaining_time` int(5) NOT NULL,
  `completion_time` int(5) NOT NULL,
  `turnaround_time` int(5) NOT NULL,
  `waiting_time` int(5) NOT NULL,
  `response_time` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
