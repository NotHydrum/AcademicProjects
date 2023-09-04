checksum :: [Integer] -> [Integer]
checksum xs = (control (list xs)):xs

list :: [Integer] -> Integer
list xs = sum (zipWith (*) xs [1..])

control :: Integer -> Integer
control x = mod (10 - mod x 10) 10



palavras :: [String]
palavras = [concat ([fs]:ss:ts:[]) | fs <- firstSyllable, ss <- secondSyllable, ts <- thirdSyllable, last ss /= head ts]

firstSyllable :: String
firstSyllable = vowels

secondSyllable :: [String]
secondSyllable = [x:y:[] | x <- consonants, y <- vowels]

thirdSyllable :: [String]
thirdSyllable = [x:y:[] | x <- vowels, y <- consonants]

vowels :: String
vowels = "aeiou"

consonants :: String
consonants = "bcdfghjklmnpqrstvwxyz"
