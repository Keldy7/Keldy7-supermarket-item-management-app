-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : dim. 08 mai 2022 à 04:41
-- Version du serveur : 5.7.36
-- Version de PHP : 7.4.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `gestion_sup`
--

-- --------------------------------------------------------

--
-- Structure de la table `acheter`
--

DROP TABLE IF EXISTS `acheter`;
CREATE TABLE IF NOT EXISTS `acheter` (
  `id_Article` int(11) NOT NULL,
  `id_Client` int(11) NOT NULL,
  KEY `id_Article` (`id_Article`),
  KEY `id_Client` (`id_Client`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `acheter`
--

INSERT INTO `acheter` (`id_Article`, `id_Client`) VALUES
(7, 3),
(3, 4);

-- --------------------------------------------------------

--
-- Structure de la table `articles`
--

DROP TABLE IF EXISTS `articles`;
CREATE TABLE IF NOT EXISTS `articles` (
  `id_Article` int(11) NOT NULL AUTO_INCREMENT,
  `libelle_Article` varchar(50) DEFAULT NULL,
  `prix_Article` int(11) DEFAULT NULL,
  `id_Recu` int(11) DEFAULT NULL,
  `id_Categorie` int(11) NOT NULL,
  `id_Rayon` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_Article`),
  KEY `id_Recu` (`id_Recu`),
  KEY `id_Categorie` (`id_Categorie`),
  KEY `id_Rayon` (`id_Rayon`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `articles`
--

INSERT INTO `articles` (`id_Article`, `libelle_Article`, `prix_Article`, `id_Recu`, `id_Categorie`, `id_Rayon`) VALUES
(1, 'Vase', 220, NULL, 2, 1),
(2, 'Candia', 650, NULL, 2, 1),
(3, 'Cassoulet', 1800, NULL, 3, 2),
(4, 'Short', 2500, NULL, 1, 1),
(5, 'Top', 1500, NULL, 1, 1),
(6, 'Yaourt', 500, NULL, 2, 2),
(7, 'Mixeur', 20000, NULL, 1, 1),
(8, 'Dinor', 1200, NULL, 2, 1);

-- --------------------------------------------------------

--
-- Structure de la table `caisses`
--

DROP TABLE IF EXISTS `caisses`;
CREATE TABLE IF NOT EXISTS `caisses` (
  `id_Caisse` int(11) NOT NULL AUTO_INCREMENT,
  `nom_Caissiere` varchar(50) DEFAULT NULL,
  `tel_Caissiere` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_Caisse`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `caisses`
--

INSERT INTO `caisses` (`id_Caisse`, `nom_Caissiere`, `tel_Caissiere`) VALUES
(1, 'Konan Aya', '010012314'),
(2, 'Séri Rachelle', '010458752'),
(4, '   ', '   ');

-- --------------------------------------------------------

--
-- Structure de la table `categories`
--

DROP TABLE IF EXISTS `categories`;
CREATE TABLE IF NOT EXISTS `categories` (
  `id_Categorie` int(11) NOT NULL AUTO_INCREMENT,
  `libelle_Categorie` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_Categorie`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `categories`
--

INSERT INTO `categories` (`id_Categorie`, `libelle_Categorie`) VALUES
(1, 'Vêtements'),
(2, 'Produits laitiers'),
(3, 'Boîtes de Conserves');

-- --------------------------------------------------------

--
-- Structure de la table `clients`
--

DROP TABLE IF EXISTS `clients`;
CREATE TABLE IF NOT EXISTS `clients` (
  `id_Client` int(11) NOT NULL AUTO_INCREMENT,
  `nom_Client` varchar(50) DEFAULT NULL,
  `prenom_Client` varchar(50) DEFAULT NULL,
  `adress_Client` varchar(50) DEFAULT NULL,
  `tel_Client` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_Client`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `clients`
--

INSERT INTO `clients` (`id_Client`, `nom_Client`, `prenom_Client`, `adress_Client`, `tel_Client`) VALUES
(3, 'Koffi', 'Enerst', 'Abobo', '0101020347'),
(4, 'Aké', 'Franck', 'Yakro', '0145426510'),
(5, 'Coulibaly', 'Dagnogo', 'Tiémé', '0142153214'),
(6, 'Koffi Kan', 'Kanon', 'Yop', '0745487521'),
(7, 'aya', 'marie', 'abobo', '010215345');

-- --------------------------------------------------------

--
-- Structure de la table `commande_f`
--

DROP TABLE IF EXISTS `commande_f`;
CREATE TABLE IF NOT EXISTS `commande_f` (
  `id_CommandeF` int(11) NOT NULL AUTO_INCREMENT,
  `date_cf` date DEFAULT NULL,
  `qte_Com` int(11) NOT NULL,
  `id_Article` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_CommandeF`),
  KEY `id_Article` (`id_Article`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `commande_f`
--

INSERT INTO `commande_f` (`id_CommandeF`, `date_cf`, `qte_Com`, `id_Article`) VALUES
(1, '2022-04-21', 5, 1),
(2, '2022-05-20', 15, 2),
(3, '2022-05-11', 21, 1),
(4, '2022-04-26', 10, 5);

-- --------------------------------------------------------

--
-- Structure de la table `fournisseurs`
--

DROP TABLE IF EXISTS `fournisseurs`;
CREATE TABLE IF NOT EXISTS `fournisseurs` (
  `id_Fournisseur` int(11) NOT NULL AUTO_INCREMENT,
  `nom_Fournisseur` varchar(50) DEFAULT NULL,
  `address_Fournisseur` varchar(50) DEFAULT NULL,
  `tel_Fournisseur` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_Fournisseur`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `fournisseurs`
--

INSERT INTO `fournisseurs` (`id_Fournisseur`, `nom_Fournisseur`, `address_Fournisseur`, `tel_Fournisseur`) VALUES
(1, 'Konan', 'Bouaflé', '011245736'),
(2, 'Koffi', 'Koumassi', '0212365412');

-- --------------------------------------------------------

--
-- Structure de la table `livrer`
--

DROP TABLE IF EXISTS `livrer`;
CREATE TABLE IF NOT EXISTS `livrer` (
  `CommandeF_id` int(11) NOT NULL,
  `Fournisseur_id` int(11) NOT NULL,
  `date_Liv` date DEFAULT NULL,
  `qte_Liv` int(11) DEFAULT NULL,
  KEY `Fournisseur_id` (`Fournisseur_id`),
  KEY `CommandeF_id` (`CommandeF_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `livrer`
--

INSERT INTO `livrer` (`CommandeF_id`, `Fournisseur_id`, `date_Liv`, `qte_Liv`) VALUES
(1, 1, '2022-03-31', 13),
(2, 1, '2022-04-28', 1301),
(1, 1, '2022-04-12', 5);

-- --------------------------------------------------------

--
-- Structure de la table `rayons`
--

DROP TABLE IF EXISTS `rayons`;
CREATE TABLE IF NOT EXISTS `rayons` (
  `id_Rayon` int(11) NOT NULL AUTO_INCREMENT,
  `libelle_Rayon` varchar(50) DEFAULT NULL,
  `nbre_place` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_Rayon`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `rayons`
--

INSERT INTO `rayons` (`id_Rayon`, `libelle_Rayon`, `nbre_place`) VALUES
(1, 'Appareils menagers', 20),
(2, 'Vetements et chaussures', 15);

-- --------------------------------------------------------

--
-- Structure de la table `recus`
--

DROP TABLE IF EXISTS `recus`;
CREATE TABLE IF NOT EXISTS `recus` (
  `id_Recu` int(11) NOT NULL AUTO_INCREMENT,
  `Caisse_id` int(11) NOT NULL,
  `Client_id` int(11) NOT NULL,
  `numero_Recu` varchar(50) DEFAULT NULL,
  `qte_Achat` int(11) DEFAULT NULL,
  `montant_Achat` int(11) DEFAULT NULL,
  `date_Recu` datetime DEFAULT NULL,
  PRIMARY KEY (`id_Recu`),
  KEY `Caisse_id` (`Caisse_id`),
  KEY `Client_id` (`Client_id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `recus`
--

INSERT INTO `recus` (`id_Recu`, `Caisse_id`, `Client_id`, `numero_Recu`, `qte_Achat`, `montant_Achat`, `date_Recu`) VALUES
(1, 2, 4, 'KJ1M14', 5, 100000, '2022-04-25 12:46:02'),
(2, 2, 4, 'HU1MT6', 3, 1950, '2022-04-25 12:46:32'),
(3, 1, 3, 'AXF4E00', 2, 3600, '2022-05-08 04:18:55');

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `UserName` varchar(50) NOT NULL,
  `Password` varchar(50) NOT NULL,
  `id` int(3) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `users`
--

INSERT INTO `users` (`UserName`, `Password`, `id`) VALUES
('Aude', 'aude', 1),
('Luna', 'luna', 2);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
