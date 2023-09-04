module Cesar (cesarEncrypt, cesarDecrypt) where

import Library
import Data.List

cesarEncrypt :: String -> Int -> String
cesarEncrypt ms c = [encode m c | m <- ms]

cesarDecrypt :: String -> Int -> String
cesarDecrypt ms c = [decode m c | m <- ms]

encode :: Char -> Int -> Char
encode m c
    | elem m alphabet = alphabet !! underoverflow ((justInt (elemIndex m alphabet)) + c)
    | elem m capitals = capitals !! underoverflow ((justInt (elemIndex m capitals)) + c)
    | otherwise = m

decode :: Char -> Int -> Char
decode m c
    | elem m alphabet = alphabet !! underoverflow ((justInt (elemIndex m alphabet)) - c)
    | elem m capitals = capitals !! underoverflow ((justInt (elemIndex m capitals)) - c)
    | otherwise = m
