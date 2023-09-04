module Tests (runTests) where

import Test.QuickCheck
import Cesar
import Vigenere
import Substitui
import Library
import Data.Char

runTests = do
    quickCheck prop_cesar_union
    quickCheck prop_cesar_original
    quickCheck prop_vigenere_original
    quickCheck prop_substitui_original
    quickCheck prop_cesar_wrap
    quickCheck prop_cesar_cycle
    quickCheck prop_vigenere_duplication

punctuation = ",;.?!:-()"

newtype ValidMessage = ValidMessage String

instance Show ValidMessage where
    show (ValidMessage x) = x

instance Arbitrary ValidMessage where
    arbitrary = ValidMessage <$> listOf (elements (alphabet ++ capitals ++ punctuation ++ " "))

newtype ValidKey = ValidKey String

instance Show ValidKey where
    show (ValidKey x) = x

instance Arbitrary ValidKey where
    arbitrary = ValidKey <$> listOf1 (elements capitals)

--Using a Cesar with cypher 'a' followed by a Cesar with cypher 'b' is the same as using a Cesar with cypher 'a' + 'b'
prop_cesar_union :: ValidMessage -> Int -> Int -> Bool
prop_cesar_union (ValidMessage m) a b = cesarEncrypt (cesarEncrypt m a) b == cesarEncrypt m (a + b)

--Decrypting an encrypted message with the cypher used to encrypt it gives back the original message
prop_cesar_original :: ValidMessage -> Int -> Bool
prop_cesar_original (ValidMessage m) c = m == cesarDecrypt (cesarEncrypt m c) c

--Decrypting an encrypted message with the cypher used to encrypt it gives back the original message
prop_vigenere_original :: ValidMessage -> ValidKey -> Bool
prop_vigenere_original (ValidMessage m) (ValidKey c) = m == vigenereDecrypt (vigenereEncrypt m c) c

--Decrypting an encrypted message with the cypher used to encrypt it gives back the original message
prop_substitui_original :: ValidMessage -> ValidKey -> Bool
prop_substitui_original (ValidMessage m) (ValidKey c) = m == substituiDecrypt (substituiEncrypt m c) c

--The alphabet wraps around correctly
prop_cesar_wrap :: ValidMessage -> Int -> Bool
prop_cesar_wrap (ValidMessage m) c = cesarEncrypt m c == cesarEncrypt m (c - 26) && cesarEncrypt m c == cesarEncrypt m (c + 26)

--Encrypting a previously encripted message with cypher (26 - 'original key') should return the original decrypted message.
prop_cesar_cycle :: ValidMessage -> Int -> Bool
prop_cesar_cycle (ValidMessage m) c = m == cesarEncrypt (cesarEncrypt m c) (26 - c)

--A message encrypted with a certain cypher (ex: "HASKELL") should equal a message encrypted with that same cypher duplicated ("HASKELLHASKELL")
prop_vigenere_duplication :: ValidMessage -> ValidKey -> Bool
prop_vigenere_duplication (ValidMessage m) (ValidKey c) = vigenereEncrypt m c == vigenereEncrypt m (c++c)
