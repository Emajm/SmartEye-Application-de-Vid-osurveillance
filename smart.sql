
CREATE TABLE IF NOT EXISTS `image_rec` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code_image` int(10) NOT NULL,
  `prenom` varchar(30) NOT NULL,
  `nom` varchar(20) NOT NULL,
  `tel` varchar(20) NOT NULL,
  `age` int(10) NOT NULL,
  `mail` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=latin1;

