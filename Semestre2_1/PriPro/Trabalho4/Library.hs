module Library (justInt, underoverflow, alphabet, capitals) where

justInt :: Maybe Int -> Int
justInt (Just x) = x

underoverflow :: Int -> Int
underoverflow x
    | x < 0 = underoverflow (x + 26)
    | x >= 0 && x <= 25 = x
    | x > 25 = mod x 26

alphabet :: String
alphabet = "abcdefghijklmnopqrstuvwxyz"

capitals :: String
capitals = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
