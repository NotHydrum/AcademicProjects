module Vigenere (vigenereEncrypt, vigenereDecrypt) where

import Library
import Data.List

vigenereEncrypt :: String -> String -> String
vigenereEncrypt ms c = encode ms (cycle [justInt (elemIndex x capitals) | x <- c])

vigenereDecrypt :: String -> String -> String
vigenereDecrypt ms c = decode ms (cycle [justInt (elemIndex x capitals) | x <- c])

encode :: String -> [Int] -> String
encode [] _ = []
encode _ [] = []
encode (m:ms) (s:ss)
    | elem m alphabet = [alphabet !! underoverflow ((justInt (elemIndex m alphabet)) + s)] ++ encode ms ss
    | elem m capitals = [capitals !! underoverflow ((justInt (elemIndex m capitals)) + s)] ++ encode ms ss
    | otherwise = [m] ++ encode ms (s:ss)

decode :: String -> [Int] -> String
decode [] _ = []
decode _ [] = []
decode (m:ms) (s:ss)
    | elem m alphabet = [alphabet !! underoverflow ((justInt (elemIndex m alphabet)) - s)] ++ decode ms ss
    | elem m capitals = [capitals !! underoverflow ((justInt (elemIndex m capitals)) - s)] ++ decode ms ss
    | otherwise = [m] ++ decode ms (s:ss)
