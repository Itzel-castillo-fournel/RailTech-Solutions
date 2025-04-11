-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3307
-- Généré le : ven. 04 avr. 2025 à 13:03
-- Version du serveur : 11.2.2-MariaDB
-- Version de PHP : 8.2.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `railtechrh`
--

-- --------------------------------------------------------

--
-- Structure de la table `incident`
--

DROP TABLE IF EXISTS `incident`;
CREATE TABLE IF NOT EXISTS `incident` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `typeIncident` enum('PANNE_TECHNIQUE','RETARD_TRAIN','VOIE_ENDOMMAGEE','INCIDENT_A_BORD') DEFAULT NULL,
  `gravite` enum('MINEUR','MODERE','MAJEUR','CRITIQUE') DEFAULT NULL,
  `trainImmat` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `trainImmat` (`trainImmat`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `incident`
--

INSERT INTO `incident` (`id`, `description`, `typeIncident`, `gravite`, `trainImmat`) VALUES
(1, 'Panne de moteur', 'PANNE_TECHNIQUE', 'MAJEUR', 'T12345'),
(2, 'Retard dû à un obstacle sur la voie', 'RETARD_TRAIN', 'MODERE', 'T67890'),
(3, 'test', 'VOIE_ENDOMMAGEE', 'MAJEUR', 'T67890'),
(4, 'test2', 'RETARD_TRAIN', 'MODERE', 'T67890'),
(5, 'test 3', 'RETARD_TRAIN', 'MODERE', 'T67890'),
(6, 'c\'est chaud de fou faut intervenir là', 'INCIDENT_A_BORD', 'MAJEUR', 'T12345');

-- --------------------------------------------------------

--
-- Structure de la table `maintenance`
--

DROP TABLE IF EXISTS `maintenance`;
CREATE TABLE IF NOT EXISTS `maintenance` (
  `dateMaintenance` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `etat` enum('PANNE','MAINTENANCE','OPERATIONNEL') DEFAULT NULL,
  `incidentId` int(11) NOT NULL,
  `technicienId` int(11) DEFAULT NULL,
  PRIMARY KEY (`incidentId`),
  KEY `technicienId` (`technicienId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `maintenance`
--

INSERT INTO `maintenance` (`dateMaintenance`, `description`, `etat`, `incidentId`, `technicienId`) VALUES
('2025-01-22 10:00:00', 'Réparation du moteur', 'MAINTENANCE', 1, 2),
('2025-01-23 09:00:00', 'Vérification de la voie', 'OPERATIONNEL', 2, 2);

-- --------------------------------------------------------

--
-- Structure de la table `notification`
--

DROP TABLE IF EXISTS `notification`;
CREATE TABLE IF NOT EXISTS `notification` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `titre` varchar(100) NOT NULL,
  `utilisateur_id` int(11) DEFAULT NULL,
  `incident_id` int(11) DEFAULT NULL,
  `date` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `utilisateur_id` (`utilisateur_id`),
  KEY `incident_id` (`incident_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `technicien`
--

DROP TABLE IF EXISTS `technicien`;
CREATE TABLE IF NOT EXISTS `technicien` (
  `id` int(11) NOT NULL,
  `specialite` enum('MECANIQUE','ELECTRONIQUE','ELECTRIQUE','INFORMATIQUE','INFRASTRUCTURE') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `technicien`
--

INSERT INTO `technicien` (`id`, `specialite`) VALUES
(2, 'ELECTRONIQUE');

-- --------------------------------------------------------

--
-- Structure de la table `train`
--

DROP TABLE IF EXISTS `train`;
CREATE TABLE IF NOT EXISTS `train` (
  `immatriculation` varchar(50) NOT NULL,
  `marque` varchar(50) DEFAULT NULL,
  `modele` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`immatriculation`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `train`
--

INSERT INTO `train` (`immatriculation`, `marque`, `modele`) VALUES
('T12345', 'Alstom', 'TGV'),
('T67890', 'Siemens', 'ICE');

-- --------------------------------------------------------

--
-- Structure de la table `trajet`
--

DROP TABLE IF EXISTS `trajet`;
CREATE TABLE IF NOT EXISTS `trajet` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `heureDepart` datetime DEFAULT NULL,
  `heureArrivee` datetime DEFAULT NULL,
  `arretDepart` enum('PARIS','LYON','MARSEILLE','BORDEAUX','TOULOUSE','LILLE','NANTES','STRASBOURG','NICE','RENNES','MONTPELLIER') DEFAULT NULL,
  `arretArrivee` enum('PARIS','LYON','MARSEILLE','BORDEAUX','TOULOUSE','LILLE','NANTES','STRASBOURG','NICE','RENNES','MONTPELLIER') DEFAULT NULL,
  `trainImmat` varchar(50) DEFAULT NULL,
  `conducteurId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `trainImmat` (`trainImmat`),
  KEY `conducteurId` (`conducteurId`)
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `trajet`
--

INSERT INTO `trajet` (`id`, `heureDepart`, `heureArrivee`, `arretDepart`, `arretArrivee`, `trainImmat`, `conducteurId`) VALUES
(1, '2025-01-21 09:00:00', '2025-01-21 11:00:00', 'MONTPELLIER', 'STRASBOURG', 'T12345', 3),
(2, '2025-01-21 14:00:00', '2025-01-21 16:00:00', 'LYON', 'MARSEILLE', 'T67890', 3),
(3, '2025-02-27 12:35:25', '2025-02-27 14:35:25', 'PARIS', 'LYON', 'T12345', 3),
(4, '2025-03-01 11:45:00', '2025-03-01 16:00:00', 'BORDEAUX', 'LYON', 'T67890', NULL),
(5, '2025-03-01 11:45:00', '2025-03-01 16:00:00', 'PARIS', 'LYON', 'T67890', NULL),
(6, '2025-03-01 11:45:00', '2025-03-01 16:00:00', 'PARIS', 'LYON', 'T67890', NULL),
(7, '2025-03-03 00:15:00', '2025-03-03 04:30:00', 'MARSEILLE', 'STRASBOURG', 'T12345', 0),
(8, '2025-03-03 00:00:00', '2025-03-03 00:00:00', 'LYON', 'NANTES', 'T67890', 0),
(9, '2025-03-03 00:00:00', '2025-03-03 00:00:00', 'BORDEAUX', 'MARSEILLE', 'T67890', NULL),
(10, '2025-03-03 00:00:00', '2025-03-03 00:00:00', 'PARIS', 'BORDEAUX', 'T67890', 0),
(11, '2025-03-03 00:00:00', '2025-03-03 00:00:00', 'PARIS', 'PARIS', 'T12345', 3),
(12, '2025-03-04 00:00:00', '2025-03-04 00:00:00', 'PARIS', 'LYON', 'T12345', 3),
(13, '2025-03-07 00:00:00', '2025-03-07 00:00:00', 'PARIS', 'PARIS', 'T12345', NULL);

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

DROP TABLE IF EXISTS `utilisateur`;
CREATE TABLE IF NOT EXISTS `utilisateur` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nom` varchar(50) DEFAULT NULL,
  `prenom` varchar(50) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `mdp` varchar(255) DEFAULT NULL,
  `role` enum('ADMIN','TECHNICIEN','CONDUCTEUR','OPERATEUR') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `utilisateur`
--

INSERT INTO `utilisateur` (`id`, `nom`, `prenom`, `email`, `mdp`, `role`) VALUES
(2, 'Martin', 'Lucie', 'lucie.martin@example.com', 'test1', 'ADMIN'),
(3, 'Durand', 'Pierre', 'pierre.durand@example.com', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'CONDUCTEUR'),
(4, 'Bernard', 'Marie', 'marie.bernard@example.com', 'mdp4', 'OPERATEUR'),
(7, 'Test', 'User', 'test.user@example.com', 'password123', 'TECHNICIEN'),
(9, 'test', 'test', 'test@test.test', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'ADMIN');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
