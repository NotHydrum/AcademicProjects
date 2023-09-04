module Substitui (substituiEncrypt, substituiDecrypt) where

import Library
import Data.Char
import Data.List

substituiEncrypt :: String -> String -> String
substituiEncrypt ms c = [encode m cs | m <- ms]
                        where cs = codedAlphabet c

substituiDecrypt :: String -> String -> String
substituiDecrypt ms c = [decode m cs | m <- ms]
                        where cs = codedAlphabet c

encode :: Char -> String -> Char
encode m cs
    | elem m alphabet = (map toLower cs) !! (justInt (elemIndex m alphabet))
    | elem m capitals = cs !! (justInt (elemIndex m capitals))
    | otherwise = m

decode :: Char -> String -> Char
decode m cs
    | elem m alphabet = alphabet !! (justInt (elemIndex m (map toLower cs)))
    | elem m capitals = capitals !! (justInt (elemIndex m cs))
    | otherwise = m

codedAlphabet :: String -> String
codedAlphabet c = reverse (codedAlphabet2 (reverse (c ++ capitals)))

codedAlphabet2 :: String -> String
codedAlphabet2 [] = []
codedAlphabet2 (c:cs)
    | elem c cs = codedAlphabet2 cs
    | otherwise = [c] ++ codedAlphabet2 cs
