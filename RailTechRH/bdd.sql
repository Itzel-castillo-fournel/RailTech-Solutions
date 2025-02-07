-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3307
-- Généré le : ven. 07 fév. 2025 à 15:44
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
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `incident`
--

INSERT INTO `incident` (`id`, `description`, `typeIncident`, `gravite`, `trainImmat`) VALUES
(1, 'Panne de moteur', 'PANNE_TECHNIQUE', 'MAJEUR', 'T12345'),
(2, 'Retard dû à un obstacle sur la voie', 'RETARD_TRAIN', 'MODERE', 'T67890');

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
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `trajet`
--

INSERT INTO `trajet` (`id`, `heureDepart`, `heureArrivee`, `arretDepart`, `arretArrivee`, `trainImmat`, `conducteurId`) VALUES
(1, '2025-01-21 08:00:00', '2025-01-21 10:00:00', 'PARIS', 'LYON', 'T12345', 3),
(2, '2025-01-21 14:00:00', '2025-01-21 16:00:00', 'LYON', 'MARSEILLE', 'T67890', 3);

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
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `utilisateur`
--

INSERT INTO `utilisateur` (`id`, `nom`, `prenom`, `email`, `mdp`, `role`) VALUES
(1, 'Dupont', 'Jean', 'jean.dupont@example.com', 'mdp1', 'ADMIN'),
(2, 'Martin', 'Lucie', 'lucie.martin@example.com', 'mdp2', 'TECHNICIEN'),
(3, 'Durand', 'Pierre', 'pierre.durand@example.com', 'mdp3', 'CONDUCTEUR'),
(4, 'Bernard', 'Marie', 'marie.bernard@example.com', 'mdp4', 'OPERATEUR');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
